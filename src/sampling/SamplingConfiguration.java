package sampling;

import java.util.Random;

public class SamplingConfiguration {
    private final SamplingMode mode;
    private final TargetAreaType shape;
    private final SamplingPattern pattern;
    private final double radius;
    private final Random random;

    public SamplingConfiguration(SamplingMode mode, TargetAreaType shape, SamplingPattern pattern, double radius) {
        this.mode = mode;
        this.shape = shape;
        this.pattern = pattern;
        this.radius = radius;
        this.random = new Random();
    }
}
