package renderer;

import acceleration.RegularGrid;
import acceleration.VoxelTraverser;
import geometries.Intersectable;
import primitives.Double3;
import primitives.Ray;
import sampling.TargetAreaBase;
import scene.Scene;

import java.util.List;
import java.util.Map;

public class RegularGridRayTracer extends ExtendedRayTracer {
    private final ThreadLocal<VoxelTraverser> voxelTraverser;

    public RegularGridRayTracer(Scene scene, RegularGrid grid) {
        super(scene);
        this.voxelTraverser = ThreadLocal.withInitial(() -> new VoxelTraverser(grid));
    }

    public RegularGridRayTracer(Scene scene, RegularGrid grid, Map<EffectType, TargetAreaBase> targetArea) {
        super(scene, targetArea);
        this.voxelTraverser = ThreadLocal.withInitial(() -> new VoxelTraverser(grid));
    }

    @Override
    protected Intersectable.Intersection findClosestIntersection(Ray ray) {
        return voxelTraverser.get().findClosestIntersection(ray);
    }

    @Override
    protected Double3 transparency(Intersectable.Intersection intersection) {
        Ray shadowRay = new Ray(intersection.point, intersection.lightDirection.scale(-1), intersection.normal);
        double maxDistance = intersection.lightSource.getDistance(shadowRay.getHead());
        List<Intersectable.Intersection> intersections = voxelTraverser.get().findIntersections(shadowRay, maxDistance);
        if (intersections == null) return Double3.ONE;

        Double3 ktr = Double3.ONE;
        for (Intersectable.Intersection shadowIntersection : intersections) {
            if (shadowIntersection.point.distance(intersection.point) < maxDistance) {
                ktr = ktr.product(shadowIntersection.material.kT);
                if (ktr.lowerThan(MIN_CALC_COLOR_K)) return Double3.ZERO;
            }
        }
        return ktr;
    }
}
