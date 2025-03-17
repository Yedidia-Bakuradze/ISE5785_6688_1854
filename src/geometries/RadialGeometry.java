package geometries;

abstract public class RadialGeometry extends Geometry{
    protected final double radius;

    protected RadialGeometry(double radius) {
        this.radius = radius;
    }
}
