package sampling;

import geometries.Intersectable;
import primitives.Point;
import primitives.Vector;

import java.util.List;

public class DiffusiveTargetArea extends TargetAreaBase {

    public DiffusiveTargetArea(CastSamplingMode mode) {
        super(mode);
    }

    @Override
    public List<Point> generateCircularSpreadPoints(Point hitPoint, Vector direction, TargetAreaType shape, SamplingPattern pattern, int numSamples) {
        return List.of();
    }

    @Override
    public List<Point> generateSquaredSpreadPoints(Point hitPoint, Vector direction, TargetAreaType shape, SamplingPattern pattern, int numSamples) {
        return List.of();
    }

    public static List<Vector> generateSamplePoints(Intersectable.Intersection intersection, int numRays, TargetAreaType shape, SamplingPattern pattern) {
        Vector R0 = Vector.refract(intersection.rayDirection, intersection.normal, 1.0,).normalize();

        // 2. build orthonormal basis (u, v) around R0
        Vector3 up = Math.abs(R0.dot(Vector3.UP)) < 0.999 ? Vector3.UP : Vector3.RIGHT;
        Vector3 u = R0.cross(up).normalize();
        Vector3 v = R0.cross(u).normalize();

        // 3. determine sampling grid size
        int gridSize = (int) Math.ceil(Math.sqrt(numRays));
        double radius = intersection.getMaterial().getRoughness(); // assume roughness in [0,1]
        List<Vector3> samples = new ArrayList<>(numRays);

        // 4. sample points in local (u,v) plane
        for (int y = 0; y < gridSize && samples.size() < numRays; y++) {
            for (int x = 0; x < gridSize && samples.size() < numRays; x++) {
                // compute local offsets in [-1,1]
                double sx, sy;
                if (pattern == Sampling.GRID) {
                    sx = (2.0 * x + 1) / gridSize - 1.0;
                    sy = (2.0 * y + 1) / gridSize - 1.0;
                } else { // JITTERED
                    sx = (2.0 * (x + RNG.nextDouble()) / gridSize) - 1.0;
                    sy = (2.0 * (y + RNG.nextDouble()) / gridSize) - 1.0;
                }

                // apply shape test
                if (shape == Shape.CIRCLE) {
                    if (sx * sx + sy * sy > 1.0) continue; // outside unit circle
                }

                // map to actual radius
                double du = sx * radius;
                double dv = sy * radius;

                // world-space target point
                Vector3 offset = u.mul(du).add(v.mul(dv));
                Vector3 targetPoint = intersection.getHitPoint().add(offset);
                samples.add(targetPoint);
            }
        }
        return samples;
    }

}
