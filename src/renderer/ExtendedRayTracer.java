package renderer;

import primitives.*;
import sampling.TargetAreaBase;
import scene.Scene;

import java.util.List;
import java.util.Map;

import static geometries.Intersectable.Intersection;

/**
 * Extended ray tracer with support for advanced visual effects like soft shadows,
 * diffusive glass, depth of field, and antialiasing through multiple sampling techniques.
 * Extends the basic ray tracer with distribution ray capabilities.
 */
public class ExtendedRayTracer extends SimpleRayTracer {
    /**
     * The target area for distributing rays when using effects like depth of field or soft shadows
     */
    private final Map<EffectType, TargetAreaBase> targetArea;
    /**
     * Indicates whether advanced features are enabled.
     */
    protected boolean isFeatureEnabled = false;

    //TODO: Effect -> Effects

    /**
     * Constructs an extended ray tracer with the specified scene and target areas for special effects.
     *
     * @param scene      The scene to be rendered.
     * @param targetArea Map of effect types to their respective target area implementations
     */
    public ExtendedRayTracer(Scene scene, Map<EffectType, TargetAreaBase> targetArea) {
        super(scene);
        this.targetArea = targetArea;
        this.isFeatureEnabled = true;
    }

    /**
     * Constructs an extended ray tracer with the specified scene.
     *
     * @param scene The scene to be rendered.
     */
    public ExtendedRayTracer(Scene scene) {
        super(scene);
        this.targetArea = null;
    }

    /**
     * Calculates the sum of all global effects (reflection and refraction).
     *
     * @param intersection the intersection to calculate effects for
     * @param level        the recursion level
     * @param k            the color attenuation factor
     * @return the color contribution from all global effects
     */
    protected Color calcGlobalEffects(Intersection intersection, int level, Double3 k) {
        if (!isFeatureEnabled) return super.calcGlobalEffects(intersection, level, k);

        Color refractionColor;
        // Handle transmission/refraction (what goes THROUGH the object)
        if (intersection.material.roughness <= 0.0)
            refractionColor = calcGlobalEffect(calcRefractionRay(intersection), level, k, intersection.material.kT);
        else {
            // Diffusive transmission: multiple sampled rays going through the object
            List<Ray> refractionRays = targetArea.get(EffectType.DIFFUSIVE_GLASS).generateRays(intersection);

            refractionColor = refractionRays.stream()
                    .map(refractionRay -> calcGlobalEffect(refractionRay, level, k, intersection.material.kT))
                    .reduce(Color.BLACK, Color::add);

            // Average the refraction color
            if (!refractionRays.isEmpty()) {
                refractionColor = refractionColor.reduce(refractionRays.size());
            }
        }

        return refractionColor.add(calcGlobalEffect(calcReflectionRay(intersection), level, k, intersection.material.kR));
    }
}
