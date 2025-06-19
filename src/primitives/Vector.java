package primitives;

import java.util.List;

import static primitives.Util.isZero;

/**
 * Represents a vector in 3D space.
 * Provides methods for vector arithmetic and operations such as dot product and cross product.
 */
public class Vector extends Point {

    // Static constants representing the three axes in 3D space

    /**
     * The X-axis unit vector
     */
    public final static Vector AXIS_X = new Vector(1, 0, 0);

    /**
     * The Y-axis unit vector
     */
    public final static Vector AXIS_Y = new Vector(0, 1, 0);

    /**
     * The Z-axis unit vector
     */
    public final static Vector AXIS_Z = new Vector(0, 0, 1);

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
        return Math.sqrt(lengthSquared());
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

    /**
     * Calculate refracted ray using Snell's law
     *
     * @param normal Surface normal at intersection point (should be normalized)
     * @param n1     Refractive index of medium ray is coming from
     * @param n2     Refractive index of medium ray is entering
     * @return Refracted ray direction, or null if total internal reflection occurs
     */
    public Vector calcSnellRefraction(Vector normal, double n1, double n2) {
        // Calculate cosine of incident angle
        double cosTheta1 = -this.dotProduct(normal);

        // Handle ray coming from the opposite side of the surface
        if (cosTheta1 < 0) {
            cosTheta1 = -cosTheta1;
            normal = normal.scale(-1);
        }

        // Calculate relative refractive index
        double eta = n1 / n2;

        if (isZero(eta - 1)) return this;

        // Calculate discriminant to check for total internal reflection
        double discriminant = 1.0 - eta * eta * (1.0 - cosTheta1 * cosTheta1);

        // Check for total internal reflection
        if (discriminant < 0) return null;

        // Calculate cosine of refracted angle
        double cosTheta2 = Math.sqrt(discriminant);

        // Calculate refracted ray direction using Snell's law vector form
        return this.scale(eta).add(normal.scale(eta * cosTheta1 - cosTheta2)).normalize();
    }

    /**
     * Creates a new orthogonal coordinate system based on a main direction vector.
     * This is useful for distributing samples around a given direction vector.
     *
     * @return A list containing two vectors that, along with main, form an orthogonal coordinate system
     */
    public List<Vector> getNewCoordinateSystems() {
        Vector w = getOrthogonalNormalized();
        return List.of(w, crossProduct(w).normalize());
    }

    public Vector getOrthogonalNormalized() {
        try {
            return new Vector(-xyz.d2(), xyz.d1(), 0).normalize();
        } catch (IllegalArgumentException ignored) {
            return new Vector(0, -xyz.d3(), xyz.d2()).normalize();
        }
    }
}
