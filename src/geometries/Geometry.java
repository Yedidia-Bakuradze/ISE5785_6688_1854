package geometries;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

/**
 * Abstract class representing a geometric shape in 3D space.
 * Provides a method to calculate the normal vector at a given point.
 */
abstract public class Geometry extends Intersectable {

    /**
     * Default constructor - only to dismiss errors in JavaDoc generator.
     */
    protected Geometry() {
    }

    protected Color emission = Color.BLACK;


    /**
     * Abstract method to calculate the normal vector to the geometry at a given point.
     *
     * @param point The point on the geometry.
     * @return The normal vector at the given point.
     */
    abstract public Vector getNormal(Point point);


    public Color getEmission() {
        return emission;
    }

    public Geometry setEmission(Color emission) {
        this.emission = emission;
        return this;
    }


}
