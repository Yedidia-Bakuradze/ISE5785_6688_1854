package renderer;

import lighting.LightSource;
import primitives.*;
import sampling.TargetAreaBase;
import scene.Scene;

import java.util.List;

import static geometries.Intersectable.Intersection;
import static primitives.Util.alignZero;

/**
 * Represents a type of ray tracer which is the simplest and most basic ray
 */
public class SimpleRayTracer extends RayTracerBase {

    /**
     * The maximum recursion level for color calculation.
     */
    private static final int MAX_CALC_COLOR_LEVEL = 10;
    /**
     * The minimum value for color contribution to be considered.
     */
    private static final double MIN_CALC_COLOR_K = 0.001;
    /**
     * The initial k value for color calculation.
     */
    private static final Double3 INITIAL_K = Double3.ONE;

    private final TargetAreaBase targetArea;

    /**
     * Initiates the simple ray tracer with a scene containing the objects
     *
     * @param scene the scene which contains the geometries that are a part of that image and scene
     */
    SimpleRayTracer(Scene scene, TargetAreaBase targetArea) {
        super(scene);
        this.targetArea = targetArea;
    }

    @Override
    public Color traceRay(Ray ray) {
        Intersection closestIntersection = findClosestIntersection(ray);
        return closestIntersection == null
                ? scene.backgroundColor
                : calcColor(closestIntersection, ray.getDirection());
    }

    /**
     * Finds the closest intersection of the given ray with the scene geometries.
     *
     * @param ray The ray to check for intersections.
     * @return The closest intersection, or null if none.
     */
    public Intersection findClosestIntersection(Ray ray) {
        return ray.findClosestIntersection(scene.geometries.calculateIntersections(ray));
    }

    /**
     * Calculates the color at the intersection point, including recursive effects.
     *
     * @param intersection the intersection onto the calculation is made
     * @param rayDirection the direction of the ray
     * @return the calculated color to paint that pixel with
     */
    private Color calcColor(Intersection intersection, Vector rayDirection) {
        return preprocessIntersection(intersection, rayDirection)
                ? this.scene.ambientLight.getIntensity().scale(intersection.material.kA)
                .add(calcColor(intersection, MAX_CALC_COLOR_LEVEL, INITIAL_K))
                : Color.BLACK;
    }

    /**
     * Calculates the color at the intersection point, including recursive effects.
     *
     * @param intersection the intersection onto the calculation is made
     * @param level        the recursion level
     * @param k            the color attenuation factor
     * @return the calculated color to paint that pixel with
     */
    private Color calcColor(Intersection intersection, int level, Double3 k) {
        Color color = calcColorLocalEffects(intersection, k);
        return 1 == level ? color : color.add(calcGlobalEffects(intersection, level, k));
    }

    /**
     * Calculates the global lighting effects (reflection and refraction).
     *
     * @param interactRay the ray for the effect
     * @param level       the recursion level
     * @param k           the color attenuation factor
     * @param kx          the effect coefficient
     * @return the color contribution from the global effect
     */
    private Color calcGlobalEffect(Ray interactRay, int level, Double3 k, Double3 kx) {
        Double3 kkx = kx.product(k);
        if (kkx.lowerThan(MIN_CALC_COLOR_K)) return Color.BLACK;
        Intersection closestIntersection = findClosestIntersection(interactRay);
        if (closestIntersection == null) return scene.backgroundColor.scale(kx);
        return preprocessIntersection(closestIntersection, interactRay.getDirection())
                ? calcColor(closestIntersection, level - 1, kkx).scale(kx) : Color.BLACK;
    }

