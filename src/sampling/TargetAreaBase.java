package sampling;

import geometries.Intersectable;
import primitives.Point;
import primitives.Ray;

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
     * Generates secondary rays based on the intersection point and the target area shape.
     *
     * @param intersection contains hit point, incoming ray direction, normal, and material info
     * @return list of secondary rays to be cast from the intersection point
     */
    public List<Ray> generateRays(Intersectable.Intersection intersection) {
        List<Point> listOfPoints = generateSamplePoints(intersection);
        return listOfPoints.stream()
                .map(point -> new Ray(intersection.point, point.subtract(intersection.point).normalize()))
                .toList();
    }

    /**
     * Generates target points on a disk or square around the refracted direction.
     *
     * @param intersection contains hit point, incoming ray direction, normal, and material info
     * @return list of 3D points where secondary rays will be cast
     */
    protected abstract List<Point> generateSamplePoints(Intersectable.Intersection intersection);
}
