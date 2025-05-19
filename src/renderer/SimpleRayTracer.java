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
     * @return the calculated color to paint that pixel with
     */
    private Color calcColor(Intersectable.Intersection intersection, Vector rayDirection) {
        if (!preprocessIntersection(intersection, rayDirection)) return new Color(BLACK);
        return this.scene.ambientLight.getIntensity()
                .scale(intersection.material.kA)
                .add(calcColorLocalEffects(intersection));
    }


    private boolean preprocessIntersection(Intersectable.Intersection intersection, Vector rayDirection) {
        intersection.rayDirection = rayDirection;
        intersection.normal = intersection.geometry.getNormal(intersection.point);
        intersection.rayNormalProduct = alignZero(intersection.rayDirection.dotProduct(intersection.normal));
        return intersection.rayNormalProduct != 0;
    }

    private boolean setLightSource(Intersectable.Intersection intersection, LightSource lightSource) {
        intersection.lightSource = lightSource;
        intersection.lightDirection = lightSource.getL(intersection.point).normalize();
        intersection.lightNormalProduct = alignZero(intersection.lightDirection.dotProduct(intersection.normal));
        return intersection.lightNormalProduct * intersection.rayNormalProduct > 0;
    }

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

    private Vector calcReflection(Intersectable.Intersection intersection) {
        return intersection.lightDirection.add(
                (
                        intersection.normal.scale(
                                intersection.lightDirection.dotProduct(intersection.normal)
                        ).scale(-2)
                )
        );
    }

    private Double3 calcSpecular(Intersectable.Intersection intersection) {
        double factor = intersection
                .rayDirection
                .scale(-1)
                .dotProduct(calcReflection(intersection)) <= 0 ? 0 : intersection.rayDirection.scale(-1).dotProduct(calcReflection(intersection));
        factor = Math.pow(factor, intersection.material.nShininess);
        return intersection.material.kS.scale(factor);
    }

    private Double3 calcDiffusive(Intersectable.Intersection intersection) {
        return intersection.material.kD.scale(intersection.lightNormalProduct);
    }
}
