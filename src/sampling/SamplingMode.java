package sampling;

public enum SamplingMode {
    EASY(2),
    DEMO(9),
    PRODUCTION(33);

    public final int gridValue;
    public final int numberSamples;

    SamplingMode(int value) {
        this.gridValue = value;
        this.numberSamples = value * value;
    }
}
