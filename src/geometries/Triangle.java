package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

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
        super(point,point1,point2);
    }

    @Override
    public List<Point> findIntersections(Ray ray) {
        // Check if there are any intersections with the triangle's plane
        List<Point> intersections = super.findIntersections(ray);
        if (intersections == null) return null;

        // Check if the intersection point is inside the triangle
        Point p0 = ray.getHead();
        Vector v1 = this.vertices.get(0).subtract(p0);
        Vector v2 = this.vertices.get(1).subtract(p0);
        Vector v3 = this.vertices.get(2).subtract(p0);

        // Constraint: If any of them is a zero vector - there is no intersection
        Vector n1 = v1.crossProduct(v2).normalize();
        Vector n2 = v2.crossProduct(v3).normalize();
        Vector n3 = v3.crossProduct(v1).normalize();

        // Check if the intersection point is inside the triangle
        Vector v = ray.getDirection();

        double res1 = v.dotProduct(n1);
        double res2 = v.dotProduct(n2);
        double res3 = v.dotProduct(n3);

        boolean isSameSign =
                (res1 > 0 && res2 > 0 && res3 > 0) ||
                (res1 < 0 && res2 < 0 && res3 < 0);

        if (isSameSign) return intersections;
        return null;
    }
}
