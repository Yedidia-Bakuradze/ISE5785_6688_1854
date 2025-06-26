package acceleration;

import geometries.*;
import primitives.Point;
import scene.Scene;

import java.util.*;

/**
 * Regular Grid acceleration structure focused solely on grid construction and voxel lookup.
 * Responsibilities:
 * - Build 3D voxel grid from scene geometries
 * - Distribute finite geometries into appropriate voxels
 * - Manage infinite geometries separately
 * - Provide efficient voxel lookup operations
 * - Convert between world and grid coordinate systems
 * <p>
 * Does NOT handle ray traversal - that responsibility belongs to VoxelTraverser.
 */
public class RegularGrid {

    // Grid structure and dimensions
    /**
     * The bounding box of the scene.
     */
    private final BoundingBox sceneBounds;

    /**
     * The resolution of the grid along the X-axis.
     */
    private final int resolutionX;

    /**
     * The resolution of the grid along the Y-axis.
     */
    private final int resolutionY;

    /**
     * The resolution of the grid along the Z-axis.
     */
    private final int resolutionZ;

    /**
     * The size of a voxel along the X-axis.
     */
    private final double voxelSizeX;

    /**
     * The size of a voxel along the Y-axis.
     */
    private final double voxelSizeY;

    /**
     * The size of a voxel along the Z-axis.
     */
    private final double voxelSizeZ;

    /**
     * Recommended density factor for calculating optimal grid resolution.
     */
    public final static double RECOMMENDED_DENSITY_FACTOR = 3.0;

    /**
     * Minimum resolution for the grid.
     */
    public final static int MIN_RESOLUTION = 1;

    /**
     * Maximum resolution for the grid.
     */
    public final static int MAX_RESOLUTION = 100;

    /**
     * Indicates whether the grid contains infinite geometries.
     */
    protected final boolean hasInfiniteGeometries;

    /**
     * Indicates whether the grid contains finite geometries.
     */
    protected final boolean hasFiniteGeometries;

    // Geometry storage
    /**
     * Map storing the voxels in the grid.
     */
    private final Map<VoxelKey, Voxel> voxelMap;

    /**
     * List of infinite geometries in the scene.
     */
    private final List<Intersectable> infiniteGeometries;

    /**
     * Constructs the regular grid from a scene and configuration.
     *
     * @param scene The scene containing geometries to accelerate
     * @throws IllegalArgumentException if scene or config is null
     */
    public RegularGrid(Scene scene) {
        if (scene == null) throw new IllegalArgumentException("Scene cannot be null");

        this.voxelMap = new HashMap<>();
        this.infiniteGeometries = scene.geometries.getInfiniteInjectables();
        this.sceneBounds = scene.geometries.getBoundingBox();

        this.hasInfiniteGeometries = scene.geometries.getInfiniteInjectables() != null;
        this.hasFiniteGeometries = scene.geometries.getFiniteInjectables() != null;

        int resolution = calculateOptimalResolution(scene.geometries.getFiniteInjectables().size());
        this.resolutionX = resolution;
        this.resolutionY = resolution;
        this.resolutionZ = resolution;

        this.voxelSizeX = calculateVoxelSize(sceneBounds.max().getX() - sceneBounds.min().getX(), resolutionX);
        this.voxelSizeY = calculateVoxelSize(sceneBounds.max().getY() - sceneBounds.min().getY(), resolutionY);
        this.voxelSizeZ = calculateVoxelSize(sceneBounds.max().getZ() - sceneBounds.min().getZ(), resolutionZ);

        convertToVoxels(scene);
    }

    /**
     * Gets a voxel at the specified grid coordinates.
     *
     * @param i X grid coordinate
     * @param j Y grid coordinate
     * @param k Z grid coordinate
     * @return Optional containing the voxel if it exists and is non-empty
     */
    public Optional<Voxel> getVoxel(int i, int j, int k) {
        if (!isValidGridCoordinate(i, j, k)) return Optional.empty();
        Voxel voxel = voxelMap.get(new VoxelKey(i, j, k));
        return (voxel != null && !voxel.isEmpty()) ? Optional.of(voxel) : Optional.empty();
    }

    /**
     * Gets a voxel using VoxelKey.
     *
     * @param key The voxel key
     * @return Optional containing the voxel if it exists and is non-empty
     */
    public Optional<Voxel> getVoxel(VoxelKey key) {
        return getVoxel(key.x(), key.y(), key.z());
    }

    /**
     * Gets the scene bounding box that encompasses all finite geometries.
     *
     * @return The axis-aligned bounding box of the scene
     */
    public BoundingBox getSceneBounds() {
        return sceneBounds;
    }

    /**
     * Gets infinite geometries that cannot be grid-accelerated.
     * These geometries (like planes) are handled separately during ray traversal.
     *
     * @return Unmodifiable list of infinite geometries
     */
    public List<Intersectable> getInfiniteGeometries() {
        return Collections.unmodifiableList(infiniteGeometries);
    }

