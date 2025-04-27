package geometries;

import primitives.Point;
import primitives.Ray;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Represents a collection of geometries that can be intersected by rays.
 * Implements the {@link Intersectable} interface.
 */
public class Geometries implements Intersectable {

    /**
     * A list of intersectable geometries.
     */
    private final List<Intersectable> geometries = new LinkedList<Intersectable>();

    /**
     * Default constructor for Geometries.
     * Made for empty collection of geometries.
     */
    Geometries() {
    }

    /**
     * Constructor for Geometries that accepts many geometries objects.
     *
     * @param intersectable Objects of intersectable geometries.
     */
    public Geometries(Intersectable... intersectable) {
        this.add(intersectable);
    }

    /**
     * Adds intersectable geometries into the collection.
     *
     * @param intersectable Objects of intersectable geometries.
     */
    public void add(Intersectable... intersectable) {
        Collections.addAll(geometries, intersectable);
    }

    /**
     * Calculates the intersections of a ray with all geometries in the collection.
     *
     * @param ray The cast ray.
     * @return A list of intersection points (LinkedList instance), or null value if there are no intersections.
     */
    @Override
    public List<Point> findIntersections(Ray ray) {
        List<Point> intersections = null;
        for (Intersectable i : geometries) {
            List<Point> res = i.findIntersections(ray);
            if (res != null) {
                if (intersections == null)
                    intersections = new LinkedList<>(res);
                else
                    intersections.addAll(res);
            }
        }
        return intersections;
    }
}
