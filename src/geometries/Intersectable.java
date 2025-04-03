package geometries;

import primitives.Point;
import java.util.List;

public interface Intersectable {
    public abstract List<Point> findIntersections();
}
