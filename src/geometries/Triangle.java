package geometries;

import primitives.*;

import java.util.List;

import static primitives.Util.*;

/**
 * Represents a triangle in 3D space.
 * A triangle is a special case of a polygon with three vertices.
 * Extends the {@link Polygon} class.
 */
public class Triangle extends Polygon {

    /**
     * Constructs a triangle with three points.
     *
     * @param point  The first point.
     * @param point1 The second point.
     * @param point2 The third point.
     */
    public Triangle(Point point, Point point1, Point point2) {
        super(point, point1, point2);
    }

    /**
     * Calculates the intersections of a ray with the triangle.
     *
     * @param ray         The ray to intersect with.
     * @param maxDistance The maximum distance for intersection.
     * @return A list of intersections, or null if none.
     */
    @Override
    protected List<Intersection> calculateIntersectionsHelper(Ray ray, double maxDistance) {
        // Get vertices of the triangle
        Point v0 = vertices.get(0);
        Point v1 = vertices.get(1);
        Point v2 = vertices.get(2);

        Vector direction = ray.getDirection();

        Vector edge1 = v1.subtract(v0);
        Vector edge2 = v2.subtract(v0);

        Vector pVec = direction.crossProduct(edge2);
        double det = edge1.dotProduct(pVec);
        if (isZero(det)) return null;

        double invDet = 1.0 / det;
        Vector tVec = ray.getHead().subtract(v0);

        double u = tVec.dotProduct(pVec) * invDet;
        if (alignZero(u) <= 0) return null;

        // Calculate v parameter
        Vector qVec = tVec.crossProduct(edge1);
        double v = direction.dotProduct(qVec) * invDet;

        // Check strict bounds (not on edges and not on hypotenuse)
        if (alignZero(v) <= 0 || alignZero(u + v - 1) >= 0) return null;

        // Calculate t (distance)
        double t = edge2.dotProduct(qVec) * invDet;
        // Only consider positive t values (in front of ray)
        return alignZero(t) <= 0 || alignZero(maxDistance - t) < 0 ? null
                : List.of(new Intersection(this, ray.getPoint(t)));
    }

    /**
     * Returns a string representation of the triangle.
     *
     * @return a string representation of the triangle.
     */
    @Override
    public String toString() {
        return "Triangle{" + "vertices=" + vertices + " }";
    }

    @Override
    public BoundingBox getBoundingBox() {
        Point v0 = vertices.get(0);
        Point v1 = vertices.get(1);
        Point v2 = vertices.get(2);

        double minX = Math.min(Math.min(v0.getX(), v1.getX()), v2.getX());
        double minY = Math.min(Math.min(v0.getY(), v1.getY()), v2.getY());
        double minZ = Math.min(Math.min(v0.getZ(), v1.getZ()), v2.getZ());

        double maxX = Math.max(Math.max(v0.getX(), v1.getX()), v2.getX());
        double maxY = Math.max(Math.max(v0.getY(), v1.getY()), v2.getY());
        double maxZ = Math.max(Math.max(v0.getZ(), v1.getZ()), v2.getZ());

        return new BoundingBox(
                new Point(minX, minY, minZ),
                new Point(maxX, maxY, maxZ)
        );
    }
}
