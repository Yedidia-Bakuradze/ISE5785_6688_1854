package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

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
     * The radius squared of the sphere.
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
        this.radiusSquared = radius * radius;
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
        Vector u = this.center.subtract(ray.getHead());
        double tm = ray.getDirection().dotProduct(u);
        double d = Math.sqrt(u.lengthSquared() - tm * tm);

        if(d >= radius) return null;

        double th = Math.sqrt(radiusSquared - d * d);
        double t1 = tm - th;
        double t2 = tm + th;

        return List.of(ray.getHead().add(ray.getDirection().scale(t1)), ray.getHead().add(ray.getDirection().scale(t2)));
    }
}
