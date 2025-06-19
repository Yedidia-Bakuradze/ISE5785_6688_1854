package sampling;

import java.util.Random;

/**
 * Configuration class for defining sampling parameters used in ray distribution techniques.
 * Encapsulates all settings needed for different sampling strategies.
 */
public class SamplingConfiguration {
    /**
     * The density and distribution pattern of samples (e.g., 2x2, 5x5)
     */
    public final SamplingMode mode;

    /**
     * The shape of the sampling area (e.g., circle, square)
     */
    public final TargetAreaType shape;

    /**
     * The sampling pattern strategy (e.g., jittered, random, grid)
     */
    public final SamplingPattern pattern;

    /**
     * The distance from the origin point to sample points
     */
    public final double distance;

    /**
     * Random number generator used for sampling distributions
     */
    public final Random random;

    /**
     * Constructs a sampling configuration with specified parameters.
     *
     * @param mode     The sampling mode determining sample density
     * @param shape    The shape of the sampling area
     * @param pattern  The distribution pattern of samples
     * @param distance The distance from origin to sample points (defaults to 1.0 if 0)
     */
    public SamplingConfiguration(SamplingMode mode, TargetAreaType shape, SamplingPattern pattern, double distance) {
        this.mode = mode;
        this.shape = shape;
        this.pattern = pattern;
        this.random = new Random();
        this.distance = distance == 0 ? 1.0 : distance;
    }
}
