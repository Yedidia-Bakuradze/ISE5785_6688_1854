package renderer;

import acceleration.*;
import geometries.Intersectable;
import primitives.Color;
import primitives.Ray;
import sampling.TargetAreaBase;
import scene.Scene;

import java.util.Map;

public class RegularGridRayTracer extends ExtendedRayTracer {
    private final RegularGrid grid;
    private final VoxelTraverser voxelTraverser;
    private final RegularGridConfiguration configuration;

    public RegularGridRayTracer(Scene scene, RegularGrid grid, VoxelTraverser voxelTraverser, RegularGridConfiguration configuration) {
        super(scene);
        this.grid = grid;
        this.voxelTraverser = voxelTraverser;
        this.configuration = configuration;
    }

    public RegularGridRayTracer(Scene scene, RegularGridConfiguration configuration, RegularGrid grid, VoxelTraverser voxelTraverser, Map<EffectType, TargetAreaBase> targetArea) {
        super(scene, targetArea);
        this.grid = grid;
        this.voxelTraverser = voxelTraverser;
        this.configuration = configuration;
    }

    @Override
    public Color traceRay(Ray ray) {
        // Check if grid acceleration is enabled
        if (!configuration.isEnabled()) return super.traceRay(ray);

        // Use grid traversal to find the closest intersection
        Intersectable.Intersection intersection = voxelTraverser.findClosestIntersection(ray);

        // Calculate color using existing shading logic
        return intersection == null
                ? scene.backgroundColor
                : super.calcColor(intersection, ray.getDirection());
    }
}