    /**
     * Converts world coordinates to grid coordinates with consistent boundary handling.
     *
     * @param worldPoint Point in world space
     * @return Array of [i, j, k] grid coordinates
     */
    public int[] worldToGrid(Point worldPoint) {
        double relativeX = worldPoint.getX() - sceneBounds.min().getX();
        double relativeY = worldPoint.getY() - sceneBounds.min().getY();
        double relativeZ = worldPoint.getZ() - sceneBounds.min().getZ();

        // Use Math.floor for consistent boundary handling
        int i = (int) Math.floor(relativeX / voxelSizeX);
        int j = (int) Math.floor(relativeY / voxelSizeY);
        int k = (int) Math.floor(relativeZ / voxelSizeZ);

        // Clamp to valid range - ensure max boundary points go to last voxel
        i = Math.max(0, Math.min(i, resolutionX - 1));
        j = Math.max(0, Math.min(j, resolutionY - 1));
        k = Math.max(0, Math.min(k, resolutionZ - 1));

        return new int[]{i, j, k};
    }

    /**
     * Converts grid coordinates to world space center point of the voxel.
     *
     * @param i X grid coordinate
     * @param j Y grid coordinate
     * @param k Z grid coordinate
     * @return Center point of the voxel in world space
     * @throws IllegalArgumentException if coordinates are invalid
     */
    public Point gridToWorld(int i, int j, int k) {
        if (!isValidGridCoordinate(i, j, k))
            throw new IllegalArgumentException("Invalid grid coordinates: (" + i + "," + j + "," + k + ")");

        double worldX = sceneBounds.min().getX() + (i + 0.5) * voxelSizeX;
        double worldY = sceneBounds.min().getY() + (j + 0.5) * voxelSizeY;
        double worldZ = sceneBounds.min().getZ() + (k + 0.5) * voxelSizeZ;

        return new Point(worldX, worldY, worldZ);
    }

    /**
     * Gets grid resolution as an array.
     *
     * @return Array of [resX, resY, resZ]
     */
    public int[] getResolution() {
        return new int[]{resolutionX, resolutionY, resolutionZ};
    }

    /**
     * Gets voxel dimensions in world space.
     *
     * @return Array of [sizeX, sizeY, sizeZ]
     */
    public double[] getVoxelSize() {
        return new double[]{voxelSizeX, voxelSizeY, voxelSizeZ};
    }

    // ======================= Private Helper Methods =======================

    /**
     * Calculates the voxel size based on the dimension and resolution.
     *
     * @param dimension The size of the dimension in world space.
     * @param resolution The number of voxels along the dimension.
     * @return The size of a single voxel along the dimension.
     */
    private double calculateVoxelSize(double dimension, int resolution) {
        if (resolution <= 0) throw new IllegalArgumentException("Resolution must be positive");
        return Math.max(dimension / resolution, 1e-10);
    }

    /**
     * Converts the geometries in the scene into voxels.
     *
     * @param scene The scene containing the geometries.
     */
    private void convertToVoxels(Scene scene) {
        for (Intersectable intersectable : scene.geometries.getGeometries()) {
            if (intersectable instanceof Geometry geometry) {
                BoundingBox bounds = geometry.getBoundingBox();
                if (bounds != null) distributeGeometryToVoxels(geometry, bounds);
            }
        }
    }

    /**
     * Distributes a geometry into the voxels it overlaps.
     *
     * @param geometry The geometry to distribute.
     * @param bounds The bounding box of the geometry.
     */
    private void distributeGeometryToVoxels(Intersectable geometry, BoundingBox bounds) {
        int[] minGrid = worldToGrid(bounds.min());
        int[] maxGrid = worldToGrid(bounds.max());

        // Add geometry to all overlapping voxels
        for (int i = minGrid[0]; i <= maxGrid[0]; i++) {
            for (int j = minGrid[1]; j <= maxGrid[1]; j++) {
                for (int k = minGrid[2]; k <= maxGrid[2]; k++) {
                    if (isValidGridCoordinate(i, j, k))
                        voxelMap.computeIfAbsent(new VoxelKey(i, j, k), kk -> new Voxel()).addGeometry(geometry);
                }
            }
        }
    }

    /**
     * Checks if the given voxel coordinates are valid within the grid resolution.
     *
     * @param i The X-coordinate of the voxel.
     * @param j The Y-coordinate of the voxel.
     * @param k The Z-coordinate of the voxel.
     * @return True if the voxel coordinates are valid, false otherwise.
     */
    private boolean isValidGridCoordinate(int i, int j, int k) {
        return i >= 0 && i < resolutionX &&
                j >= 0 && j < resolutionY &&
                k >= 0 && k < resolutionZ;
    }

    /**
     * Calculates the optimal resolution for the grid based on the number of objects.
     *
     * @param objectCount The number of objects in the scene.
     * @return The optimal resolution for the grid.
     */
    public int calculateOptimalResolution(int objectCount) {
        return Math.max(MIN_RESOLUTION, Math.min(MAX_RESOLUTION, (int) Math.ceil(RECOMMENDED_DENSITY_FACTOR * Math.cbrt(objectCount))));
    }
}