package sampling;

import primitives.Point;
import primitives.Vector;

import java.util.List;

public class DiffusiveTargetArea extends TargetAreaBase {

    public DiffusiveTargetArea(CastSamplingMode mode) {
        super(mode);
    }

    @Override
    public List<Point> generateCircularSpreadPoints(Point hitPoint, Vector direction, TargetAreaType shape, SamplingPattern pattern, int numSamples) {
        return List.of();
    }

    @Override
    public List<Point> generateSquaredSpreadPoints(Point hitPoint, Vector direction, TargetAreaType shape, SamplingPattern pattern, int numSamples) {
        return List.of();
    }

}
