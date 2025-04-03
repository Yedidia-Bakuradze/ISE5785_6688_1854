package geometries;

import primitives.Point;
import primitives.Ray;
import java.util.List;

/**
 * Interface for geometric objects that can be intersected by a ray.
 * Defines methods for finding intersection points between the object and a given ray.
 */
public interface Intersectable {
    /**
     * Calculates intersection points between a ray and the geometric object.
     * 
     * @param ray The ray to check for intersections
     * @return A list of intersection points, or null if no intersections are found
     */
    public abstract List<Point> findIntersections(Ray ray);
}
