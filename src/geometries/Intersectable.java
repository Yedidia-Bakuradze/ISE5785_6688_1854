package geometries;

import primitives.Point;
import primitives.Ray;

import java.util.List;

/**
 * Interface for geometries that can be intersected by rays.
 */
public abstract class Intersectable {

    public static class g {
        public final Geometry geometry;
        public final Point point;

        Intersection(Geometry geometry, Point point) {
            this.geometry = geometry;
            this.point = point;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            return obj instanceof Intersection
                    && geometry.equals(((Intersection) obj).geometry)
                    && point.equals(((Intersection) obj).point);
        }

        @Override
        public String toString() {
            return "Intersection{" + "geometry=" + geometry + ", point=" + point + '}';
        }
    }

    /**
     * Finds the ray intersections with the geometry.
     *
     * @param ray The cast ray.
     * @return A list of intersection points (LinkedList instance), or null value if there are no intersections.
     */
    public abstract List<Point> findIntersections(Ray ray);


}
