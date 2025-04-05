package primitives;

/**
 * Represents a vector in 3D space.
 * Provides methods for vector arithmetic and operations such as dot product and cross product.
 */
public class Vector extends Point {

    /**
     * Constructs a Vector with given x, y, and z components.
     *
     * @param x the x component
     * @param y the y component
     * @param z the z component
     */
    public Vector(double x, double y, double z) {
        super(x, y, z);
        if (xyz.equals(Double3.ZERO))
            throw new IllegalArgumentException("Vector zero is not allowed");
    }

    /**
     * Constructs a Vector with a given Double3 object.
     *
     * @param xyz the Double3 object representing the vector components
     */
    public Vector(Double3 xyz) {
        super(xyz);
        if (xyz.equals(Double3.ZERO))
            throw new IllegalArgumentException("Vector zero is not allowed");
    }

    /**
     * Checks if this vector is equal to another object.
     *
     * @param obj the object to compare with
     * @return true if the object is a Vector with the same components, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        return obj instanceof Vector vec && (vec.xyz.equals(xyz));
    }

    /**
     * Returns a string representation of the vector.
     *
     * @return a string representation of the vector
     */
    @Override
    public String toString() {
        return "Vector: " + super.toString();
    }

    /**
     * Calculates the length of the vector.
     *
     * @return the length of the vector
     */
    public double length() {
        return (double) Math.sqrt(lengthSquared());
    }

    /**
     * Calculates the squared length of the vector.
     *
     * @return the squared length of the vector
     */
    public double lengthSquared() {
        return this.dotProduct(this);
    }

    /**
     * Adds another vector to this vector.
     *
     * @param rightVector the vector to add
     * @return a new vector that is the sum of this vector and the given vector
     */
    public Vector add(Vector rightVector) {
        return new Vector(xyz.add(rightVector.xyz));
    }

    /**
     * Scales this vector by a given factor.
     *
     * @param scaleFactor the factor to scale by
     * @return a new vector that is this vector scaled by the given factor
     */
    public Vector scale(double scaleFactor) {
        return new Vector(xyz.scale(scaleFactor));
    }

    /**
     * Calculates the dot product of this vector and another vector.
     *
     * @param rightVec the other vector
     * @return the dot product of the two vectors
     */
    public double dotProduct(Vector rightVec) {
        return this.xyz.d1() * rightVec.xyz.d1()
                + this.xyz.d2() * rightVec.xyz.d2()
                + this.xyz.d3() * rightVec.xyz.d3();
    }

    /**
     * Calculates the cross product of this vector and another vector.
     *
     * @param rightVec the other vector
     * @return a new vector that is the cross product of the two vectors
     * @throws IllegalArgumentException if the right vector is zero or if the vectors are parallel
     */
    public Vector crossProduct(Vector rightVec) {
        //Check if right vector is zero
        if (rightVec.equals(Point.ZERO))
            throw new IllegalArgumentException("Cross-Product of zero vectors is not allowed");

        //Check if vectors are parallel
        if (this.xyz.d1() / rightVec.xyz.d1() == this.xyz.d2() / rightVec.xyz.d2() && this.xyz.d2() / rightVec.xyz.d2() == this.xyz.d3() / rightVec.xyz.d3())
            throw new IllegalArgumentException("Cross-Product of parallel vectors is not allowed");

        return new Vector(
                this.xyz.d2() * rightVec.xyz.d3() - this.xyz.d3() * rightVec.xyz.d2(),  // x component
                this.xyz.d3() * rightVec.xyz.d1() - this.xyz.d1() * rightVec.xyz.d3(),  // y component (fixed)
                this.xyz.d1() * rightVec.xyz.d2() - this.xyz.d2() * rightVec.xyz.d1()   // z component
        );
    }

    /**
     * Normalizes this vector.
     *
     * @return a new vector that is the normalized version of this vector
     */
    public Vector normalize() {
        return new Vector(xyz.reduce(this.length()));
    }
}
