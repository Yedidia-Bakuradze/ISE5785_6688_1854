package sampling;

/**
 * Enumeration representing different sampling modes for anti-aliasing and other effects.
 * Each mode defines how many samples to take per pixel or area.
 */
public enum SamplingMode {
    /**
     * Basic sampling with 2x2 grid (4 samples)
     */
    EASY(2),

    /**
     * Medium quality sampling with 9x9 grid (81 samples)
     */
    DEMO(9),

    /**
     * High quality sampling with 33x33 grid (1089 samples)
     */
    PRODUCTION(33);

    /**
     * The size of the sampling grid in one dimension
     */
    public final int gridValue;

    /**
     * The total number of samples (gridValue squared)
     */
    public final int numberSamples;

    /**
     * Constructs a sampling mode with the specified grid size
     *
     * @param value The size of one dimension of the sampling grid
     */
    SamplingMode(int value) {
        this.gridValue = value;
        this.numberSamples = value * value;
    }
}
