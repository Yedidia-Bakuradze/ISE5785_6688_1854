package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

/**
 * Represents a tube in 3D space, which is a cylinder with infinite height.
 * Extends the {@link RadialGeometry} class.
 */
public class Tube extends RadialGeometry {
    /**
     * The axis ray of the tube.
     */
    protected final Ray axis;

    /**
     * Constructs a tube with a given axis and radius.
     *
     * @param axis   The axis ray of the tube.
     * @param radius The radius of the tube.
     */
    public Tube(Ray axis, double radius) {
        super(radius);
        this.axis = axis;
    }

    /**
     * Calculates the normal vector to the tube at a given point.
     *
     * @param point The point on the tube.
     * @return The normal vector at the given point.
     */
    @Override
    public Vector getNormal(Point point) {
        double t = this.axis.getDirection().dotProduct(point.subtract(this.axis.getHead()));
        Point o = this.axis.getPoint(t);
        return point.subtract(o).normalize();
    }

    //TODO: This method is a bonus for this project. implement it later

    @Override
    public List<Intersection> calculateIntersectionsHelper(Ray ray) {
        return null;
    }
}
