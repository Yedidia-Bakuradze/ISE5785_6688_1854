package renderer;

import geometries.Intersectable;
import lighting.LightSource;
import primitives.Color;
import primitives.Double3;
import primitives.Ray;
import primitives.Vector;
import scene.Scene;

import java.util.List;

import static java.awt.Color.BLACK;
import static primitives.Util.alignZero;

/**
 * Represents a type of ray tracer which is the simplest and most basic ray
 */
public class SimpleRayTracer extends RayTracerBase {

    /**
     * Initiates the simple ray tracer with a scene containing the objects
     *
     * @param scene the scene which contains the geometries that are a part of that image and scene
     */
    SimpleRayTracer(Scene scene) {
        super(scene);
    }

    @Override
    public Color traceRay(Ray ray) {
        List<Intersectable.Intersection> listOfIntersections = scene.geometries.calculateIntersections(ray);
        return listOfIntersections != null
                ? calcColor(ray.findClosestIntersection(listOfIntersections), ray.getDirection())
                : scene.backgroundColor;
    }

    /**
     * Calculates the color on the given point with the ambient light and the emission of the geometry
     *
     * @param intersection the intersection onto the calculation is made
     * @param rayDirection the direction of the ray
     * @return the calculated color to paint that pixel with
     */
    private Color calcColor(Intersectable.Intersection intersection, Vector rayDirection) {
        if (!preprocessIntersection(intersection, rayDirection)) return new Color(BLACK);
        return this.scene.ambientLight
                .getIntensity().scale(intersection.material.kA)
                .add(calcColorLocalEffects(intersection));
    }

    /**
     * Preprocesses the intersection to calculate necessary values.
     *
     * @param intersection The intersection to preprocess.
     * @param rayDirection The direction of the ray.
     * @return True if preprocessing is successful, false otherwise.
     */
    private boolean preprocessIntersection(Intersectable.Intersection intersection, Vector rayDirection) {
        intersection.rayDirection = rayDirection;
        intersection.normal = intersection.geometry.getNormal(intersection.point);
        intersection.rayNormalProduct = alignZero(intersection.rayDirection.dotProduct(intersection.normal));
        return intersection.rayNormalProduct != 0;
    }

    /**
     * Sets the light source for the intersection.
     *
     * @param intersection The intersection to set the light source for.
     * @param lightSource  The light source to set.
     * @return True if the light source is valid, false otherwise.
     */
    private boolean setLightSource(Intersectable.Intersection intersection, LightSource lightSource) {
        intersection.lightSource = lightSource;
        intersection.lightDirection = lightSource.getL(intersection.point).normalize();
        intersection.lightNormalProduct = alignZero(intersection.lightDirection.dotProduct(intersection.normal));
        return intersection.lightNormalProduct * intersection.rayNormalProduct > 0;
    }

    /**
     * Calculates the local effects of lighting at the intersection.
     *
     * @param intersection The intersection to calculate effects for.
     * @return The color resulting from local lighting effects.
     */
    private Color calcColorLocalEffects(Intersectable.Intersection intersection) {
        Color color = intersection.geometry.getEmission();
        for (LightSource lightSource : scene.lights) {
            if (!setLightSource(intersection, lightSource)) continue;
            color = color.add(
                    lightSource
                            .getIntensity(intersection.point)
                            .scale(calcDiffusive(intersection).add(calcSpecular(intersection))));
        }
        return color;
    }

    /**
     * Calculates the reflection vector at the intersection.
     *
     * @param intersection The intersection to calculate reflection for.
     * @return The reflection vector.
     */
    private Vector calcReflection(Intersectable.Intersection intersection) {
        return intersection.lightDirection.add(
                (
                        intersection.normal.scale(
                                intersection.lightDirection.dotProduct(intersection.normal)
                        ).scale(-2)
                )
        );
    }

    /**
     * Calculates the specular lighting effect at the intersection.
     *
     * @param intersection The intersection to calculate specular effect for.
     * @return The specular lighting effect as a Double3.
     */
    private Double3 calcSpecular(Intersectable.Intersection intersection) {
        double factor = intersection
                .rayDirection
                .scale(-1)
                .dotProduct(calcReflection(intersection)) <= 0 ? 0 : intersection.rayDirection.scale(-1).dotProduct(calcReflection(intersection));
        factor = Math.pow(factor, intersection.material.nShininess);
        return intersection.material.kS.scale(factor);
    }

    /**
     * Calculates the diffusive lighting effect at the intersection.
     *
     * @param intersection The intersection to calculate diffusive effect for.
     * @return The diffusive lighting effect as a Double3.
     */
    private Double3 calcDiffusive(Intersectable.Intersection intersection) {
        Double3 res = intersection.material.kD.scale(intersection.lightNormalProduct);
        double q1 = res.d1() < 0 ? -res.d1() : res.d1();
        double q2 = res.d2() < 0 ? -res.d2() : res.d2();
        double q3 = res.d3() < 0 ? -res.d3() : res.d3();
        return new Double3(q1, q2, q3);
    }
}
