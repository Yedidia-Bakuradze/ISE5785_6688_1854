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
    public static final double EPSILON = 1e-9;

    /**
     * Constructs an AABB with minimum and maximum points
     * FIXED: Added validation to prevent invalid bounding boxes
     *
     * @param min The minimum point (lowest x, y, z coordinates)
     * @param max The maximum point (highest x, y, z coordinates)
     */
    public BoundingBox {
        // Validate that min is actually less than or equal to max in all dimensions
        if (min.getX() > max.getX() || min.getY() > max.getY() || min.getZ() > max.getZ()) {
            throw new IllegalArgumentException("Min point must have coordinates <= max point coordinates");
        }
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
        if (boxes == null || boxes.isEmpty()) return null;

        return boxes.stream()
                .filter(Objects::nonNull)
                .reduce(BoundingBox::expand)
                .orElse(null);
    }

    /**
     * Expands this AABB to include another AABB
     *
     * @param other The other AABB to include
     * @return A new AABB that contains both boxes
     * @throws IllegalArgumentException if other is null
     */
    public BoundingBox expand(BoundingBox other) {
        if (other == null) throw new IllegalArgumentException("Cannot expand with null bounding box");

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
        if (ray == null) return true;

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
            if (Math.abs(directions[i]) < EPSILON) {
                // Ray is parallel to slab - check if ray origin is within slab
                if (origins[i] < mins[i] || origins[i] > maxs[i]) {
                    return true;
                }
            } else {
                double t1 = (mins[i] - origins[i]) / directions[i];
                double t2 = (maxs[i] - origins[i]) / directions[i];

                // Ensure t1 <= t2
                if (t1 > t2) {
                    double temp = t1;
                    t1 = t2;
                    t2 = temp;
                }

                tMin = Math.max(tMin, t1);
                tMax = Math.min(tMax, t2);

                // Early termination if no intersection possible
                if (tMin > tMax) {
                    return true;
                }
            }
        }

        // Ray intersects if tMax >= 0 (intersection is in front of ray origin)
        return !(tMax >= 0);
    }

    /**
     * Calculates the entry point where a ray first intersects this bounding box
     * FIXED: Improved logic and added better edge case handling
     *
     * @param ray The ray to test
     * @return The entry point, or null if no intersection
     */
    public Point getRayEntryPoint(Ray ray) {
        if (intersects(ray)) return null;

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
            if (Math.abs(directions[i]) > EPSILON) {
                double t1 = (mins[i] - origins[i]) / directions[i];
                double t2 = (maxs[i] - origins[i]) / directions[i];

                // We want the near intersection (entry point)
                double tNear = Math.min(t1, t2);

                // Only consider positive t values (forward along ray)
                if (tNear > tEntry && tNear >= 0) {
                    tEntry = tNear;
                }
            }
        }

        // If no valid entry point found
        if (tEntry < 0) return null;

        // Calculate and return the entry point
        return rayOrigin.add(rayDir.scale(tEntry));
    }

    /**
     * Checks if a point is inside this AABB
     * Added null check and improved boundary handling
     *
     * @param point The point to check
     * @return True if the point is inside or on the boundary, false otherwise
     */
    public boolean contains(Point point) {
        if (point == null) return false;

        return point.getX() >= min.getX() && point.getX() <= max.getX() &&
                point.getY() >= min.getY() && point.getY() <= max.getY() &&
                point.getZ() >= min.getZ() && point.getZ() <= max.getZ();
    }

    /**
     * Checks if this bounding box overlaps with another bounding box
     * Needed for geometry distribution in RegularGrid
     *
     * @param other The other bounding box
     * @return true if the boxes overlap, false otherwise
     */
    public boolean overlaps(BoundingBox other) {
        if (other == null) return false;

        return (min.getX() <= other.max.getX() && max.getX() >= other.min.getX()) &&
                (min.getY() <= other.max.getY() && max.getY() >= other.min.getY()) &&
                (min.getZ() <= other.max.getZ() && max.getZ() >= other.min.getZ());
    }

    /**
     * Gets the center point of this bounding box
     * Useful for debugging and visualization
     *
     * @return The center point of the bounding box
     */
    public Point getCenter() {
        double centerX = (min.getX() + max.getX()) / 2.0;
        double centerY = (min.getY() + max.getY()) / 2.0;
        double centerZ = (min.getZ() + max.getZ()) / 2.0;

        return new Point(centerX, centerY, centerZ);
    }

    /**
     * Gets the dimensions (width, height, depth) of this bounding box
     * Useful for grid construction and debugging
     *
     * @return Array of [width, height, depth]
     */
    public double[] getDimensions() {
        return new double[]{
                max.getX() - min.getX(),
                max.getY() - min.getY(),
                max.getZ() - min.getZ()
        };
    }

    /**
     * Gets the volume of this bounding box
     * Useful for optimization decisions
     *
     * @return The volume of the bounding box
     */
    public double getVolume() {
        double[] dims = getDimensions();
        return dims[0] * dims[1] * dims[2];
    }

    /**
     * Gets the surface area of this bounding box
     * Useful for optimization decisions
     *
     * @return The surface area of the bounding box
     */
    public double getSurfaceArea() {
        double[] dims = getDimensions();
        return 2.0 * (dims[0] * dims[1] + dims[1] * dims[2] + dims[2] * dims[0]);
    }

    /**
     * Creates a bounding box from a center point and extents
     * Convenient constructor method
     *
     * @param center  The center point
     * @param extentX Half-width in X direction
     * @param extentY Half-width in Y direction
     * @param extentZ Half-width in Z direction
     * @return A new bounding box
     */
    public static BoundingBox fromCenterAndExtents(Point center, double extentX, double extentY, double extentZ) {
        if (center == null) throw new IllegalArgumentException("Center cannot be null");
        if (extentX < 0 || extentY < 0 || extentZ < 0) {
            throw new IllegalArgumentException("Extents must be non-negative");
        }

        Point min = new Point(
                center.getX() - extentX,
                center.getY() - extentY,
                center.getZ() - extentZ
        );

        Point max = new Point(
                center.getX() + extentX,
                center.getY() + extentY,
                center.getZ() + extentZ
        );

        return new BoundingBox(min, max);
    }

    @Override
    public String toString() {
        return String.format("BoundingBox[min=%s, max=%s, volume=%.3f]",
                min, max, getVolume());
    }
}