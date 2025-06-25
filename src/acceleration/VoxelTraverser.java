package acceleration;

import geometries.Intersectable;
import primitives.*;
import primitives.Vector;

import java.util.*;

import static geometries.Intersectable.Intersection;

/**
 * Handles 3D-DDA (Digital Differential Analyzer) traversal of the Regular Grid.
 * Corrected critical bugs in intersection logic.
 */
public class VoxelTraverser {

    private final RegularGrid grid;
    private final Set<Intersectable> testedGeometries;
    private TraversalStatistics currentTraversalStats;

    public VoxelTraverser(RegularGrid grid) {
        if (grid == null) throw new IllegalArgumentException("Grid cannot be null");
        this.grid = grid;
        this.testedGeometries = new HashSet<>();
    }

    /**
     * Finds the closest intersection along a ray
     */
    public Intersection findClosestIntersection(Ray ray) {
        // Initialize traversal state
        testedGeometries.clear();
        currentTraversalStats = new TraversalStatistics();

        Intersection infiniteClosest = findClosestInfiniteIntersection(ray);
        Intersection voxelClosest = findClosestVoxelIntersection(ray);

        // Return the closer of the two
        if (infiniteClosest == null && voxelClosest == null) {
            return null;
        } else if (infiniteClosest == null) {
            return voxelClosest;
        } else if (voxelClosest == null) {
            return infiniteClosest;
        } else {
            double infiniteDistance = infiniteClosest.point.distance(ray.getHead());
            double voxelDistance = voxelClosest.point.distance(ray.getHead());
            return infiniteDistance <= voxelDistance ? infiniteClosest : voxelClosest;
        }
    }

    /**
     * Finds the closest intersection with infinite geometries only
     *
     * @param ray The ray to test
     * @return The closest intersection with infinite geometries, or null if none
     */
    private Intersection findClosestInfiniteIntersection(Ray ray) {
        if (!grid.hasInfiniteGeometries) return null;

        Intersection closest = null;
        double minDistance = Double.MAX_VALUE;

        for (Intersectable geometry : grid.getInfiniteGeometries()) {
            List<Intersection> geoIntersections = geometry.calculateIntersections(ray);
            if (geoIntersections != null) {
                for (Intersection intersection : geoIntersections) {
                    double distance = intersection.point.distance(ray.getHead());
                    if (distance < minDistance) {
                        minDistance = distance;
                        closest = intersection;
                    }
                }
                currentTraversalStats.infiniteGeometryTests++;
            }
        }

        return closest;
    }

    /**
     * Finds the closest intersection with voxel geometries only
     *
     * @param ray The ray to test
     * @return The closest intersection with voxel geometries, or null if none
     */
    private Intersection findClosestVoxelIntersection(Ray ray) {
        if (!grid.hasFiniteGeometries) return null;
        if (!grid.getSceneBounds().intersects(ray)) return null;

        // Calculate ray entry point into scene bounds
        Point entryPoint = grid.getSceneBounds().getRayEntryPoint(ray);
        if (entryPoint == null) return null;

        // Initialize 3D-DDA variables
        DDAState ddaState = initializeDDA(ray, entryPoint);

        Intersection closest = null;
        double minDistance = Double.MAX_VALUE;

        // Traverse voxels using 3D-DDA
        int[] resolution = grid.getResolution();
        while (isValidVoxel(ddaState.currentVoxel, resolution)) {
            currentTraversalStats.voxelsVisited++;

            // Test geometries in current voxel
            Intersection voxelClosest = testVoxelGeometriesForClosest(ddaState.currentVoxel, ray, minDistance);
            if (voxelClosest != null) {
                double distance = voxelClosest.point.distance(ray.getHead());
                if (distance < minDistance) {
                    minDistance = distance;
                    closest = voxelClosest;

                    // Early termination if enabled
                    if (grid.getConfiguration().isEnableEarlyTermination()) break;
                }
            }

            // Move to next voxel
            advanceToNextVoxel(ddaState);
        }

        return closest;
    }

    public List<Intersection> findIntersections(Ray ray) {
        return findIntersections(ray, Double.POSITIVE_INFINITY);
    }

