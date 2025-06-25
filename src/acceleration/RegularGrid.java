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
    private final RegularGridConfiguration config;
    private final BoundingBox sceneBounds;
    private final int resolutionX;
    private final int resolutionY;
    private final int resolutionZ;
    private final double voxelSizeX;
    private final double voxelSizeY;
    private final double voxelSizeZ;

    protected final boolean hasInfiniteGeometries;
    protected final boolean hasFiniteGeometries;

    // Geometry storage
    private final Map<VoxelKey, Voxel> voxelMap;
    private final List<Intersectable> infiniteGeometries;

    /**
     * Constructs the regular grid from a scene and configuration.
     *
     * @param scene  The scene containing geometries to accelerate
     * @param config Configuration parameters for grid construction
     * @throws IllegalArgumentException if scene or config is null
     */
    public RegularGrid(Scene scene, RegularGridConfiguration config) {
        validateInputs(scene, config);

        this.config = config;
        this.voxelMap = new HashMap<>();
        this.infiniteGeometries = scene.geometries.getInfiniteInjectables();
        this.sceneBounds = scene.geometries.getBoundingBox();

        this.hasInfiniteGeometries = scene.geometries.getInfiniteInjectables() != null;
        this.hasFiniteGeometries = scene.geometries.getFiniteInjectables() != null;

        //TODO: Disable the feature if no finite geometries exist

        int resolution = config.calculateOptimalResolution(scene.geometries.getFiniteInjectables().size());
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

    public RegularGridConfiguration getConfiguration() {
        return config;
    }

    // ======================= Private Helper Methods =======================

    private void validateInputs(Scene scene, RegularGridConfiguration config) {
        if (scene == null) throw new IllegalArgumentException("Scene cannot be null");
        if (config == null) throw new IllegalArgumentException("Configuration cannot be null");
    }

    /**
     * Calculates voxel size with protection against degenerate cases.
     */
    private double calculateVoxelSize(double dimension, int resolution) {
        if (resolution <= 0) throw new IllegalArgumentException("Resolution must be positive");
        return Math.max(dimension / resolution, 1e-10);
    }

    /**
     * Builds the grid by distributing geometries into appropriate voxels.
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
     * Distributes a single geometry to all voxels it overlaps with consistent boundary handling.
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
     * Validates that the current voxel coordinates are within grid bounds.
     */
    private boolean isValidGridCoordinate(int i, int j, int k) {
        return i >= 0 && i < resolutionX &&
                j >= 0 && j < resolutionY &&
                k >= 0 && k < resolutionZ;
    }
}