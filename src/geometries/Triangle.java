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
    public List<Intersection> calculateIntersectionsHelper(Ray ray) {
        // Get vertices of the triangle
        Point v0 = vertices.get(0);
        Point v1 = vertices.get(1);
        Point v2 = vertices.get(2);

        // Ray components
        Point origin = ray.getHead();
        Vector direction = ray.getDirection();

        // Calculate edges
        Vector edge1 = v1.subtract(v0);
        Vector edge2 = v2.subtract(v0);

        // Begin Möller-Trumbore algorithm
        Vector pvec = direction.crossProduct(edge2);
        double det = edge1.dotProduct(pvec);

        // Check if ray is parallel to the triangle
        if (alignZero(det) == 0)
            return null;

        double invDet = 1.0 / det;
        Vector tvec = origin.subtract(v0);

        // Calculate u parameter
        double u = tvec.dotProduct(pvec) * invDet;

        // Check strict bounds (not on edges)
        if (alignZero(u) <= 0 || alignZero(u - 1) >= 0)
            return null;

        // Calculate v parameter
        Vector qvec = tvec.crossProduct(edge1);
        double v = direction.dotProduct(qvec) * invDet;

        // Check strict bounds (not on edges and not on hypotenuse)
        if (alignZero(v) <= 0 || alignZero(u + v - 1) >= 0)
            return null;

        // Calculate t (distance)
        double t = edge2.dotProduct(qvec) * invDet;

        // Only consider positive t values (in front of ray)
        if (alignZero(t) <= 0)
            return null;

        // Calculate the intersection point
        Point intersectionPoint = ray.getPoint(t);
        return List.of(new Intersection(this, intersectionPoint));
    }
}
