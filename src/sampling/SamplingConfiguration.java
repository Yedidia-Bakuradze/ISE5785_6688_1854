package sampling;

import java.util.Random;

public class SamplingConfiguration {
    public final SamplingMode mode;
    public final TargetAreaType shape;
    public final SamplingPattern pattern;
    public final double radius;
    public final Random random;

    public SamplingConfiguration(SamplingMode mode, TargetAreaType shape, SamplingPattern pattern, double radius) {
        this.mode = mode;
        this.shape = shape;
        this.pattern = pattern;
        this.radius = radius;
        this.random = new Random();
    }
}
