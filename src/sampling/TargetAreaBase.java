package sampling;

import geometries.Intersectable;
import primitives.Point;
import primitives.Ray;

import java.util.List;

/**
 * Abstract base class for target area implementations used in ray sampling techniques.
 * Provides common fields and methods for generating sample points in different patterns and shapes.
 */
public abstract class TargetAreaBase {

    protected final SamplingConfiguration config;

    /**
     * Constructs a target area with the specified sampling parameters
     *
     * @param config The sampling configuration containing mode, shape, and pattern settings
     */
    protected TargetAreaBase(SamplingConfiguration config) {
        this.config = config;
    }

    /**
     * Generates target points on a disk or square around the refracted direction.
     *
     * @param intersection contains hit point, incoming ray direction, normal, and material info
     * @return list of 3D points where secondary rays will be cast
     */
    protected abstract List<Point> getSamplePoints(Intersectable.Intersection intersection);

    public abstract List<Ray> generateRays(Intersectable.Intersection intersection);
}
