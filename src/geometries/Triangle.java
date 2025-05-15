package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static primitives.Util.alignZero;

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

    @Override
    public List<Point> findIntersections(Ray ray) {
        // Check if there are any intersections with the triangle's plane
        List<Point> intersections = super.findIntersections(ray);
        if (intersections == null) return null;

        Point p0 = ray.getHead();
        Vector v = ray.getDirection();

        // Check if the intersection point is inside the triangle
        Vector v1 = this.vertices.get(0).subtract(p0);
        Vector v2 = this.vertices.get(1).subtract(p0);
        // Constraint: If any of them is a zero vector - there is no intersection
        Vector n1 = v1.crossProduct(v2).normalize();
        double res1 = alignZero(v.dotProduct(n1));
        // Check if the intersection point is inside the triangle
        if (res1 == 0) return null;

        Vector v3 = this.vertices.get(2).subtract(p0);
        Vector n2 = v2.crossProduct(v3).normalize();
        double res2 = alignZero(v.dotProduct(n2));
        if (res1 * res2 <= 0) return null;

        Vector n3 = v3.crossProduct(v1).normalize();
        double res3 = v.dotProduct(n3);
        if (res1 * res3 <= 0) return null;

        return intersections;
    }

    @Override
    public List<Intersection> calculateIntersectionsHelper(Ray ray) {
        List<Point> intersections = this.findIntersections(ray);
        if (intersections == null) return null;
        return intersections.stream()
                .map(point -> new Intersection(this, point))
                .toList();
    }
}
