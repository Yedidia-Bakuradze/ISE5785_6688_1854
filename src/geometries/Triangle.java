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
        super(point, point1, point2);
    }


    /**
     * Finds the intersections of a ray with the polygon.
     *
     * @param ray The ray to check for intersections.
     * @return A list of intersection points, or null if there are no intersections.
     * */
    @Override
    public List<Point> findIntersections(Ray ray) {
        List<Point> intersections = super.findIntersections(ray);
        if(intersections == null) return null;

        Point p0 = ray.getHead();
        Vector dir = ray.getDirection();
        Vector v1 = vertices.get(0).subtract(p0);
        Vector v2 = vertices.get(1).subtract(p0);
        Vector v3 = vertices.get(2).subtract(p0);

        boolean isPos1 = v1.crossProduct(v2).dotProduct(dir) > 0;
        boolean isPos2 = v2.crossProduct(v1).dotProduct(dir) > 0;

        if (isPos1 == isPos2) return intersections;

        boolean isPos3 = v3.crossProduct(v2).dotProduct(dir) > 0;
        if (isPos2 == isPos3) return intersections;

        return null;
    }
}
