package sampling;

import primitives.*;

import java.util.List;

public abstract class TargetAreaBase {

    protected final CastSamplingMode mode;

    protected TargetAreaBase(CastSamplingMode mode) {
        this.mode = mode;
    }

    public List<Ray> generateRays(Point hitPoint, Vector direction, TargetAreaType shape, SamplingPattern pattern, int numSamples) {
        List<Point> listOfPoints = switch (shape) {
            case TargetAreaType.CIRCLE -> generateCircularSpreadPoints(hitPoint, direction, shape, pattern, numSamples);
            case TargetAreaType.SQUARE -> generateSquaredSpreadPoints(hitPoint, direction, shape, pattern, numSamples);
            default -> throw new IllegalArgumentException("Unsupported TargetAreaType: " + shape);
        };
        return listOfPoints.stream()
                .map(point -> new Ray(hitPoint, point.subtract(hitPoint).normalize()))
                .toList();
    }

    public abstract List<Point> generateCircularSpreadPoints(Point hitPoint, Vector direction, TargetAreaType shape, SamplingPattern pattern, int numSamples);

    public abstract List<Point> generateSquaredSpreadPoints(Point hitPoint, Vector direction, TargetAreaType shape, SamplingPattern pattern, int numSamples);
}
