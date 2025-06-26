package acceleration;

import java.util.Objects;

/**
 * Represents a unique key for identifying a voxel in the RegularGrid.
 * The key is defined by the voxel's grid coordinates (x, y, z).
 *
 * @param x The x coordinate of the voxel.
 * @param y The y coordinate of the voxel.
 * @param z The z coordinate of the voxel.
 */
public record VoxelKey(int x, int y, int z) {

    /**
     * Constructs a VoxelKey with specified x, y, z coordinates.
     *
     * @param x The x coordinate of the voxel
     * @param y The y coordinate of the voxel
     * @param z The z coordinate of the voxel
     */
    public VoxelKey {
    }

    /**
     * Gets the x coordinate of the voxel.
     *
     * @return The x coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * Gets the y coordinate of the voxel.
     *
     * @return The y coordinate
     */
    public int getY() {
        return y;
    }

    /**
     * Gets the z coordinate of the voxel.
     *
     * @return The z coordinate
     */
    public int getZ() {
        return z;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        return o instanceof VoxelKey(int x1, int y1, int z1) &&
                x == x1 &&
                y == y1 &&
                z == z1;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }
}