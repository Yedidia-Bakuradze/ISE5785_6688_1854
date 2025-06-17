package sampling;

/**
 * Enumeration representing different shapes for target areas used in ray distribution.
 * The shape affects how rays are distributed within the sampling area.
 */
public enum TargetAreaType {
    /**
     * Circular target area - samples are distributed within a circular region
     */
    CIRCLE,

    /**
     * Square target area - samples are distributed within a square region
     */
    SQUARE,
}
