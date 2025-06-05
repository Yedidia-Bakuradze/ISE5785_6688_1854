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

    private static final int MAX_CALC_COLOR_LEVEL = 10;
    private static final double MIN_CALC_COLOR_K = 0.001;
    private static final Double3 INITIAL_K = Double3.ONE;

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
        Intersectable.Intersection closestIntersection = findClosestIntersection(ray);
        return closestIntersection == null
                ? scene.backgroundColor
                : calcColor(closestIntersection, ray.getDirection());
    }

    public Intersectable.Intersection findClosestIntersection(Ray ray) {
        List<Intersectable.Intersection> intersections = scene.geometries.calculateIntersections(ray);
        return intersections == null
                ? null
                : ray.findClosestIntersection(intersections);
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
                .add(calcColor(intersection, MAX_CALC_COLOR_LEVEL, INITIAL_K));
    }

    private Color calcColor(Intersectable.Intersection intersection, int level, Double3 k) {
        Color color = calcColorLocalEffects(intersection, k);
        return 1 == level ? color : color.add(calcGlobalEffects(intersection, level, k));
    }

    private Color calcGlobalEffect(Ray interactRay, int level, Double3 k, Double3 kx) {
        Double3 kkx = kx.product(k);
        if (kkx.lowerThan(MIN_CALC_COLOR_K)) return Color.BLACK;
        Intersectable.Intersection closestIntersection = findClosestIntersection(interactRay);
        if (closestIntersection == null) return scene.backgroundColor.scale(kx);
        return preprocessIntersection(closestIntersection, interactRay.getDirection())
                ? calcColor(closestIntersection, level - 1, kkx).scale(kx) : Color.BLACK;
    }

    private Color calcGlobalEffects(Intersectable.Intersection intersection, int level, Double3 k) {
        return calcGlobalEffect(calcRefractionRay(intersection), level, k, intersection.material.kT)
                .add(calcGlobalEffect(calcReflectionRay(intersection), level, k, intersection.material.kR));
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
    private Color calcColorLocalEffects(Intersectable.Intersection intersection, Double3 k) {
        Color color = intersection.geometry.getEmission();
        for (LightSource lightSource : scene.lights) {
            if (!setLightSource(intersection, lightSource)) continue;
            Double3 ktr = transparency(intersection);
            if (!ktr.product(k).greaterThan(MIN_CALC_COLOR_K)) continue;

            color = color.add(
                    lightSource
                            .getIntensity(intersection.point).scale(ktr)
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
        return intersection.lightDirection.subtract(
                intersection.normal.scale(
                        2 * intersection.lightDirection.dotProduct(intersection.normal)
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

    private Ray calcReflectionRay(Intersectable.Intersection intersection) {
        return new Ray(intersection.point, calcReflection(intersection), intersection.normal);
    }

    private Ray calcRefractionRay(Intersectable.Intersection intersection) {
        return new Ray(intersection.point, intersection.rayDirection, intersection.normal);
    }

    private List<Intersectable.Intersection> castShadowRay(Intersectable.Intersection intersection) {
        if (!intersection.material.kR.lowerThan(MIN_CALC_COLOR_K)) return null;
        Ray shadowRay = new Ray(intersection.point, intersection.lightDirection.scale(-1), intersection.normal);
        //TODO: Self improvement: Might be better if used a new method that returns a boolean value if one intersection exists
        return scene.geometries.calculateIntersections(shadowRay, intersection.lightSource.getDistance(intersection.point));
    }

    private boolean unshaded(Intersectable.Intersection intersection) {
        return castShadowRay(intersection) == null;
    }

    private Double3 transparency(Intersectable.Intersection intersection) {
        Double3 ktr = Double3.ONE;
        List<Intersectable.Intersection> intersections = castShadowRay(intersection);
        if (intersections == null) return ktr;
        double lightDistance = intersection.lightSource.getDistance(intersection.point);
        for (Intersectable.Intersection shadowIntersection : intersections) {
            if (shadowIntersection.point.distance(intersection.point) >= lightDistance) continue;
            ktr = ktr.product(shadowIntersection.material.kT);
            if (ktr.lowerThan(MIN_CALC_COLOR_K)) return Double3.ZERO;
        }
        return ktr;
    }

}