    /**
     * Calculates the sum of all global effects (reflection and refraction).
     *
     * @param intersection the intersection to calculate effects for
     * @param level        the recursion level
     * @param k            the color attenuation factor
     * @return the color contribution from all global effects
     */
    private Color calcGlobalEffects(Intersection intersection, int level, Double3 k) {
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
    private boolean preprocessIntersection(Intersection intersection, Vector rayDirection) {
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
    private boolean setLightSource(Intersection intersection, LightSource lightSource) {
        intersection.lightSource = lightSource;
        intersection.lightDirection = lightSource.getL(intersection.point).normalize();
        intersection.lightNormalProduct = alignZero(intersection.lightDirection.dotProduct(intersection.normal));
        return intersection.lightNormalProduct * intersection.rayNormalProduct > 0;
    }

    /**
     * Calculates the local effects of lighting at the intersection.
     *
     * @param intersection The intersection to calculate effects for.
     * @param k            The color attenuation factor.
     * @return The color resulting from local lighting effects.
     */
    private Color calcColorLocalEffects(Intersection intersection, Double3 k) {
        Color color = intersection.geometry.getEmission();
        for (LightSource lightSource : scene.lights) {
            if (!setLightSource(intersection, lightSource)) continue;
            Double3 ktr = transparency(intersection);
            if (ktr.product(k).lowerThan(MIN_CALC_COLOR_K)) continue;

            color = color.add(
                    lightSource
                            .getIntensity(intersection.point).scale(ktr)
                            .scale(calcDiffusive(intersection).add(calcSpecular(intersection))));
        }
        return color;
    }

    /**
     * Calculates the reflection vector of an original direction  at the intersection
     *
     * @param intersection The intersection to calculate reflection for
     * @param direction    the original vector
     * @return The reflection vector
     */
    private Vector calcReflection(Intersection intersection, Vector direction) {
        return direction.subtract(
                intersection.normal.scale(2 * direction.dotProduct(intersection.normal))
        );
    }

    /**
     * Calculates the specular lighting effect at the intersection.
     *
     * @param intersection The intersection to calculate specular effect for.
     * @return The specular lighting effect as a Double3.
     */
    private Double3 calcSpecular(Intersection intersection) {
        double minusVR = -alignZero(intersection.rayDirection.dotProduct(calcReflection(intersection, intersection.lightDirection)));
        return minusVR <= 0 ? Double3.ZERO
                : intersection.material.kS.scale(Math.pow(minusVR, intersection.material.nShininess));
    }

    /**
     * Calculates the diffusive lighting effect at the intersection.
     *
     * @param intersection The intersection to calculate diffusive effect for.
     * @return The diffusive lighting effect as a Double3.
     */
    private Double3 calcDiffusive(Intersection intersection) {
        return intersection.material.kD.scale(intersection.lightNormalProduct < 0
                ? -intersection.lightNormalProduct : intersection.lightNormalProduct);
    }

    /**
     * Calculates the reflection ray at the intersection.
     *
     * @param intersection The intersection to calculate the reflection ray for.
     * @return The reflection ray.
     */
    private Ray calcReflectionRay(Intersection intersection) {
        return new Ray(intersection.point, calcReflection(intersection, intersection.rayDirection), intersection.normal);
    }

    /**
     * Calculates the refraction ray at the intersection.
     *
     * @param intersection The intersection to calculate the refraction ray for.
     * @return The refraction ray.
     */
    private Ray calcRefractionRay(Intersection intersection) {
        return new Ray(intersection.point, intersection.rayDirection, intersection.normal);
    }

    /**
     * Casts a shadow ray from the intersection point to check for occlusion.
     *
     * @param intersection The intersection to cast the shadow ray from.
     * @return A list of intersections with shadowing objects, or null if unshaded.
     */
    private List<Intersection> castShadowRay(Intersection intersection) {
        if (intersection.material.kR.greaterThan(MIN_CALC_COLOR_K)) return null;

        Ray shadowRay = new Ray(intersection.point, intersection.lightDirection.scale(-1), intersection.normal);
        return scene.geometries.calculateIntersections(shadowRay, intersection.lightSource.getDistance(intersection.point));
    }

    /**
     * Checks if the intersection point is unshaded (not in shadow).
     *
     * @param intersection The intersection to check.
     * @return true if unshaded, false otherwise.
     */
    @SuppressWarnings("unused")
    private boolean unshaded(Intersection intersection) {
        var intersections = castShadowRay(intersection);
        if (intersections == null) return true;

        Double3 ktr = Double3.ONE;
        for (Intersection shadowIntersection : intersections) {
            ktr = ktr.product(shadowIntersection.material.kT);
            if (ktr.lowerThan(MIN_CALC_COLOR_K)) return false;
        }
        return true;
    }

    /**
     * Calculates the transparency factor at the intersection point.
     *
     * @param intersection The intersection to calculate transparency for.
     * @return The transparency factor as a Double3.
     */
    private Double3 transparency(Intersection intersection) {
        Double3 ktr = Double3.ONE;
        var intersections = castShadowRay(intersection);
        if (intersections == null) return ktr;

        for (Intersection shadowIntersection : intersections) {
            ktr = ktr.product(shadowIntersection.material.kT);
            if (ktr.lowerThan(MIN_CALC_COLOR_K)) return Double3.ZERO;
        }
        return ktr;
    }

}
