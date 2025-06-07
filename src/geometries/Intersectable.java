package geometries;

import lighting.LightSource;
import primitives.*;

import java.util.List;

/**
 * Abstract class representing objects that can be intersected by rays.
 */
public abstract class Intersectable {
    /**
     * Default constructor for Intersectable.
     */
    public Intersectable() {
    }

    /**
     * Represents an intersection point between a ray and a geometry.
     */
    public static class Intersection {
        /**
         * The geometry that was intersected.
         */
        public final Geometry geometry;

        /**
         * The intersection point.
         */
        public final Point point;

        /**
         * The material of the intersected geometry.
         */
        public final Material material;

        /**
         * The normal vector at the intersection point.
         */
        public Vector normal;

        /**
         * The direction of the ray that caused the intersection.
         */
        public Vector rayDirection;

        /**
         * The dot product of the ray direction and the normal vector.
         */
        public double rayNormalProduct;

        /**
         * The light source affecting the intersection.
         */
        public LightSource lightSource;

        /**
         * The direction of the light at the intersection point.
         */
        public Vector lightDirection;

        /**
         * The dot product of the light direction and the normal vector.
         */
        public double lightNormalProduct;

        /**
         * Constructs an Intersection object with the specified geometry and point.
         *
         * @param geometry The intersected geometry.
         * @param point    The intersection point.
         */
        public Intersection(Geometry geometry, Point point) {
            this.geometry = geometry;
            this.point = point;
            this.material = geometry == null ? null : geometry.getMaterial();
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
            return "Intersection{" + geometry + "," + point + '}';
        }
    }

    /**
     * Finds the ray intersections with the geometry.
     *
     * @param ray The cast ray.
     * @return A list of intersection points (LinkedList instance), or null value if there are no intersections.
     */
    public final List<Point> findIntersections(Ray ray) {
        var list = calculateIntersections(ray);
        return list == null ? null
                : list.stream().map(intersection -> intersection.point).toList();
    }

    /**
     * Calculates the intersections of a ray with the geometry.
     *
     * @param ray The ray to intersect with.
     * @return A list of intersections, or null if no intersections exist.
     */
    public final List<Intersection> calculateIntersections(Ray ray) {
        return calculateIntersections(ray, Double.POSITIVE_INFINITY);
    }

    /**
     * Calculates the intersections of a ray with the geometry up to a maximum distance.
     *
     * @param ray The ray to intersect with.
     * @param maxDistance The maximum distance for intersection.
     * @return A list of intersections, or null if no intersections exist.
     */
    public final List<Intersection> calculateIntersections(Ray ray, double maxDistance) {
        return calculateIntersectionsHelper(ray, maxDistance);
    }

    /**
     * Helper method to calculate intersections for specific geometries.
     *
     * @param ray The ray to intersect with.
     * @param maxDistance The maximum distance for intersection.
     * @return A list of intersections, or null if no intersections exist.
     */
    protected abstract List<Intersection> calculateIntersectionsHelper(Ray ray, double maxDistance);
}
