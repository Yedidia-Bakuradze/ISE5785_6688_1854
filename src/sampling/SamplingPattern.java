package sampling;

/**
 * Enumeration representing different sampling patterns for ray distribution strategies.
 * These patterns determine how rays are distributed when sampling an area.
 */
public enum SamplingPattern {
    /**
     * Jittered sampling pattern - combines regular grid with random offsets to reduce aliasing artifacts
     * while maintaining good distribution properties.
     */
    JITTERED,

    /**
     * Random sampling pattern - uses completely random distribution of samples.
     * This can help eliminate regular patterns but may create noise.
     */
    RANDOM,

    /**
     * Grid sampling pattern - uses regular grid distribution of samples.
     * Simple and predictable but can create aliasing artifacts.
     */
    GRID,
}
