package renderer;

/**
 * Enumeration of special effects available for rendering.
 * Each effect type represents a different visual enhancement technique.
 */
public enum EffectType {
    /**
     * Simulates diffusion through partially transparent materials like frosted glass
     */
    DIFFUSIVE_GLASS,

    /**
     * Creates realistic shadows with soft edges instead of sharp boundaries
     */
    SOFT_SHADOW,

    /**
     * Reduces jagged edges by sampling multiple rays per pixel
     */
    ANTI_ALIASING,

    /**
     * Creates blurred reflections on smooth but not perfectly polished surfaces
     */
    GLOSSY_REFLECTION,

    /**
     * Simulates camera focus effects with blurred background and foreground
     */
    DEPTH_OF_FIELD
}