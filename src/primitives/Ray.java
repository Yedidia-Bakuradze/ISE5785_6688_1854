package primitives;

/**
 * Represents a ray in 3D space, defined by a starting point and a direction vector.
 */
public class Ray {
    private final Point head;
    private final Vector direction;

    public Ray(Point head, Vector direction) {
        this.head = head;
        this.direction = direction.normalize();
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;
        return (obj instanceof Ray ray) &&
                this.head.equals(ray.head) &&
                this.direction.equals(ray.direction);
    }

    @Override
    public String toString() {
        return "Ray: " + super.toString();
    }
}
