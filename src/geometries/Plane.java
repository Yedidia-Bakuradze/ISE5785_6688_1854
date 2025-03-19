package geometries;

import primitives.Point;
import primitives.Vector;

/**
 * Represents a plane in 3D space defined by a point and a normal vector.
 * Extends the {@link Geometry} class.
 */
public class Plane extends Geometry {
    private final Vector normal;
    private final Point q;

    /**
     * Constructs a plane using three points in 3D space.
     * 
     * @param p1 The first point.
     * @param p2 The second point.
     * @param p3 The third point.
     */
    public Plane(Point p1, Point p2, Point p3) {
        this.normal = null;
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

    @Override
    public Vector getNormal(Point point) {
        return normal;
    }
}
