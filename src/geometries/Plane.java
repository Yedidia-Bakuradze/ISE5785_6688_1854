package geometries;

import primitives.*;

import java.util.List;

import static primitives.Util.*;

/**
 * Represents a plane in 3D space defined by a point and a normal vector.
 * Extends the {@link Geometry} class.
 */
public class Plane extends Geometry {
    /**
     * The normal vector to the plane.
     */
    private final Vector normal;

    /**
     * A point on the plane.
     */
    private final Point q;

    /**
     * Constructs a plane using three points in 3D space.
     *
     * @param p1 The first point.
     * @param p2 The second point.
     * @param p3 The third point.
     * @throws IllegalArgumentException when the points are collinear.
     */
    public Plane(Point p1, Point p2, Point p3) {
        Vector v1 = p2.subtract(p1);
        Vector v2 = p3.subtract(p1);
        this.normal = v1.crossProduct(v2).normalize();
        this.q = p1;
    }

    /**
     * Constructs a plane using a normal vector and a point.
     *
     * @param normal The normal vector to the plane.
     * @param q      A point on the plane.
     */
    public Plane(Vector normal, Point q) {
        this.normal = normal.normalize();
        this.q = q;
    }

    /**
     * Calculates the normal vector to the plane at a given point.
     *
     * @param point The point on the plane.
     * @return The normal vector at the given point.
     */
    @Override
    public Vector getNormal(Point point) {
        return normal;
    }

    @Override
    public List<Intersection> calculateIntersectionsHelper(Ray ray, double maxDistance) {
        Vector u;
        try {
            u = this.q.subtract(ray.getHead());
        } catch (IllegalArgumentException ignore) {
            return null;
        }

        double up = this.normal.dotProduct(u);
        // If the ray is parallel to the plane, return null
        double down = this.normal.dotProduct(ray.getDirection());
        if (isZero(down)) return null;

        double t = alignZero(up / down);
        return t <= 0 || alignZero(maxDistance - t) < 0 ? null : List.of(new Intersection(this, ray.getPoint(t)));
    }
}
