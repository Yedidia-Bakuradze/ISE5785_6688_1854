package geometries;

import primitives.Point;

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