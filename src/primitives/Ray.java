package primitives;

/**
 * Represents a ray in 3D space, defined by a starting point and a direction vector.
 */
public class Ray {
    /**
     * The starting point of the ray.
     */
    private final Point head;

    /**
     * The direction vector of the ray.
     */
    private final Vector direction;

    /**
     * Constructs a Ray with a given starting point and direction vector.
     * @param head the starting point of the ray
     * @param direction the direction vector of the ray
     */
    public Ray(Point head, Vector direction) {
        this.head = head;
        this.direction = direction.normalize();
    }

    /**
     * Checks if this ray is equal to another object.
     * @param obj the object to compare with
     * @return true if the object is a Ray with the same head and direction, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;
        return (obj instanceof Ray ray) &&
                this.head.equals(ray.head) &&
                this.direction.equals(ray.direction);
    }

    /**
     * Returns the starting point of the ray.
     * @return the starting point of the ray
     */
    public Point getHead() {
        return head;
    }

    /**
     * Returns the vector of the ray.
     * @return the vector of the ray
     */
    public Vector getDirection() {
        return direction;
    }

    @Override
    public String toString() {
        return "Ray: " + super.toString();
    }
}
