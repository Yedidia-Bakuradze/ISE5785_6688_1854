package acceleration;

/**
 * Configuration class for Regular Grid acceleration parameters.
 * Contains all settings needed to customize grid behavior, performance,
 * and memory usage according to scene requirements and system capabilities.
 * <p>
 * Follows software engineering principles by centralizing configuration
 * and providing builder pattern for easy setup and validation.
 */
public class RegularGridConfiguration {

    // ======================= Grid Resolution Settings =======================

    /**
     * Whether to automatically calculate grid resolution based on object count
     */
    private final boolean automaticResolution;

    /**
     * Manual grid resolution for X dimension (ignored if automaticResolution is true)
     */
    private final int manualResolutionX;

    /**
     * Manual grid resolution for Y dimension (ignored if automaticResolution is true)
     */
    private final int manualResolutionY;

    /**
     * Manual grid resolution for Z dimension (ignored if automaticResolution is true)
     */
    private final int manualResolutionZ;

    /**
     * Density factor for automatic resolution calculation.
     * Formula: resolution = densityFactor * ∛(objectCount)
     * Typical values: 1.0 to 3.0
     */
    private final double densityFactor;

    /**
     * Minimum grid resolution per dimension to prevent degenerate cases
     */
    private final int minResolution;

    /**
     * Maximum grid resolution per dimension to prevent excessive memory usage
     */
    private final int maxResolution;

    // ======================= Memory Management Settings =======================

    /**
     * Enable sparse storage using HashMap for empty voxels
     */
    private final boolean enableSparseStorage;

    /**
     * Maximum memory usage in MB before applying memory optimizations
     */
    private final double maxMemoryUsageMB;

    /**
     * Maximum number of objects per voxel before subdivision warning
     */
    private final int maxObjectsPerVoxel;

    // ======================= Performance Tuning Settings =======================

    /**
     * Enable early ray termination when first intersection is found
     */
    private final boolean enableEarlyTermination;

    /**
     * Avoid duplicate intersection tests for same object on same ray
     */
    private final boolean enableObjectDeduplication;

    /**
     * Use optimized 3D-DDA traversal algorithm
     */
    private final boolean useOptimizedTraversal;

    /**
     * Enable ray-box intersection optimization before detailed traversal
     */
    private final boolean enableRayBoxOptimization;

    // ======================= Debug and Testing Settings =======================

    /**
     * Master switch to enable/disable entire grid acceleration
     */
    private final boolean isEnabled;

    /**
     * Enable debug mode with additional logging and validation
     */
    private final boolean debugMode;

    /**
     * Collect performance metrics during rendering
     */
    private final boolean collectMetrics;

    /**
     * Enable grid visualization for debugging purposes
     */
    private final boolean visualizeGrid;

    /**
     * Print detailed statistics after grid construction
     */
    private final boolean printStatistics;

    // ======================= Constructor =======================

    /**
     * Private constructor - use Builder pattern
     */
    private RegularGridConfiguration(Builder builder) {
        this.automaticResolution = builder.automaticResolution;
        this.manualResolutionX = builder.manualResolutionX;
        this.manualResolutionY = builder.manualResolutionY;
        this.manualResolutionZ = builder.manualResolutionZ;
        this.densityFactor = builder.densityFactor;
        this.minResolution = builder.minResolution;
        this.maxResolution = builder.maxResolution;

        this.enableSparseStorage = builder.enableSparseStorage;
        this.maxMemoryUsageMB = builder.maxMemoryUsageMB;
        this.maxObjectsPerVoxel = builder.maxObjectsPerVoxel;

        this.enableEarlyTermination = builder.enableEarlyTermination;
        this.enableObjectDeduplication = builder.enableObjectDeduplication;
        this.useOptimizedTraversal = builder.useOptimizedTraversal;
        this.enableRayBoxOptimization = builder.enableRayBoxOptimization;

        this.isEnabled = builder.enabled;
        this.debugMode = builder.debugMode;
        this.collectMetrics = builder.collectMetrics;
        this.visualizeGrid = builder.visualizeGrid;
        this.printStatistics = builder.printStatistics;
    }

    // ======================= Getters =======================

    public boolean isAutomaticResolution() {
        return automaticResolution;
    }

    public int getManualResolutionX() {
        return manualResolutionX;
    }

    public int getManualResolutionY() {
        return manualResolutionY;
    }

    public int getManualResolutionZ() {
        return manualResolutionZ;
    }

    public double getDensityFactor() {
        return densityFactor;
    }

    public int getMinResolution() {
        return minResolution;
    }

    public int getMaxResolution() {
        return maxResolution;
    }

    public boolean isEnableSparseStorage() {
        return enableSparseStorage;
    }

    public double getMaxMemoryUsageMB() {
        return maxMemoryUsageMB;
    }

