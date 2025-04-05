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

    //TODO: Refactoring is needed ASAP (Issue #11)

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

        // If the point is on the origin point
        if (point.equals(this.axis.getHead()))
            return this.axis.getDirection().normalize();

        double t = this.axis
                .getDirection()
                .dotProduct(point.subtract(this.axis.getHead()));

        // If the point is on a base

        // Case 1: If the point is on the bottom base
        if (t == 0) {
            double distance = point.distance(this.axis.getHead());

            // If the point is on the Cylinder body
            if (distance == this.radius)
                return point.subtract(this.axis.getHead()).normalize();

            // If the point is on the bottom base
            if (distance < this.radius)
                return this.axis.getDirection().normalize();

            // If the point is outside the Cylinder
            throw new IllegalArgumentException("The point is outside the Cylinder");
        }

        // Case 2: If the point is on the top base
        if (t == this.height) {
            Point o = this.axis.getPoint(t);
            double distance = point.distance(o);

            // If the point is on the Cylinder body
            if (distance == this.radius)
                return point.subtract(o).normalize();

            // If the point is on the top base
            if (distance < this.radius)
                return this.axis.getDirection().normalize();

            // If the point is outside the Cylinder
            throw new IllegalArgumentException("The point is outside the Cylinder");
        }

        // If the point is on the lateral surface
        Point o = this.axis.getPoint(t);

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
