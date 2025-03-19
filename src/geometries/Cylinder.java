package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

/**
 * Represents a cylinder in 3D space, which is a tube with a finite height.
 * Extends the {@link Tube} class.
 */
public class Cylinder extends Tube {
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

    @Override
    public Vector getNormal(Point point) {
        return null;
    }
}
