package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

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
        //TODO: Dont throw exceptions of outside the cylinder
        //TODO: Check if the point is on the top or bottom of the cylinder and throw the proper normal (Up or Down)
        return null;
    }
}
