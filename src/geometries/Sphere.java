package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static primitives.Util.alignZero;

/**
 * Represents a sphere in 3D space.
 * Extends RadialGeometry to include a center point and radius.
 */
public class Sphere extends RadialGeometry {
    /**
     * The center point of the sphere.
     */
    private final Point center;

    /**
     * The square of the radius of the sphere.
     * Made to prevent the need to save processing time on every intersection calculations.
     */
    private final double radiusSquared;

    /**
     * Constructor to create a Sphere with a center point and radius.
     *
     * @param center The center point of the sphere.
     * @param radius The radius of the sphere.
     */
    public Sphere(Point center, double radius) {
        super(radius);
        radiusSquared = radius * radius;
        this.center = center;
    }

    /**
     * Calculates the normal vector to the sphere at a given point.
     *
     * @param point The point on the sphere.
     * @return The normal vector at the given point.
     */
    @Override
    public Vector getNormal(Point point) {
        return point.subtract(center).normalize();
    }

    @Override
    public List<Point> findIntersections(Ray ray) {
        Point p0 = ray.getHead();

        if (this.center.equals(p0)) return List.of(ray.getPoint(radius));

        Vector u = this.center.subtract(p0);
        double tm = alignZero(ray.getDirection().dotProduct(u));
        double dSquared = alignZero(u.lengthSquared() - tm * tm);

        if (dSquared >= this.radiusSquared) return null;

        double th = Math.sqrt(this.radiusSquared - dSquared);
        double t1 = alignZero(tm + th);
        double t2 = alignZero(tm - th);

        if (t1 <= 0) return null;
        else return t2 <= 0 ? List.of(ray.getPoint(t1)) : List.of(ray.getPoint(t2), ray.getPoint(t1));
    }
}
