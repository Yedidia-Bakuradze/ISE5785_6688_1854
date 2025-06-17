package sampling;

public enum CastSamplingMode {
    EASY(2),
    DEMO(9),
    PRODUCTION(33);

    public final int gridValue;
    public final int numberSamples;

    CastSamplingMode(int value) {
        this.gridValue = value;
        this.numberSamples = value * value;
    }
}
