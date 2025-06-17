package sampling;

import geometries.Intersectable;
import primitives.Point;
import primitives.Vector;

import java.util.ArrayList;
import java.util.List;

public class DiffusiveTargetArea extends TargetAreaBase {

    public DiffusiveTargetArea(SamplingMode mode, TargetAreaType shape, SamplingPattern pattern) {
        super(mode, shape, pattern);
    }

    @Override
    public List<Point> generateSamplePoints(Intersectable.Intersection intersection) {
        // Calculate the perfect reflection direction
        Vector R0 = intersection.rayDirection.subtract(
                intersection.normal.scale(2 * intersection.rayDirection.dotProduct(intersection.normal))
        );

        // Build orthonormal basis (u, v) around R0
        Vector up = Math.abs(Vector.AXIS_Y.dotProduct(R0)) < 0.999 ? Vector.AXIS_Y : Vector.AXIS_X;
        Vector u = R0.crossProduct(up).normalize();
        Vector v = R0.crossProduct(u).normalize();

        // Determine sampling grid size
        double radius = intersection.material.roughness;
        List<Point> samples = new ArrayList<>(this.mode.numberSamples);

        // Sample points in local (u,v) plane
        double sx = 0, sy = 0;
        for (int y = 0; y < this.mode.gridValue && samples.size() < this.mode.numberSamples; y++) {
            for (int x = 0; x < this.mode.gridValue && samples.size() < this.mode.numberSamples; x++) {
                // Compute local offsets in [-1,1]
                switch (pattern) {
                    case SamplingPattern.GRID -> {
                        sx = (2.0 * x + 1) / this.mode.gridValue - 1.0;
                        sy = (2.0 * y + 1) / this.mode.gridValue - 1.0;
                    }
                    case SamplingPattern.JITTERED -> {
                        sx = (2.0 * (x + this.random.nextDouble()) / this.mode.gridValue) - 1.0;
                        sy = (2.0 * (y + this.random.nextDouble()) / this.mode.gridValue) - 1.0;
                    }
                }

                // Apply shape test
                if (shape == TargetAreaType.CIRCLE && sx * sx + sy * sy > 1.0) continue;

                // Map to actual radius
                double du = sx * radius;
                double dv = sy * radius;

                // World-space target point
                try {
                    Vector offset = u.scale(du).add(v.scale(dv));
                    Point targetPoint = intersection.point.add(offset);
                    samples.add(targetPoint);
                } catch (Exception e) {
                    continue;
                }
            }
        }
        return samples;
    }

}
