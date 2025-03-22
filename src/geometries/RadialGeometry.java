package geometries;

/**
 * Abstract class representing a radial geometry, such as a sphere or a cylinder.
 * Extends the {@link Geometry} class.
 */
abstract public class RadialGeometry extends Geometry {
    /**
     * The radius of the radial geometry.
     */
    protected final double radius;

    /**
     * Constructs a radial geometry with a given radius.
     * 
     * @param radius The radius of the geometry.
     */
    protected RadialGeometry(double radius) {
        if(radius <= 0) throw new IllegalArgumentException("radius must be greater than zero");
        this.radius = radius;
    }
}
