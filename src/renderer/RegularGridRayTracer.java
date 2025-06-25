package renderer;

import acceleration.*;
import geometries.Intersectable;
import primitives.Color;
import primitives.Ray;
import sampling.TargetAreaBase;
import scene.Scene;

import java.util.Map;

public class RegularGridRayTracer extends ExtendedRayTracer {
    private final ThreadLocal<VoxelTraverser> voxelTraverser;
    private final RegularGridConfiguration configuration;

    public RegularGridRayTracer(Scene scene, RegularGridConfiguration configuration, RegularGrid grid) {
        super(scene);
        this.voxelTraverser = ThreadLocal.withInitial(() -> new VoxelTraverser(grid));
        this.configuration = configuration;
    }

    public RegularGridRayTracer(Scene scene, RegularGridConfiguration configuration, RegularGrid grid, Map<EffectType, TargetAreaBase> targetArea) {
        super(scene, targetArea);
        this.voxelTraverser = ThreadLocal.withInitial(() -> new VoxelTraverser(grid));
        this.configuration = configuration;
    }

    @Override
    public Color traceRay(Ray ray) {
        // Check if grid acceleration is enabled
        if (!configuration.isEnabled()) return super.traceRay(ray);

        // Use grid traversal to find the closest intersection
        Intersectable.Intersection intersection = voxelTraverser.get().findClosestIntersection(ray);

        // Calculate color using existing shading logic
        return intersection == null
                ? scene.backgroundColor
                : super.calcColor(intersection, ray.getDirection());
    }
}
