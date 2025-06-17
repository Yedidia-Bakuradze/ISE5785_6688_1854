package sampling;

import geometries.Intersectable;
import primitives.Point;
import primitives.Ray;

import java.util.List;

public abstract class TargetAreaBase {
    TargetAreaType shapeOfArea;
    SamplingPattern pattern;
    CastSamplingMode mode;

    public abstract List<Ray> generateRays(Intersectable.Intersection intersection);

    protected abstract List<Point> generateSamplingPoints(Intersectable.Intersection intersection);
}