    public int getMaxObjectsPerVoxel() {
        return maxObjectsPerVoxel;
    }

    public boolean isEnableEarlyTermination() {
        return enableEarlyTermination;
    }

    public boolean isEnableObjectDeduplication() {
        return enableObjectDeduplication;
    }

    public boolean isUseOptimizedTraversal() {
        return useOptimizedTraversal;
    }

    public boolean isEnableRayBoxOptimization() {
        return enableRayBoxOptimization;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public boolean isDebugMode() {
        return debugMode;
    }

    public boolean isCollectMetrics() {
        return collectMetrics;
    }

    public boolean isVisualizeGrid() {
        return visualizeGrid;
    }

    public boolean isPrintStatistics() {
        return printStatistics;
    }

    // ======================= Builder Pattern =======================

    /**
     * Builder class for GridConfiguration with sensible defaults
     */
    public static class Builder {

        // Default values based on research and best practices
        private boolean automaticResolution = true;
        private int manualResolutionX = 10;
        private int manualResolutionY = 10;
        private int manualResolutionZ = 10;
        private double densityFactor = 2.0;
        private int minResolution = 1;
        private int maxResolution = 100;

        private boolean enableSparseStorage = true;
        private double maxMemoryUsageMB = 512.0;
        private int maxObjectsPerVoxel = 50;

        private boolean enableEarlyTermination = true;
        private boolean enableObjectDeduplication = true;
        private boolean useOptimizedTraversal = true;
        private boolean enableRayBoxOptimization = true;

        private boolean enabled = true;
        private boolean debugMode = false;
        private boolean collectMetrics = false;
        private boolean visualizeGrid = false;
        private boolean printStatistics = false;

        // ======================= Grid Resolution Setters =======================

        /**
         * Enable or disable automatic grid resolution calculation
         */
        public Builder setAutomaticResolution(boolean automatic) {
            this.automaticResolution = automatic;
            return this;
        }

        /**
         * Set manual grid resolution for all dimensions
         */
        public Builder setManualResolution(int resolution) {
            return setManualResolution(resolution, resolution, resolution);
        }

        /**
         * Set manual grid resolution for each dimension separately
         */
        public Builder setManualResolution(int x, int y, int z) {
            this.manualResolutionX = x;
            this.manualResolutionY = y;
            this.manualResolutionZ = z;
            return this;
        }

        /**
         * Set density factor for automatic resolution calculation
         */
        public Builder setDensityFactor(double factor) {
            if (factor <= 0) throw new IllegalArgumentException("Density factor must be positive");
            this.densityFactor = factor;
            return this;
        }

        /**
         * Set minimum and maximum resolution bounds
         */
        public Builder setResolutionBounds(int min, int max) {
            if (min <= 0) throw new IllegalArgumentException("Minimum resolution must be positive");
            if (max <= min) throw new IllegalArgumentException("Maximum resolution must be greater than minimum");
            this.minResolution = min;
            this.maxResolution = max;
            return this;
        }

        // ======================= Memory Management Setters =======================

        /**
         * Enable or disable sparse storage optimization
         */
        public Builder setSparseStorage(boolean enabled) {
            this.enableSparseStorage = enabled;
            return this;
        }

        /**
         * Set maximum memory usage before optimizations kick in
         */
        public Builder setMaxMemoryUsage(double megabytes) {
            if (megabytes <= 0) throw new IllegalArgumentException("Memory usage must be positive");
            this.maxMemoryUsageMB = megabytes;
            return this;
        }

        /**
         * Set maximum objects per voxel threshold
         */
        public Builder setMaxObjectsPerVoxel(int maxObjects) {
            if (maxObjects <= 0) throw new IllegalArgumentException("Max objects per voxel must be positive");
            this.maxObjectsPerVoxel = maxObjects;
            return this;
        }

        // ======================= Performance Tuning Setters =======================

        /**
         * Enable or disable early ray termination
         */
        public Builder setEarlyTermination(boolean enabled) {
            this.enableEarlyTermination = enabled;
            return this;
        }

        /**
         * Enable or disable object deduplication during traversal
         */
        public Builder setObjectDeduplication(boolean enabled) {
            this.enableObjectDeduplication = enabled;
            return this;
        }

        /**
         * Enable or disable optimized 3D-DDA traversal
         */
        public Builder setOptimizedTraversal(boolean enabled) {
            this.useOptimizedTraversal = enabled;
            return this;
        }

        /**
         * Enable or disable ray-box intersection optimization
         */
        public Builder setRayBoxOptimization(boolean enabled) {
            this.enableRayBoxOptimization = enabled;
            return this;
        }

        // ======================= Debug and Testing Setters =======================

        /**
         * Master switch to enable/disable grid acceleration
         */
        public Builder setEnabled(boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        /**
         * Enable or disable debug mode
         */
        public Builder setDebugMode(boolean debug) {
            this.debugMode = debug;
            return this;
        }

        /**
         * Enable or disable performance metrics collection
         */
        public Builder setCollectMetrics(boolean collect) {
            this.collectMetrics = collect;
            return this;
        }

        /**
         * Enable or disable grid visualization
         */
        public Builder setVisualizeGrid(boolean visualize) {
            this.visualizeGrid = visualize;
            return this;
        }

        /**
         * Enable or disable statistics printing
         */
        public Builder setPrintStatistics(boolean print) {
            this.printStatistics = print;
            return this;
        }

        // ======================= Preset Configurations =======================

        /**
         * Configure for maximum performance (may use more memory)
         */
        public Builder setHighPerformanceMode() {
            return setDensityFactor(3.0)
                    .setEarlyTermination(true)
                    .setObjectDeduplication(true)
                    .setOptimizedTraversal(true)
                    .setRayBoxOptimization(true)
                    .setSparseStorage(true)
                    .setMaxMemoryUsage(1024.0);
        }

        /**
         * Configure for memory efficiency (might be slightly slower)
         */
        public Builder setMemoryEfficientMode() {
            return setDensityFactor(1.5)
                    .setSparseStorage(true)
                    .setMaxMemoryUsage(256.0)
                    .setMaxObjectsPerVoxel(30);
        }

        /**
         * Configure for debugging and testing
         */
        public Builder setDebugMode() {
            return setDebugMode(true)
                    .setCollectMetrics(true)
                    .setPrintStatistics(true)
                    .setVisualizeGrid(true);
        }

        // ======================= Build Method =======================

        /**
         * Build and validate the configuration
         */
        public RegularGridConfiguration build() {
            // Validation
            if (!automaticResolution) {
                if (manualResolutionX < minResolution || manualResolutionX > maxResolution)
                    throw new IllegalArgumentException("Manual resolution X out of bounds");
                if (manualResolutionY < minResolution || manualResolutionY > maxResolution)
                    throw new IllegalArgumentException("Manual resolution Y out of bounds");
                if (manualResolutionZ < minResolution || manualResolutionZ > maxResolution)
                    throw new IllegalArgumentException("Manual resolution Z out of bounds");
            }

            return new RegularGridConfiguration(this);
        }
    }

    public static class Factory {
        public static RegularGridConfiguration createConfiguration(AccelerationMode mode) {
            return switch (mode) {
                case NONE -> null;
                case DEFAULT -> createDefault();
                case PERFORMANCE -> createHighPerformance();
                case MEMORY_SAVING -> createMemoryEfficient();
                case DEBUG -> createDebugMode();
            };
        }

        /**
         * Create default configuration suitable for most scenes
         */
        private static RegularGridConfiguration createDefault() {
            return new Builder().build();
        }

        /**
         * Create high-performance configuration for complex scenes
         */
        private static RegularGridConfiguration createHighPerformance() {
            return new Builder().setHighPerformanceMode().build();
        }

        /**
         * Create memory-efficient configuration for resource-constrained environments
         */
        private static RegularGridConfiguration createMemoryEfficient() {
            return new Builder().setMemoryEfficientMode().build();
        }

        /**
         * Create debug configuration for development and testing
         */
        private static RegularGridConfiguration createDebugMode() {
            return new Builder().setDebugMode().build();
        }
    }

    // ======================= Utility Methods =======================

    /**
     * Calculate optimal grid resolution based on object count
     */
    public int[] calculateOptimalResolution(int objectCount) {
        if (!automaticResolution) return new int[]{manualResolutionX, manualResolutionY, manualResolutionZ};

        // Empirical formula: resolution = densityFactor * ∛(objectCount)
        int baseResolution = (int) Math.ceil(densityFactor * Math.cbrt(objectCount));

        // Apply bounds
        baseResolution = Math.max(minResolution, Math.min(maxResolution, baseResolution));
        return new int[]{baseResolution, baseResolution, baseResolution};
    }

    /**
     * Estimate memory usage for given grid resolution
     */
    public double estimateMemoryUsage(int resX, int resY, int resZ, int objectCount) {
        // Rough estimation based on sparse storage efficiency
        double totalVoxels = resX * resY * resZ;
        double occupiedVoxels = Math.min(totalVoxels, objectCount * 2); // Objects may span multiple voxels

        // Each voxel entry ~100 bytes (references + overhead)
        return (occupiedVoxels * 100.0) / (1024.0 * 1024.0); // Convert to MB
    }

    @Override
    public String toString() {
        return String.format(
                "GridConfiguration{enabled=%s, automatic=%s, density=%.1f, sparse=%s, earlyTerm=%s}",
                isEnabled, automaticResolution, densityFactor, enableSparseStorage, enableEarlyTermination
        );
    }
}