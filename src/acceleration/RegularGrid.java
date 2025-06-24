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

    // Geometry storage
    private final Map<VoxelKey, Voxel> voxelMap;
    private final List<Intersectable> infiniteGeometries;

    // Statistics
    private final GridStatistics statistics;

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
        this.infiniteGeometries = new ArrayList<>();
        this.statistics = new GridStatistics();

        // Phase 1: Calculate scene bounds from finite geometries
        this.sceneBounds = calculateSceneBounds(scene);
        logDebug("Scene bounds: " + sceneBounds);

        // Phase 2: Determine optimal grid resolution
        int finiteGeometryCount = countFiniteGeometries(scene);
        int[] resolution = config.calculateOptimalResolution(finiteGeometryCount);
        this.resolutionX = resolution[0];
        this.resolutionY = resolution[1];
        this.resolutionZ = resolution[2];

        logDebug(String.format("Grid resolution: %dx%dx%d for %d finite geometries",
                resolutionX, resolutionY, resolutionZ, finiteGeometryCount));

        // Phase 3: Calculate voxel dimensions
        this.voxelSizeX = calculateVoxelSize(sceneBounds.max().getX() - sceneBounds.min().getX(), resolutionX);
        this.voxelSizeY = calculateVoxelSize(sceneBounds.max().getY() - sceneBounds.min().getY(), resolutionY);
        this.voxelSizeZ = calculateVoxelSize(sceneBounds.max().getZ() - sceneBounds.min().getZ(), resolutionZ);

        // Phase 4: Build the grid by distributing geometries
        buildGrid(scene);

        // Phase 5: Calculate statistics ONCE and cache results
        int totalPlacements = calculateTotalGeometryPlacements();
        statistics.finalizeBuild(voxelMap.size(), totalPlacements, infiniteGeometries.size(),
                resolutionX * resolutionY * resolutionZ);

        // Phase 6: Validate memory usage with cached value
        validateMemoryUsage(totalPlacements);

        if (config.isPrintStatistics()) {
            printGridStatistics();
        }
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
        if (!isValidGridCoordinate(i, j, k)) {
            return Optional.empty();
        }

        VoxelKey key = new VoxelKey(i, j, k);
        Voxel voxel = voxelMap.get(key);

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
        if (!isValidGridCoordinate(i, j, k)) {
            throw new IllegalArgumentException("Invalid grid coordinates: (" + i + "," + j + "," + k + ")");
        }

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

    /**
     * Gets grid statistics for performance analysis.
     *
     * @return Grid statistics object
     */
    public GridStatistics getStatistics() {
        return statistics;
    }

    // ======================= Private Helper Methods =======================

    private void validateInputs(Scene scene, RegularGridConfiguration config) {
        if (scene == null) {
            throw new IllegalArgumentException("Scene cannot be null");
        }
        if (config == null) {
            throw new IllegalArgumentException("Configuration cannot be null");
        }
    }

    /**
     * Calculates the scene bounding box from all finite geometries.
     * Handles the case where no finite geometries exist.
     */
    private BoundingBox calculateSceneBounds(Scene scene) {
        List<BoundingBox> finiteBounds = scene.geometries.getBoundingBoxes();

        // Union all bounding boxes
        BoundingBox result = BoundingBox.union(finiteBounds);

        // Handle degenerate scene case with proper warning
        if (result == null) {
            System.err.println("Warning: No finite geometries found in scene. Using default unit cube bounds.");
            logDebug("Scene contains only infinite geometries (planes, etc.)");
            result = new BoundingBox(new Point(-1, -1, -1), new Point(1, 1, 1));
        }

        return result;
    }

    /**
     * Counts the number of finite geometries in the scene.
     */
    private int countFiniteGeometries(Scene scene) {
        return (int) scene.geometries.getBoundingBoxes().stream().filter(Objects::nonNull).count();
    }

    /**
     * Calculates voxel size with protection against degenerate cases.
     */
    private double calculateVoxelSize(double dimension, int resolution) {
        if (resolution <= 0) {
            throw new IllegalArgumentException("Resolution must be positive");
        }

        double size = dimension / resolution;

        // Protect against very small voxels
        if (size < 1e-10) {
            System.err.println("Warning: Very small voxel size detected: " + size);
            size = 1e-10;
        }

        return size;
    }

    /**
     * Builds the grid by distributing geometries into appropriate voxels.
     */
    private void buildGrid(Scene scene) {
        int processedFinite = 0;
        int processedInfinite = 0;

        for (Intersectable intersectable : scene.geometries.getGeometries()) {
            if (intersectable instanceof Geometry geometry) {
                BoundingBox bounds = geometry.getBoundingBox();

                if (bounds == null) {
                    // Infinite geometry - handle separately
                    infiniteGeometries.add(geometry);
                    processedInfinite++;
                    logDebug("Added infinite geometry: " + geometry.getClass().getSimpleName());
                } else {
                    // Finite geometry - distribute to overlapping voxels
                    distributeGeometryToVoxels(geometry, bounds);
                    processedFinite++;
                }
            }
        }

        logDebug(String.format("Processed %d finite geometries, %d infinite geometries",
                processedFinite, processedInfinite));
    }

    /**
     * Distributes a single geometry to all voxels it overlaps with consistent boundary handling.
     */
    private void distributeGeometryToVoxels(Intersectable geometry, BoundingBox bounds) {
        // Find the range of voxels that the geometry overlaps using consistent coordinate conversion
        int[] minGrid = worldToGrid(bounds.min());
        int[] maxGrid = worldToGrid(bounds.max());

        int placementCount = 0;

        // Add geometry to all overlapping voxels
        for (int i = minGrid[0]; i <= maxGrid[0]; i++) {
            for (int j = minGrid[1]; j <= maxGrid[1]; j++) {
                for (int k = minGrid[2]; k <= maxGrid[2]; k++) {
                    if (isValidGridCoordinate(i, j, k)) {
                        VoxelKey key = new VoxelKey(i, j, k);

                        // Create voxel if it doesn't exist (sparse storage)
                        Voxel voxel = voxelMap.computeIfAbsent(key, kk -> new Voxel());
                        voxel.addGeometry(geometry);

                        placementCount++;
                        statistics.recordGeometryPlacement();
                    }
                }
            }
        }

        // Check for potential performance issues
        if (placementCount > config.getMaxObjectsPerVoxel()) {
            System.err.printf("Warning: Geometry %s placed in %d voxels (max recommended: %d)%n",
                    geometry.getClass().getSimpleName(), placementCount, config.getMaxObjectsPerVoxel());
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

    /**
     * Calculates the total number of geometry placements across all voxels.
     * Caches the result to avoid multiple iterations.
     */
    private int calculateTotalGeometryPlacements() {
        return voxelMap.values().stream()
                .mapToInt(Voxel::size)
                .sum();
    }

    /**
     * Validates memory usage against configuration limits using cached total placements.
     */
    private void validateMemoryUsage(int totalPlacements) {
        double estimatedMemoryMB = config.estimateMemoryUsage(
                resolutionX, resolutionY, resolutionZ, totalPlacements
        );

        if (estimatedMemoryMB > config.getMaxMemoryUsageMB()) {
            System.err.printf("Warning: Estimated memory usage (%.1f MB) exceeds limit (%.1f MB)%n",
                    estimatedMemoryMB, config.getMaxMemoryUsageMB());
        }

        logDebug(String.format("Estimated memory usage: %.1f MB", estimatedMemoryMB));
    }

    /**
     * Prints detailed grid statistics to console.
     */
    private void printGridStatistics() {
        System.out.println("\n=== Regular Grid Statistics ===");
        System.out.printf("Grid Resolution: %dx%dx%d%n", resolutionX, resolutionY, resolutionZ);
        System.out.printf("Voxel Size: %.3f x %.3f x %.3f%n", voxelSizeX, voxelSizeY, voxelSizeZ);
        System.out.printf("Total Voxels: %d%n", statistics.getTotalVoxels());
        System.out.printf("Occupied Voxels: %d (%.1f%%)%n",
                statistics.getOccupiedVoxelCount(),
                statistics.getStorageEfficiency() * 100);
        System.out.printf("Infinite Geometries: %d%n", statistics.getInfiniteGeometryCount());
        System.out.printf("Total Geometry Placements: %d%n", statistics.getTotalGeometryPlacements());
        System.out.printf("Average Geometries per Occupied Voxel: %.1f%n", statistics.getAverageGeometriesPerVoxel());
        System.out.println("==============================\n");
    }

    /**
     * Logs debug messages if debug mode is enabled.
     */
    private void logDebug(String message) {
        if (config.isDebugMode()) {
            System.out.println("[RegularGrid] " + message);
        }
    }

    // ======================= Inner Classes =======================

    /**
     * Statistics collection for performance analysis and debugging.
     * Provides meaningful metrics without arbitrary calculations.
     */
    public static class GridStatistics {
        private int occupiedVoxelCount;
        private int totalGeometryPlacements;
        private int infiniteGeometryCount;
        private int totalVoxels;
        private int geometryPlacementCounter = 0;

        void finalizeBuild(int occupiedVoxels, int totalPlacements, int infiniteGeometries, int totalVoxelCount) {
            this.occupiedVoxelCount = occupiedVoxels;
            this.totalGeometryPlacements = totalPlacements;
            this.infiniteGeometryCount = infiniteGeometries;
            this.totalVoxels = totalVoxelCount;
        }

        void recordGeometryPlacement() {
            geometryPlacementCounter++;
        }

        // Getters for meaningful statistics
        public int getOccupiedVoxelCount() {
            return occupiedVoxelCount;
        }

        public int getTotalGeometryPlacements() {
            return totalGeometryPlacements;
        }

        public int getInfiniteGeometryCount() {
            return infiniteGeometryCount;
        }

        public int getTotalVoxels() {
            return totalVoxels;
        }

        public double getAverageGeometriesPerVoxel() {
            return occupiedVoxelCount == 0 ? 0.0 : (double) totalGeometryPlacements / occupiedVoxelCount;
        }

        /**
         * Gets the storage efficiency (occupied voxels vs total voxels)
         */
        public double getStorageEfficiency() {
            return totalVoxels == 0 ? 0.0 : (double) occupiedVoxelCount / totalVoxels;
        }

        /**
         * Gets the geometry distribution factor (how many voxels per geometry on average)
         */
        public double getGeometryDistributionFactor() {
            // This would require tracking unique geometries, simplified for now
            return totalGeometryPlacements > 0 ? (double) totalGeometryPlacements / Math.max(1, occupiedVoxelCount) : 0.0;
        }
    }
}