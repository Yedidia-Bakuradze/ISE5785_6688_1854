package sampling;

import renderer.EffectType;

/**
 * Factory class for creating appropriate samplers for different rendering effects.
 * Provides static methods to instantiate specialized target areas based on effect type.
 */
public class SamplerFactory {

    /**
     * constructor to prevent instantiation of the factory class.
     * This class is intended to be used statically.
     */
    private SamplerFactory() {
    }

    /**
     * Creates and returns a sampler appropriate for the specified effect type with the given configuration.
     *
     * @param effectType The type of effect for which to create a sampler
     * @param config     The sampling configuration containing mode, shape, and pattern settings
     * @return A target area implementation appropriate for the specified effect type
     */
    public static TargetAreaBase createSampler(EffectType effectType, SamplingConfiguration config) {
        return switch (effectType) {
            case DIFFUSIVE_GLASS -> new DiffusiveTargetArea(config);
            case SOFT_SHADOW -> null; // SoftShadowSampler is not implemented yet
            case ANTI_ALIASING -> null; // AntiAliasingSampler is not implemented yet
            case GLOSSY_REFLECTION -> null; // GlossyReflectionSampler is not implemented yet
            case DEPTH_OF_FIELD -> null; // DepthOfFieldSampler is not implemented yet
        };
    }
}