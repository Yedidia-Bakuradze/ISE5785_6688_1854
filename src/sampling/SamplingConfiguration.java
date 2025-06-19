package sampling;

import java.util.Random;

public class SamplingConfiguration {
    public final SamplingMode mode;
    public final TargetAreaType shape;
    public final SamplingPattern pattern;
    public final double distance;
    public final Random random;

    public SamplingConfiguration(SamplingMode mode, TargetAreaType shape, SamplingPattern pattern, double distance) {
        this.mode = mode;
        this.shape = shape;
        this.pattern = pattern;
        this.random = new Random();
        this.distance = distance == 0 ? 1.0 : distance;
    }
}
