package sampling;

import geometries.Intersectable;
import primitives.Point;

import java.util.List;
import java.util.Random;

/**
 * Abstract base class for target area implementations used in ray sampling techniques.
 * Provides common fields and methods for generating sample points in different patterns and shapes.
 */
public abstract class TargetAreaBase {

    /**
     * The sampling mode that defines the number of samples to generate
     */
    protected final SamplingMode mode;

    /**
     * The shape of the target area (circle or square) for sample distribution
     */
    protected final TargetAreaType shape;

    /**
     * The pattern used for distributing samples (grid, random, or jittered)
     */
    protected final SamplingPattern pattern;

    /**
     * Random number generator for creating sample variations
     */
    protected final Random random = new Random();

    /**
     * Constructs a target area with the specified sampling parameters
     *
     * @param mode    The sampling mode that defines the number of samples
     * @param shape   The shape of the target area (circle or square)
     * @param pattern The pattern for distributing samples
     */
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
