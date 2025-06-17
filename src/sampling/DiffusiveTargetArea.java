package sampling;

import geometries.Intersectable;
import primitives.Point;
import primitives.Vector;

import java.util.ArrayList;
import java.util.List;

/**
 * A class for generating target points for diffusive glass effects.
 * This class creates scattered sample points around the primary ray direction based on
 * material roughness to simulate imperfect glass transmission.
 */
public class DiffusiveTargetArea extends TargetAreaBase {

    /**
     * Constructs a diffusive target area for generating scattered ray samples
     *
     * @param mode    The sampling mode that controls the number of samples generated
     * @param shape   The shape of the sampling area (circle or square)
     * @param pattern The distribution pattern of samples (grid, random, jittered)
     */
    public DiffusiveTargetArea(SamplingMode mode, TargetAreaType shape, SamplingPattern pattern) {
        super(mode, shape, pattern);
    }

    @Override
    public List<Point> generateSamplePoints(Intersectable.Intersection intersection) {
        // Calculate the perfect refraction direction using Snell's law (transmission through object)
        Vector T0 = intersection.rayDirection.calcSnellRefraction(intersection.normal, 1.0, intersection.material.ior);

        // If total internal reflection occurs, fall back to perfect transmission
        if (T0 == null) {
            T0 = intersection.rayDirection; // Continue in same direction
        }

        // Build orthonormal basis (u, v) around T0 (transmitted direction)
        Vector up = Math.abs(Vector.AXIS_Y.dotProduct(T0)) < 0.999 ? Vector.AXIS_Y : Vector.AXIS_X;
        Vector u = T0.crossProduct(up).normalize();
        Vector v = T0.crossProduct(u).normalize();

        // Determine sampling area size based on material roughness
        double radius = intersection.material.roughness;
        List<Point> samples = new ArrayList<>(this.mode.numberSamples);

        // Sample points in local (u,v) plane around the transmitted direction
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

                // Map to actual radius (controls how much the transmission scatters)
                double du = sx * radius;
                double dv = sy * radius;

                // World-space target point (offset from the perfect transmission direction)
                try {
                    Vector offset = u.scale(du).add(v.scale(dv));
                    // Create target point along the scattered transmission direction
                    Point targetPoint = intersection.point.add(T0.add(offset).normalize());
                    samples.add(targetPoint);
                } catch (Exception e) {
                    continue;
                }
            }
        }
        return samples;
    }

}
