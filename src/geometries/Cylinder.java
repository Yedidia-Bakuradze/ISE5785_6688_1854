package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

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

    //TODO: Refactoring is needed ASAP (Issue #11)

    /**
     * Calculates the normal vector to the cylinder at a given point.
     *
     * @param point The point on the cylinder.
     * @return The normal vector at the given point.
     */
    @Override
    public Vector getNormal(Point point) {
        Vector v = this.axis.getDirection();
        Vector u;
        try {
            u = point.subtract(this.axis.getHead());
        } catch (IllegalArgumentException ignore) {
            return v.scale(-1);
        }

        double t = v.dotProduct(u);
        if (isZero(t)) return v.scale(-1);
        if (isZero(t - height)) return v;

        return point.subtract(this.axis.getPoint(t)).normalize();
    }

    /**
     * Finds the intersections of a ray with the cylinder.
     *
     * @param ray The ray to check for intersections.
     * @return A list of intersection points, or null if there are no intersections.
     */
    @Override
    public List<Intersection> calculateIntersectionsHelper(Ray ray) {
        return null;
    }
}
