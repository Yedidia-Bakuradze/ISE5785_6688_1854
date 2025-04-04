package geometries;

import primitives.Point;
import primitives.Ray;
import java.util.List;

/**
 * Interface for geometries that can be intersected by rays.
 */
public interface Intersectable {

    /**
     * Finds the ray intersections with the geometry.
     * @param ray The cast ray.
     * @return A list of intersection points (LinkedList instance), or null value if there are no intersections.
     */
    List<Point> findIntersections(Ray ray);
}
