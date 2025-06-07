package geometries;

import primitives.*;

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

    /**
     * The emission color of the geometry.
     */
    protected Color emission = Color.BLACK;

    /**
     * The material properties of the geometry.
     */
    private Material material = new Material();

    /**
     * Abstract method to calculate the normal vector to the geometry at a given point.
     *
     * @param point The point on the geometry.
     * @return The normal vector at the given point.
     */
    abstract public Vector getNormal(Point point);

    /**
     * Gets the emission color of the geometry.
     *
     * @return The emission color.
     */
    public Color getEmission() {
        return emission;
    }

    /**
     * Sets the emission color of the geometry.
     *
     * @param emission The emission color to set.
     * @return The current Geometry instance.
     */
    public Geometry setEmission(Color emission) {
        this.emission = emission;
        return this;
    }

    /**
     * Gets the material properties of the geometry.
     *
     * @return The material properties.
     */
    public Material getMaterial() {
        return material;
    }

    /**
     * Sets the material properties of the geometry.
     *
     * @param material The material properties to set.
     * @return The current Geometry instance.
     */
    public Geometry setMaterial(Material material) {
        this.material = material;
        return this;
    }
}
