package geometries;

import primitives.Ray;

import java.util.*;

/**
 * Represents a collection of geometries that can be intersected by rays.
 * Implements the {@link Intersectable} interface.
 */
public class Geometries extends Intersectable {

    /**
     * A list of intersectable geometries.
     */
    private final List<Intersectable> geometries = new LinkedList<>();

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
    protected List<Intersection> calculateIntersectionsHelper(Ray ray, double maxDistance) {
        List<Intersection> intersections = null;

        for (Intersectable intersectable : geometries) {
            var res = intersectable.calculateIntersections(ray, maxDistance);
            if (res != null) {
                if (intersections == null)
                    intersections = new LinkedList<>(res);
                else
                    intersections.addAll(res);
            }
        }

        return intersections;
    }

    /**
     * Returns an unmodifiable list of all geometries in the collection.
     *
     * @return A list of intersectable geometries.
     */
    public List<Intersectable> getGeometries() {
        return Collections.unmodifiableList(geometries);
    }

    /**
     * Returns a list of bounding boxes for all geometries in the collection.
     *
     * @return A list of bounding boxes.
     */
    public List<BoundingBox> getBoundingBoxes() {
        List<BoundingBox> boundingBoxes = new ArrayList<>();
        for (Intersectable obj : geometries) {
            if (obj instanceof Geometry geometry && geometry.getBoundingBox() != null) {
                boundingBoxes.add(geometry.getBoundingBox());
            }
        }
        return boundingBoxes;
    }

    /**
     * Returns the bounding box that encompasses all geometries in the collection.
     *
     * @return The union bounding box of all geometries.
     */
    public BoundingBox getBoundingBox() {
        return BoundingBox.union(getBoundingBoxes());
    }

    /**
     * Returns a list of finite geometries (those with bounding boxes) in the collection.
     *
     * @return A list of finite geometries.
     */
    public List<Intersectable> getFiniteInjectables() {
        List<Intersectable> finiteGeometries = null;
        for (Intersectable obj : geometries) {
            if (obj instanceof Geometry geometry && geometry.getBoundingBox() != null) {
                if (finiteGeometries == null) {
                    finiteGeometries = new LinkedList<>();
                }
                finiteGeometries.add(geometry);
            }
        }
        return finiteGeometries;
    }

    /**
     * Returns a list of infinite geometries (those without bounding boxes) in the collection.
     *
     * @return A list of infinite geometries.
     */
    public List<Intersectable> getInfiniteInjectables() {
        List<Intersectable> infiniteGeometries = null;
        for (Intersectable obj : geometries) {
            if (obj instanceof Geometry geometry && geometry.getBoundingBox() == null) {
                if (infiniteGeometries == null) {
                    infiniteGeometries = new LinkedList<>();
                }
                infiniteGeometries.add(geometry);
            }
        }
        return infiniteGeometries;
    }
}
