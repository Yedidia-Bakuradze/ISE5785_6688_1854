package sampling;

import geometries.Intersectable;
import primitives.Point;
import primitives.Ray;

import java.util.List;
import java.util.Random;

public abstract class TargetAreaBase {

    protected final CastSamplingMode mode;
    protected final Random random = new Random();

    protected TargetAreaBase(CastSamplingMode mode) {
        this.mode = mode;
    }

    /**
     * Generates secondary rays based on the intersection point and the target area shape.
     *
     * @param intersection contains hit point, incoming ray direction, normal, and material info
     * @param shape        disk shape: CIRCLE or SQUARE
     * @param pattern      sampling pattern: JITTERED or GRID
     * @return list of secondary rays to be cast from the intersection point
     */
    public List<Ray> generateRays(Intersectable.Intersection intersection, TargetAreaType shape, SamplingPattern pattern) {
        List<Point> listOfPoints = generateSamplePoints(intersection, shape, pattern);
        return listOfPoints.stream()
                .map(point -> new Ray(intersection.point, point.subtract(intersection.point).normalize()))
                .toList();
    }

    /**
     * Generates target points on a disk or square around the refracted direction.
     *
     * @param intersection contains hit point, incoming ray direction, normal, and material info
     * @param shape        disk shape: CIRCLE or SQUARE
     * @param pattern      sampling pattern: JITTERED or GRID
     * @return list of 3D points where secondary rays will be cast
     */
    protected abstract List<Point> generateSamplePoints(Intersectable.Intersection intersection, TargetAreaType shape, SamplingPattern pattern);
}
