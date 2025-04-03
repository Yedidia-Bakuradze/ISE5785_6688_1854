package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;

/**
 * Represents a cylinder in 3D space, which is a tube with a finite height.
 * Extends the {@link Tube} class.
 */
public class Cylinder extends Tube {
    /**
     * The height of the cylinder.
     */
    private final double height;

    /**
     * Constructs a cylinder with a given axis, radius, and height.
     *
     * @param axis   The axis ray of the cylinder.
     * @param radius The radius of the cylinder.
     * @param height The height of the cylinder.
     */
    Cylinder(Ray axis, double radius, double height) {
        super(axis, radius);
        this.height = height;
    }

    /**
     * Calculates the normal vector to the cylinder at a given point.
     *
     * @param point The point on the cylinder.
     * @return The normal vector at the given point.
     */
    @Override
    public Vector getNormal(Point point) {
        Point p0 = this.axis.getHead();
        Vector v = this.axis.getDirection();

        double t = point.equals(p0) ? 0 : alignZero(v.dotProduct(point.subtract(p0)));
        if (t == 0) return v.scale(-1);
        if (t == height) return v;

        return point.subtract(p0.add(v.scale(t))).normalize();
    }

    /**
     * Finds the intersections of a ray with the cylinder.
     *
     * @param ray The ray to check for intersections.
     * @return A list of intersection points, or null if there are no intersections.
     */
    @Override
    public List<Point> findIntersections(Ray ray) {
        return null;
    }
}
