package renderer;

import acceleration.*;
import geometries.Intersectable;
import primitives.*;
import sampling.TargetAreaBase;
import scene.Scene;

import java.util.List;
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
        // Use grid traversal to find the closest intersection
        Intersectable.Intersection intersection = voxelTraverser.get().findClosestIntersection(ray);

        // Calculate color using existing shading logic
        return intersection == null
                ? scene.backgroundColor
                : super.calcColor(intersection, ray.getDirection());
    }

    // In RegularGridRayTracer.java

    @Override
    protected Double3 transparency(Intersectable.Intersection intersection) {
        // Define the shadow ray starting from the intersection point
        Ray shadowRay = new Ray(intersection.point, intersection.lightDirection.scale(-1), intersection.normal);

        // Get the distance to the light source. We don't need to check for shadows beyond this point.
        double maxDistance = intersection.lightSource.getDistance(intersection.point);

        // Use the VOXEL TRAVERSER to find intersections along the shadow ray up to the light source.
        List<Intersectable.Intersection> intersections = voxelTraverser.get().findIntersections(shadowRay);

        // If there are no intersections, the point is un-shaded.
        if (intersections == null) {
            return Double3.ONE; // ktr = 1.0
        }

        // Calculate transparency by accumulating kT from objects that cast shadows.
        Double3 ktr = Double3.ONE;
        for (Intersectable.Intersection shadowIntersection : intersections) {
            // Ensure the found intersection is not behind the light source (due to floating point inaccuracies)
            // This check might be redundant if your maxDistance traversal works perfectly, but it's safe to keep.
            if (shadowIntersection.point.distance(intersection.point) < maxDistance) {
                ktr = ktr.product(shadowIntersection.material.kT);
                // If light is already fully blocked, no need to check further.
                if (ktr.lowerThan(MIN_CALC_COLOR_K)) {
                    return Double3.ZERO;
                }
            }
        }
        return ktr;
    }
}
