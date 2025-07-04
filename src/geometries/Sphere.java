package geometries;

import primitives.*;

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
     * Constructor to create a Sphere with a center point and radius.
     *
     * @param center The center point of the sphere.
     * @param radius The radius of the sphere.
     */
    public Sphere(Point center, double radius) {
        super(radius);
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
    public List<Intersection> calculateIntersectionsHelper(Ray ray, double maxDistance) {
        Point p0 = ray.getHead();

        if (this.center.equals(p0)) return List.of(new Intersection(this, ray.getPoint(radius)));

        Vector u = this.center.subtract(p0);
        double tm = alignZero(ray.getDirection().dotProduct(u));
        double dSquared = alignZero(u.lengthSquared() - tm * tm);
        double thSquared = alignZero(this.radiusSquared - dSquared);
        if (thSquared <= 0) return null;
        double th = Math.sqrt(thSquared);

        double t2 = alignZero(tm + th);
        double t1 = alignZero(tm - th);
        if (t2 <= 0 || alignZero(t1 - maxDistance) > 0) return null;

        if (alignZero(t2 - maxDistance) > 0)
            return t1 <= 0 ? null : List.of(new Intersection(this, ray.getPoint(t1)));
        else
            return t1 <= 0 ? List.of(new Intersection(this, ray.getPoint(t2)))
                    : List.of(new Intersection(this, ray.getPoint(t1)), new Intersection(this, ray.getPoint(t2)));
    }

    @Override
    public BoundingBox getBoundingBox() {
        double minX = center.getX() - radius;
        double minY = center.getY() - radius;
        double minZ = center.getZ() - radius;

        double maxX = center.getX() + radius;
        double maxY = center.getY() + radius;
        double maxZ = center.getZ() + radius;

        return new BoundingBox(
                new Point(minX, minY, minZ),
                new Point(maxX, maxY, maxZ)
        );
    }
}