    /**
     * Finds all intersections along a ray
     */
    public List<Intersection> findIntersections(Ray ray, double maxDistance) {
        // Initialize traversal state
        testedGeometries.clear();
        currentTraversalStats = new TraversalStatistics();

        List<Intersection> allIntersections = new ArrayList<>();

        // Phase 1: Test infinite geometries first
        if (this.grid.hasInfiniteGeometries) testInfiniteGeometries(ray, allIntersections);

        // Phase 2: Check if ray intersects scene bounds
        if (grid.getSceneBounds().intersects(ray)) return allIntersections.isEmpty() ? null : allIntersections;

        // Phase 3: Perform 3D-DDA traversal through grid
        perform3DDATraversal(ray, allIntersections, maxDistance);

        return allIntersections.isEmpty() ? null : allIntersections;
    }

    // ======================= Private Helper Methods =======================

    /**
     * Tests intersections with infinite geometries
     */
    private void testInfiniteGeometries(Ray ray, List<Intersection> intersections) {
        if (!grid.hasInfiniteGeometries) return;

        for (Intersectable geometry : grid.getInfiniteGeometries()) {
            List<Intersection> geoIntersections = geometry.calculateIntersections(ray);
            if (geoIntersections != null) {
                intersections.addAll(geoIntersections);
            }
        }
    }

    /**
     * Performs 3D-DDA traversal through the grid voxels
     */
    private void perform3DDATraversal(Ray ray, List<Intersection> intersections, double maxDistance) {
        // Calculate ray entry point into scene bounds
        Point entryPoint = grid.getSceneBounds().getRayEntryPoint(ray);
        if (entryPoint == null) return;

        // Initialize 3D-DDA variables
        DDAState ddaState = initializeDDA(ray, entryPoint);

        // Traverse voxels using 3D-DDA
        int[] resolution = grid.getResolution();
        while (isValidVoxel(ddaState.currentVoxel, resolution)) {
            // Find which axis is next
            int minAxis = 0;
            if (ddaState.next[1] < ddaState.next[minAxis]) minAxis = 1;
            if (ddaState.next[2] < ddaState.next[minAxis]) minAxis = 2;
            if (ddaState.next[minAxis] > maxDistance) break;

            testVoxelGeometries(ddaState.currentVoxel, ray, intersections);
            advanceToNextVoxel(ddaState);
        }
    }

    /**
     * Tests all geometries in a specific voxel for intersections
     */
    private void testVoxelGeometries(int[] voxelCoords, Ray ray, List<Intersection> intersections) {
        Optional<Voxel> voxelOpt = grid.getVoxel(voxelCoords[0], voxelCoords[1], voxelCoords[2]);
        if (voxelOpt.isEmpty()) return;

        Voxel voxel = voxelOpt.get();
        currentTraversalStats.nonEmptyVoxelsVisited++;

        for (Intersectable geometry : voxel.getGeometries()) {
            // Skip if already tested (deduplication)
            if (grid.getConfiguration().isEnableObjectDeduplication() && testedGeometries.contains(geometry)) {
                currentTraversalStats.duplicateTests++;
                continue;
            }

            // Mark as tested
            if (grid.getConfiguration().isEnableObjectDeduplication()) {
                testedGeometries.add(geometry);
            }

            // Test intersection
            List<Intersection> geoIntersections = geometry.calculateIntersections(ray);
            if (geoIntersections != null) {
                intersections.addAll(geoIntersections);
                currentTraversalStats.intersectionsFound += geoIntersections.size();
            }

            currentTraversalStats.geometryTests++;
        }
    }

    /**
     * Tests geometries in a voxel for closest intersection only
     */
    private Intersection testVoxelGeometriesForClosest(int[] voxelCoords, Ray ray, double currentMinDistance) {
        Optional<Voxel> voxelOpt = grid.getVoxel(voxelCoords[0], voxelCoords[1], voxelCoords[2]);
        if (voxelOpt.isEmpty()) return null;

        Voxel voxel = voxelOpt.get();
        currentTraversalStats.nonEmptyVoxelsVisited++;

        Intersection closest = null;
        double minDistance = currentMinDistance;

        for (Intersectable geometry : voxel.getGeometries()) {
            // Skip if already tested
            if (grid.getConfiguration().isEnableObjectDeduplication() && testedGeometries.contains(geometry)) {
                currentTraversalStats.duplicateTests++;
                continue;
            }

            if (grid.getConfiguration().isEnableObjectDeduplication()) {
                testedGeometries.add(geometry);
            }

            List<Intersection> geoIntersections = geometry.calculateIntersections(ray);
            if (geoIntersections != null) {
                for (Intersection intersection : geoIntersections) {
                    double distance = intersection.point.distance(ray.getHead());
                    if (distance < minDistance) {
                        minDistance = distance;
                        closest = intersection;
                    }
                }
                currentTraversalStats.intersectionsFound += geoIntersections.size();
            }

            currentTraversalStats.geometryTests++;
        }

        return closest;
    }

