package geometries;

import primitives.Point;
import primitives.Vector;

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
}
