package geometries;

import primitives.*;

import java.util.Collection;
import java.util.Objects;

/**
 * Represents an Axis-Aligned Bounding Box in 3D space.
 * Used for spatial acceleration structures like Regular Grid.
 *
 * @param min Minimum point of the bounding box
 * @param max Maximum point of the bounding box
 */
public record BoundingBox(Point min, Point max) {

    /**
     * Constructs an AABB with minimum and maximum points
     *
     * @param min The minimum point (lowest x, y, z coordinates)
     * @param max The maximum point (highest x, y, z coordinates)
     */
    public BoundingBox {
    }

    /**
     * Gets the minimum point of the bounding box
     *
     * @return The minimum point
     */
    @Override
    public Point min() {
        return min;
    }

    /**
     * Gets the maximum point of the bounding box
     *
     * @return The maximum point
     */
    @Override
    public Point max() {
        return max;
    }

    /**
     * Creates a union of multiple bounding boxes
     *
     * @param boxes Collection of bounding boxes to unite
     * @return A new bounding box that contains all input boxes, or null if collection is empty or all boxes are null
     */
    public static BoundingBox union(Collection<BoundingBox> boxes) {
        return boxes == null || boxes.isEmpty()
                ? null
                : boxes.stream()
                .filter(Objects::nonNull)
                .reduce(BoundingBox::expand)
                .orElse(null);
    }

    /**
     * Expands this AABB to include another AABB
     *
     * @param other The other AABB to include
     * @return A new AABB that contains both boxes
     */
    public BoundingBox expand(BoundingBox other) {
        double minX = Math.min(this.min.getX(), other.min.getX());
        double minY = Math.min(this.min.getY(), other.min.getY());
        double minZ = Math.min(this.min.getZ(), other.min.getZ());

        double maxX = Math.max(this.max.getX(), other.max.getX());
        double maxY = Math.max(this.max.getY(), other.max.getY());
        double maxZ = Math.max(this.max.getZ(), other.max.getZ());

        return new BoundingBox(new Point(minX, minY, minZ), new Point(maxX, maxY, maxZ));
    }

    /**
     * Ray-box intersection test using slab method
     *
     * @param ray The ray to test
     * @return true if ray intersects this box, false otherwise
     */
    public boolean intersects(Ray ray) {
        Point rayOrigin = ray.getHead();
        Vector rayDir = ray.getDirection();

        double tMin = Double.NEGATIVE_INFINITY;
        double tMax = Double.POSITIVE_INFINITY;

        // Check intersection with each pair of parallel planes
        double[] origins = {rayOrigin.getX(), rayOrigin.getY(), rayOrigin.getZ()};
        double[] directions = {rayDir.getX(), rayDir.getY(), rayDir.getZ()};
        double[] mins = {min.getX(), min.getY(), min.getZ()};
        double[] maxs = {max.getX(), max.getY(), max.getZ()};

        for (int i = 0; i < 3; i++) {
            if (Math.abs(directions[i]) < 1e-10) {
                // Ray is parallel to slab
                if (origins[i] < mins[i] || origins[i] > maxs[i]) {
                    return false;
                }
            } else {
                double t1 = (mins[i] - origins[i]) / directions[i];
                double t2 = (maxs[i] - origins[i]) / directions[i];

                if (t1 > t2) {
                    double temp = t1;
                    t1 = t2;
                    t2 = temp;
                }

                tMin = Math.max(tMin, t1);
                tMax = Math.min(tMax, t2);

                if (tMin > tMax) {
                    return false;
                }
            }
        }

        return tMax >= 0;
    }

    /**
     * Calculates the entry point where a ray first intersects this bounding box
     *
     * @param ray The ray to test
     * @return The entry point, or null if no intersection
     */
    public Point getRayEntryPoint(Ray ray) {
        if (!intersects(ray)) return null;

        Point rayOrigin = ray.getHead();
        Vector rayDir = ray.getDirection();

        // If ray starts inside the box, return the origin
        if (contains(rayOrigin)) return rayOrigin;

        // Find the entry point using slab method
        double tEntry = Double.NEGATIVE_INFINITY;

        double[] origins = {rayOrigin.getX(), rayOrigin.getY(), rayOrigin.getZ()};
        double[] directions = {rayDir.getX(), rayDir.getY(), rayDir.getZ()};
        double[] mins = {min.getX(), min.getY(), min.getZ()};
        double[] maxs = {max.getX(), max.getY(), max.getZ()};

        for (int i = 0; i < 3; i++) {
            if (Math.abs(directions[i]) > 1e-10) {
                double t1 = (mins[i] - origins[i]) / directions[i];
                double t2 = (maxs[i] - origins[i]) / directions[i];

                double tNear = Math.min(t1, t2);
                if (tNear > tEntry) {
                    tEntry = tNear;
                }
            }
        }

        return tEntry < 0
                ? null
                : rayOrigin.add(rayDir.scale(tEntry));
    }

    /**
     * Checks if a point is inside this AABB
     *
     * @param point The point to check
     * @return True if the point is inside, false otherwise
     */
    public boolean contains(Point point) {
        return point.getX() >= min.getX() && point.getX() <= max.getX() &&
                point.getY() >= min.getY() && point.getY() <= max.getY() &&
                point.getZ() >= min.getZ() && point.getZ() <= max.getZ();
    }
}