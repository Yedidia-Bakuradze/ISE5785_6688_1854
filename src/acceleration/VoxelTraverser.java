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

    /**
     * The regular grid used for voxel traversal.
     */
    private final RegularGrid grid;

    /**
     * Set of geometries that have already been tested for intersections.
     */
    private final Set<Intersectable> testedGeometries;

    /**
     * Constructs a voxel traverser for the specified regular grid.
     *
     * @param grid The regular grid to use for traversal.
     */
    public VoxelTraverser(RegularGrid grid) {
        if (grid == null) throw new IllegalArgumentException("Grid cannot be null");
        this.grid = grid;
        this.testedGeometries = new HashSet<>();
    }

    /**
     * Finds the closest intersection along a ray.
     *
     * @param ray The ray to test for intersections.
     * @return The closest intersection, or null if none exist.
     */
    public Intersection findClosestIntersection(Ray ray) {
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
        testedGeometries.clear();
        if (!grid.hasFiniteGeometries) return null;

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

            // Test geometries in current voxel
            Intersection voxelClosest = castRayToClosestFiniteObjects(ddaState.currentVoxel, ray, minDistance);
            if (voxelClosest != null) {
                double distance = voxelClosest.point.distance(ray.getHead());
                if (distance < minDistance) {
                    minDistance = distance;
                    closest = voxelClosest;
                }
            }
            getNextVoxel(ddaState);
        }
        return closest;
    }

    /**
     * Finds all intersections along a ray up to a maximum distance.
     *
     * @param ray         The ray to test for intersections.
     * @param maxDistance The maximum distance for intersections.
     * @return A list of intersections, or null if none exist.
     */
    public List<Intersection> findIntersections(Ray ray, double maxDistance) {
        // Initialize traversal state
        testedGeometries.clear();
        List<Intersection> allIntersections = new ArrayList<>();

        // Phase 1: Test infinite geometries first
        if (this.grid.hasInfiniteGeometries) castRayToInfiniteObjects(ray, allIntersections);

        // Phase 2: Check if ray intersects scene bounds
        if (grid.getSceneBounds().intersects(ray)) return allIntersections.isEmpty() ? null : allIntersections;

        // Phase 3: Perform 3D-DDA traversal through grid
        preform3DDDAWalk(ray, allIntersections, maxDistance);

        return allIntersections.isEmpty() ? null : allIntersections;
    }

    // ======================= Private Helper Methods =======================

    /**
     * Tests intersections with infinite geometries
     *
     * @param ray          The ray to test for intersections.
     * @param intersections The list to store intersections.
     */
    private void castRayToInfiniteObjects(Ray ray, List<Intersection> intersections) {
        if (!grid.hasInfiniteGeometries) return;

        for (Intersectable geometry : grid.getInfiniteGeometries()) {
            List<Intersection> geoIntersections = geometry.calculateIntersections(ray);
            if (geoIntersections != null) {
                intersections.addAll(geoIntersections);
            }
        }
    }

    /**
     * Performs 3D-DDA traversal through the grid voxels.
     *
     * @param ray          The ray to test for intersections.
     * @param intersections The list to store intersections.
     * @param maxDistance   The maximum distance for intersections.
     */
    private void preform3DDDAWalk(Ray ray, List<Intersection> intersections, double maxDistance) {
        // Calculate ray entry point into scene bounds
        Point entryPoint = grid.getSceneBounds().getRayEntryPoint(ray);
        if (entryPoint == null) return;

        // Initialize 3D-DDA variables
        DDAState ddaState = initializeDDA(ray, entryPoint);

        // Traverse voxels using 3D-DDA
        int[] resolution = grid.getResolution();
        while (isValidVoxel(ddaState.currentVoxel, resolution)) {
            int minAxis = 0;
            if (ddaState.next[1] < ddaState.next[minAxis]) minAxis = 1;
            if (ddaState.next[2] < ddaState.next[minAxis]) minAxis = 2;
            if (ddaState.next[minAxis] > maxDistance) break;

            castRayToFiniteObjects(ddaState.currentVoxel, ray, intersections);
            getNextVoxel(ddaState);
        }
    }

    /**
     * Tests all geometries in a specific voxel for intersections.
     *
     * @param voxelCoords   The grid coordinates of the voxel.
     * @param ray           The ray to test for intersections.
     * @param intersections The list to store intersections.
     */
    private void castRayToFiniteObjects(int[] voxelCoords, Ray ray, List<Intersection> intersections) {
        Optional<Voxel> voxelOpt = grid.getVoxel(voxelCoords[0], voxelCoords[1], voxelCoords[2]);
        if (voxelOpt.isEmpty()) return;
        for (Intersectable geometry : voxelOpt.get().getGeometries()) {
            if (testedGeometries.contains(geometry)) continue;
            testedGeometries.add(geometry);

            // Test intersection
            List<Intersection> geoIntersections = geometry.calculateIntersections(ray);
            if (geoIntersections != null) {
                intersections.addAll(geoIntersections);
            }
        }
    }

    /**
     * Tests geometries in a voxel for the closest intersection only.
     *
     * @param voxelCoords        The grid coordinates of the voxel.
     * @param ray                The ray to test for intersections.
     * @param currentMinDistance The current minimum distance to beat.
     * @return The closest intersection, or null if none exist.
     */
    private Intersection castRayToClosestFiniteObjects(int[] voxelCoords, Ray ray, double currentMinDistance) {
        Optional<Voxel> voxelOpt = grid.getVoxel(voxelCoords[0], voxelCoords[1], voxelCoords[2]);
        if (voxelOpt.isEmpty()) return null;

        Intersection closest = null;
        double minDistance = currentMinDistance;

        for (Intersectable geometry : voxelOpt.get().getGeometries()) {
            if (testedGeometries.contains(geometry)) continue;
            testedGeometries.add(geometry);

            List<Intersection> geoIntersections = geometry.calculateIntersections(ray);
            if (geoIntersections != null) {
                for (Intersection intersection : geoIntersections) {
                    double distance = intersection.point.distance(ray.getHead());
                    if (distance < minDistance) {
                        minDistance = distance;
                        closest = intersection;
                    }
                }
            }
        }

        return closest;
    }

    /**
     * Initializes the 3D-DDA traversal state for a ray.
     *
     * @param ray        The ray to initialize traversal for.
     * @param entryPoint The entry point of the ray into the grid.
     * @return The initialized DDA state.
     */
    private DDAState initializeDDA(Ray ray, Point entryPoint) {
        Vector rayDir = ray.getDirection();
        int[] currentVoxel = grid.worldToGrid(entryPoint);
        int[] step = {
                rayDir.getX() > 0 ? 1 : -1,
                rayDir.getY() > 0 ? 1 : -1,
                rayDir.getZ() > 0 ? 1 : -1
        };

        double[] voxelSize = grid.getVoxelSize();
        double[] delta = new double[3];
        double[] rayDirComponents = {rayDir.getX(), rayDir.getY(), rayDir.getZ()};

        for (int i = 0; i < 3; i++) {
            delta[i] = Math.abs(rayDirComponents[i]) < 1e-10 ? Double.MAX_VALUE : Math.abs(voxelSize[i] / rayDirComponents[i]);
        }

        // Calculate initial distances to next voxel boundaries
        return new DDAState(currentVoxel, step, delta, calculateInitialDistances(entryPoint, currentVoxel, step, delta, rayDir));
    }

    /**
     * Calculates the initial distances to voxel boundaries for 3D-DDA traversal.
     *
     * @param entryPoint   The entry point of the ray into the grid.
     * @param currentVoxel The current voxel coordinates.
     * @param step         The step direction for traversal.
     * @param delta        The delta distances for traversal.
     * @param rayDir       The direction of the ray.
     * @return An array of initial distances to voxel boundaries.
     */
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
                double boundary = sceneMinComponents[axis] + (currentVoxel[axis] + (step[axis] > 0 ? 1 : 0)) * voxelSize[axis];
                next[axis] = Math.abs((boundary - entryComponents[axis]) / rayDirComponents[axis]);
            }
        }

        return next;
    }

    /**
     * Advances the DDA state to the next voxel.
     *
     * @param state The current DDA state.
     */
    private void getNextVoxel(DDAState state) {
        int minAxis = 0;
        if (state.next[1] < state.next[minAxis]) minAxis = 1;
        if (state.next[2] < state.next[minAxis]) minAxis = 2;

        state.next[minAxis] += state.delta[minAxis];
        state.currentVoxel[minAxis] += state.step[minAxis];
    }

    /**
     * Checks if the given voxel coordinates are valid within the grid resolution.
     *
     * @param voxel      The voxel coordinates to check.
     * @param resolution The grid resolution.
     * @return True if the voxel coordinates are valid, false otherwise.
     */
    private boolean isValidVoxel(int[] voxel, int[] resolution) {
        return voxel[0] >= 0 && voxel[0] < resolution[0] &&
                voxel[1] >= 0 && voxel[1] < resolution[1] &&
                voxel[2] >= 0 && voxel[2] < resolution[2];
    }

    /**
     * Represents the state of the 3D-DDA traversal.
     *
     * @param currentVoxel The current voxel coordinates being traversed.
     * @param step         The step direction for traversal along each axis.
     * @param delta        The delta distances for traversal along each axis.
     * @param next         The distances to the next voxel boundaries along each axis.
     */
    private record DDAState(int[] currentVoxel, int[] step, double[] delta, double[] next) {
        /**
         * Constructs a DDAState with the specified traversal parameters.
         *
         * @param currentVoxel The current voxel coordinates being traversed.
         * @param step         The step direction for traversal along each axis.
         * @param delta        The delta distances for traversal along each axis.
         * @param next         The distances to the next voxel boundaries along each axis.
         */
        private DDAState(int[] currentVoxel, int[] step, double[] delta, double[] next) {
            this.currentVoxel = currentVoxel.clone();
            this.step = step.clone();
            this.delta = delta.clone();
            this.next = next.clone();
        }
    }
}