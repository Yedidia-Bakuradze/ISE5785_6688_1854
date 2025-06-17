package sampling;

import geometries.Intersectable;
import primitives.Point;

import java.util.List;
import java.util.Random;

public abstract class TargetAreaBase {

    protected final SamplingMode mode;
    protected final TargetAreaType shape;
    protected final SamplingPattern pattern;
    protected final Random random = new Random();

    protected TargetAreaBase(SamplingMode mode, TargetAreaType shape, SamplingPattern pattern) {
        this.mode = mode;
        this.shape = shape;
        this.pattern = pattern;
    }

    /**
     * Generates target points on a disk or square around the refracted direction.
     *
     * @param intersection contains hit point, incoming ray direction, normal, and material info
     * @return list of 3D points where secondary rays will be cast
     */
    public abstract List<Point> generateSamplePoints(Intersectable.Intersection intersection);
}
