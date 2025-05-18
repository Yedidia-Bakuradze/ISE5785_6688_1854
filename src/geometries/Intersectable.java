package geometries;

import lighting.LightSource;
import primitives.Material;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

/**
 * Interface for geometries that can be intersected by rays.
 */
public abstract class Intersectable {

    public static class Intersection {
        public final Geometry geometry;
        public final Point point;
        public final Material material;
        public Vector normal;
        public Vector rayDirection;
        public double rayNormalProduct;
        public LightSource lightSource;
        public Vector lightDirection;
        public double lightNormalProduct;


        public Intersection(Geometry geometry, Point point, Material material) {
            this.geometry = geometry;
            this.point = point;
            this.material = geometry == null ? null : material;
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
    public final List<Point> findIntersections(Ray ray) {
        var list = calculateIntersections(ray);
        return list == null ? null : list.stream().map(intersection -> intersection.point).toList();
    }


    protected abstract List<Intersection> calculateIntersectionsHelper(Ray ray);

    public final List<Intersection> calculateIntersections(Ray ray) {
        return calculateIntersectionsHelper(ray);
    }
}
