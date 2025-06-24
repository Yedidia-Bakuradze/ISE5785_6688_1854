package primitives;

/**
 * Represents a point in 3D space.
 * Provides methods for point arithmetic and distance calculations.
 */
public class Point {

    /**
     * The coordinates of the point.
     */
    protected final Double3 xyz;

    /**
     * A constant representing the origin point (0, 0, 0).
     */
    public static Point ZERO = new Point(Double3.ZERO);

    /**
     * Constructor to create a Point from x, y, z coordinates.
     *
     * @param x X-coordinate.
     * @param y Y-coordinate.
     * @param z Z-coordinate.
     */
    public Point(double x, double y, double z) {
        xyz = new Double3(x, y, z);
    }

    /**
     * Constructor to create a Point from a Double3 object.
     *
     * @param xyz A Double3 object representing the coordinates.
     */
    public Point(Double3 xyz) {
        this.xyz = xyz;
    }

    /**
     * Checks if this point is equal to another object.
     *
     * @param obj The object to compare with.
     * @return True if the object is a Point with the same coordinates, false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        return obj instanceof Point point && point.xyz.equals(xyz);
    }

    /**
     * Converts the Point to a string representation.
     *
     * @return A string representing the Point.
     */
    @Override
    public String toString() {
        return "" + xyz;
    }

    /**
     * Adds a vector to this point and returns a new Point.
     *
     * @param addVec The vector to add.
     * @return A new Point resulting from the addition.
     */
    public Point add(Vector addVec) {
        return new Point(this.xyz.add(addVec.xyz));
    }

    /**
     * Subtracts another point from this point and returns a Vector.
     *
     * @param rightPoint The point to subtract.
     * @return A Vector representing the difference.
     * @throws IllegalArgumentException if the points are equal
     */
    public Vector subtract(Point rightPoint) {
        return new Vector(this.xyz.subtract(rightPoint.xyz));
    }

    /**
     * Calculates the squared distance between this point and another point.
     *
     * @param rightPoint The other point.
     * @return The squared distance.
     */
    public double distanceSquared(Point rightPoint) {
        double dX = this.xyz.d1() - rightPoint.xyz.d1();
        double dY = this.xyz.d2() - rightPoint.xyz.d2();
        double dZ = this.xyz.d3() - rightPoint.xyz.d3();
        return dX * dX + dY * dY + dZ * dZ;
    }

    /**
     * Calculates the distance between this point and another point.
     *
     * @param rightPoint The other point.
     * @return The distance.
     */
    public double distance(Point rightPoint) {
        return Math.sqrt(distanceSquared(rightPoint));
    }

    public double getX() {
        return xyz.d1();
    }

    public double getY() {
        return xyz.d2();
    }

    public double getZ() {
        return xyz.d3();
    }
}
