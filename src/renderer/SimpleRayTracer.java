package renderer;

import geometries.Intersectable;
import lighting.LightSource;
import primitives.*;
import scene.Scene;

import java.util.List;

import static primitives.Color.BLACK;
import static primitives.Util.alignZero;

/**
 * Represents a type of ray tracer which is the simplest and most basic ray
 */
public class SimpleRayTracer extends RayTracerBase {

    private static final double DELTA = 0.1;

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
        return listOfIntersections == null ? scene.backgroundColor
                : calcColor(ray.findClosestIntersection(listOfIntersections), ray.getDirection());
    }

    /**
     * Calculates the color on the given point with the ambient light and the emission of the geometry
     *
     * @param intersection the intersection onto the calculation is made
     * @param rayDirection the direction of the ray
     * @return the calculated color to paint that pixel with
     */
    private Color calcColor(Intersectable.Intersection intersection, Vector rayDirection) {
        if (!preprocessIntersection(intersection, rayDirection)) return BLACK;
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
            if (!setLightSource(intersection, lightSource) || unshaded(intersection)) continue;
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
        double minusVR = -alignZero(intersection.rayDirection.dotProduct(calcReflection(intersection)));
        return minusVR <= 0 ? Double3.ZERO
                : intersection.material.kS.scale(Math.pow(minusVR, intersection.material.nShininess));
    }

    /**
     * Calculates the diffusive lighting effect at the intersection.
     *
     * @param intersection The intersection to calculate diffusive effect for.
     * @return The diffusive lighting effect as a Double3.
     */
    private Double3 calcDiffusive(Intersectable.Intersection intersection) {
        return intersection.material.kD.scale(intersection.lightNormalProduct < 0
                ? -intersection.lightNormalProduct : intersection.lightNormalProduct);
    }

    private boolean unshaded(Intersectable.Intersection intersection) {
        Vector pointToLight = intersection.lightDirection.scale(-1);
        Vector delta = intersection.normal.scale(intersection.lightNormalProduct < 0 ? DELTA : -DELTA);
        Ray shadowRay = new Ray(intersection.point.add(delta), pointToLight);
        var intersections = scene.geometries.findIntersections(shadowRay);
        return intersections == null;
    }
}
