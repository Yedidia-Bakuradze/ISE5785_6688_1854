package geometries;

import primitives.Point;
import primitives.Vector;

public class Plane extends Geometry {
    private final Vector normal;
    private final Point q;

    public Plane(Point p1, Point p2, Point p3) {
        this.normal = null;
        this.q = p1;
    }
    public Plane(Vector normal, Point q) {
        this.normal = normal.normalize();
        this.q = q;
    }

    @Override
    public Vector getNormal(Point point) {
        return normal;
    }
}
