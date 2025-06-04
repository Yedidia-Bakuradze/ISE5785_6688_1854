package primitives;

import geometries.Intersectable.Intersection;

import java.util.List;

import static primitives.Util.isZero;

/**
 * Represents a ray in 3D space, defined by a starting point and a direction vector.
 */
public class Ray {
    private static final double DELTA = 0.1;

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
     *
     * @param head      the starting point of the ray
     * @param direction the direction vector of the ray
     */
    public Ray(Point head, Vector direction) {
        this.head = head;
        this.direction = direction.normalize();
    }

    /**
     * Constructs a Ray with a given starting point and direction vector, offset by a small delta.
     *
     * @param head      the starting point of the ray
     * @param direction the direction vector of the ray
     * @param normal    the normal vector to the surface at the starting point
     */
    public Ray(Point head, Vector direction, Vector normal) {
        double factor = normal.dotProduct(direction);
        this.head = isZero(factor) ? head : head.add(normal.scale(factor < 0 ? DELTA : -DELTA));
        this.direction = direction.normalize();
    }

    /**
     * Checks if this ray is equal to another object.
     *
     * @param obj the object to compare with
     * @return true if the object is a Ray with the same head and direction, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        return (obj instanceof Ray ray) &&
                this.head.equals(ray.head) &&
                this.direction.equals(ray.direction);
    }

    /**
     * Returns the starting point of the ray.
     *
     * @return the starting point of the ray
     */
    public Point getHead() {
        return head;
    }

    /**
     * Returns the vector of the ray.
     *
     * @return the vector of the ray
     */
    public Vector getDirection() {
        return direction;
    }

    @Override
    public String toString() {
        return head + "" + direction;
    }

    /**
     * Returns a point on the ray at a distance t from the starting point.
     *
     * @param t the distance from the starting point
     * @return the point on the ray at distance t
     */
    public Point getPoint(double t) {
        try {
            return this.head.add(direction.scale(t));
        } catch (IllegalArgumentException ignore) {
            return this.head;
        }
    }

    /**
     * Calculates and returns the closest point from the provided list of points.
     *
     * @param points List of points
     * @return the closest point
     */
    public Point findClosestPoint(List<Point> points) {
        return points == null ? null
                : findClosestIntersection(points.stream().map(p -> new Intersection(null, p)).toList()).point;
    }

    /**
     * Calculates and returns the closest intersection from the provided list of intersections.
     *
     * @param listOfIntersections List of points
     * @return the closest point
     */
    public Intersection findClosestIntersection(List<Intersection> listOfIntersections) {
        if (listOfIntersections == null) return null;
        double minSquaredDistance = Double.POSITIVE_INFINITY;
        Intersection closestIntersection = null;
        for (Intersection intersection : listOfIntersections) {
            double distanceSquared = head.distanceSquared(intersection.point);
            if (distanceSquared < minSquaredDistance) {
                minSquaredDistance = distanceSquared;
                closestIntersection = intersection;
            }
        }
        return closestIntersection;
    }

}
