package sampling;

import geometries.Intersectable;
import primitives.*;

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
     * @param config The sampling configuration containing mode, shape, and pattern settings
     */
    public DiffusiveTargetArea(SamplingConfiguration config) {
        super(config);
    }

    @Override
    protected List<Point> generateSamplePoints(Intersectable.Intersection intersection) {
        // Calculate the perfect refraction direction using Snell's law (transmission through object)
        Vector T0 = intersection.rayDirection.calcSnellRefraction(intersection.normal, Material.AIR_IOR, intersection.material.ior);

        List<Vector> listOfCoordinates = Vector.getNewCoordinateSystems(T0, Vector.AXIS_X);
        Vector u = listOfCoordinates.get(0);
        Vector v = listOfCoordinates.get(1);

        List<Point> samples = new ArrayList<>(this.config.mode.numberSamples);

        // Sample points in local (u,v) plane around the transmitted direction
        double sx = 0, sy = 0;
        for (int y = 0; y < this.config.mode.gridValue && samples.size() < this.config.mode.numberSamples; y++) {
            for (int x = 0; x < this.config.mode.gridValue && samples.size() < this.config.mode.numberSamples; x++) {
                // Compute local offsets in [-1,1]
                switch (config.pattern) {
                    case SamplingPattern.GRID -> {
                        sx = (2.0 * x + 1) / this.config.mode.gridValue - 1.0;
                        sy = (2.0 * y + 1) / this.config.mode.gridValue - 1.0;
                    }
                    case SamplingPattern.JITTERED -> {
                        sx = (2.0 * (x + this.config.random.nextDouble()) / this.config.mode.gridValue) - 1.0;
                        sy = (2.0 * (y + this.config.random.nextDouble()) / this.config.mode.gridValue) - 1.0;
                    }
                }

                // Apply shape test
                if (config.shape == TargetAreaType.CIRCLE && sx * sx + sy * sy > 1.0) continue;

                // Map to actual radius (controls how much the transmission scatters)
                double du = sx * intersection.material.roughness;
                double dv = sy * intersection.material.roughness;

                // World-space target point (offset from the perfect transmission direction)
                Vector offset = u.scale(du).add(v.scale(dv));
                // Create target point along the scattered transmission direction
                Point targetPoint = intersection.point.add(T0.add(offset).normalize().scale(this.config.distance));
                samples.add(targetPoint);
            }
        }
        return samples;
    }

    @Override
    public List<Ray> generateRays(Intersectable.Intersection intersection) {
        return generateSamplePoints(intersection).stream()
                .map(point -> new Ray(intersection.point, point.subtract(intersection.point).normalize(), intersection.normal))
                .toList();
    }

}