    private DDAState initializeDDA(Ray ray, Point entryPoint) {
        Vector rayDir = ray.getDirection();

        // Convert entry point to grid coordinates
        int[] currentVoxel = grid.worldToGrid(entryPoint);

        // Calculate step directions
        int[] step = {
                rayDir.getX() > 0 ? 1 : -1,
                rayDir.getY() > 0 ? 1 : -1,
                rayDir.getZ() > 0 ? 1 : -1
        };

        // Get voxel dimensions
        double[] voxelSize = grid.getVoxelSize();

        // Calculate delta distances
        double[] delta = new double[3];
        double[] rayDirComponents = {rayDir.getX(), rayDir.getY(), rayDir.getZ()};

        for (int i = 0; i < 3; i++) {
            delta[i] = Math.abs(rayDirComponents[i]) < 1e-10 ?
                    Double.MAX_VALUE :
                    Math.abs(voxelSize[i] / rayDirComponents[i]);
        }

        // Calculate initial distances to next voxel boundaries
        double[] next = calculateInitialDistances(entryPoint, currentVoxel, step, delta, rayDir);

        return new DDAState(currentVoxel, step, delta, next);
    }

    private double[] calculateInitialDistances(Point entryPoint, int[] currentVoxel, int[] step, double[] delta, Vector rayDir) {
        double[] next = new double[3];
        Point sceneMin = grid.getSceneBounds().min();
        double[] voxelSize = grid.getVoxelSize();

        double[] entryComponents = {entryPoint.getX(), entryPoint.getY(), entryPoint.getZ()};
        double[] sceneMinComponents = {sceneMin.getX(), sceneMin.getY(), sceneMin.getZ()};
        double[] rayDirComponents = {rayDir.getX(), rayDir.getY(), rayDir.getZ()};

        for (int axis = 0; axis < 3; axis++) {
            if (Math.abs(rayDirComponents[axis]) < 1e-10) {
                next[axis] = Double.MAX_VALUE;
            } else {
                // Calculate distance to next voxel boundary
                double boundary = sceneMinComponents[axis] +
                        (currentVoxel[axis] + (step[axis] > 0 ? 1 : 0)) * voxelSize[axis];
                next[axis] = Math.abs((boundary - entryComponents[axis]) / rayDirComponents[axis]);
            }
        }

        return next;
    }

    private void advanceToNextVoxel(DDAState state) {
        // Find the axis with minimum next distance
        int minAxis = 0;
        if (state.next[1] < state.next[minAxis]) minAxis = 1;
        if (state.next[2] < state.next[minAxis]) minAxis = 2;

        // Advance along that axis
        state.next[minAxis] += state.delta[minAxis];
        state.currentVoxel[minAxis] += state.step[minAxis];
    }

    private boolean isValidVoxel(int[] voxel, int[] resolution) {
        return voxel[0] >= 0 && voxel[0] < resolution[0] &&
                voxel[1] >= 0 && voxel[1] < resolution[1] &&
                voxel[2] >= 0 && voxel[2] < resolution[2];
    }

    // Inner classes remain the same
    private record DDAState(int[] currentVoxel, int[] step, double[] delta, double[] next) {
        private DDAState(int[] currentVoxel, int[] step, double[] delta, double[] next) {
            this.currentVoxel = currentVoxel.clone();
            this.step = step.clone();
            this.delta = delta.clone();
            this.next = next.clone();
        }
    }

    private static class TraversalStatistics {
        int voxelsVisited = 0;
        int nonEmptyVoxelsVisited = 0;
        int geometryTests = 0;
        int duplicateTests = 0;
        int intersectionsFound = 0;
        int infiniteGeometryTests = 0;
    }

    public TraversalStatistics getCurrentTraversalStats() {
        return currentTraversalStats;
    }
}