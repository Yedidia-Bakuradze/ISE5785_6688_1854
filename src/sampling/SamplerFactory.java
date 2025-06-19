package sampling;

import renderer.EffectType;

// Factory for creating appropriate samplers
public class SamplerFactory {
    public static TargetAreaBase createSampler(EffectType effectType, SamplingConfiguration config) {
        return switch (effectType) {
            case DIFFUSIVE_GLASS -> new DiffusiveTargetArea(config);

            //  TODO: Future features
//            case SOFT_SHADOW -> new SoftShadowSampler(config);
//            case ANTI_ALIASING -> new AntiAliasingSampler(config);
//            case GLOSSY_REFLECTION -> new GlossyReflectionSampler(config);
//            case DEPTH_OF_FIELD -> new DepthOfFieldSampler(config);
        };
    }
}