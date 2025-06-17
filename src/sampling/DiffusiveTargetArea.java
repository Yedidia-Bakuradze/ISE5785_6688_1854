package sampling;

import geometries.Intersectable;
import primitives.Point;
import primitives.Vector;

import java.util.ArrayList;
import java.util.List;

public class DiffusiveTargetArea extends TargetAreaBase {

    public DiffusiveTargetArea(CastSamplingMode mode) {
        super(mode);
    }

    @Override
    public List<Point> generateSamplePoints(Intersectable.Intersection intersection, TargetAreaType shape, SamplingPattern pattern) {
        Vector R0 = intersection.rayDirection.calcSnellRefraction(intersection.normal, 1.0, intersection.geometry.getMaterial().ior);

        // 2. build orthonormal basis (u, v) around R0
        Vector up = Math.abs(Vector.AXIS_Y.dotProduct(R0)) < 0.999 ? Vector.AXIS_Y : Vector.AXIS_X;
        Vector u = R0.crossProduct(up).normalize();
        Vector v = R0.crossProduct(u).normalize();

        // 3. determine sampling grid size
        double radius = intersection.material.roughness;
        List<Point> samples = new ArrayList<>(this.mode.numberSamples);

        // 4. sample points in local (u,v) plane
        double sx = 0, sy = 0;
        for (int y = 0; y < this.mode.gridValue && samples.size() < this.mode.numberSamples; y++) {
            for (int x = 0; x < this.mode.gridValue && samples.size() < this.mode.numberSamples; x++) {
                // compute local offsets in [-1,1]
                switch (pattern) {
                    case SamplingPattern.GRID -> {
                        sx = (2.0 * x + 1) / this.mode.gridValue - 1.0;
                        sy = (2.0 * y + 1) / this.mode.gridValue - 1.0;
                        break;
                    }
                    case SamplingPattern.JITTERED -> {
                        sx = (2.0 * (x + this.random.nextDouble()) / this.mode.gridValue) - 1.0;
                        sy = (2.0 * (y + this.random.nextDouble()) / this.mode.gridValue) - 1.0;
                        break;
                    }
                }

                // apply shape test
                if (shape == TargetAreaType.CIRCLE && sx * sx + sy * sy > 1.0) continue;

                // map to actual radius
                double du = sx * radius;
                double dv = sy * radius;

                // world-space target point
                Vector offset = u.scale(du).add(v.scale(dv));
                Point targetPoint = intersection.point.add(offset);
                samples.add(targetPoint);
            }
        }
        return samples;
    }

}
