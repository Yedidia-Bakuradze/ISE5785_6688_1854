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

        return switch (this.config.pattern) {
            case SamplingPattern.JITTERED -> generateSamplesJittered(intersection, u, v);
            case SamplingPattern.RANDOM -> generateSamplesRandom(intersection, u, v);
            case SamplingPattern.GRID -> generateSamplesGrid(intersection, u, v);
        };
    }

    private List<Point> generateSamplesJittered(Intersectable.Intersection intersection, Vector u, Vector v) {
        List<Point> samples = new ArrayList<>(this.config.mode.numberSamples);
        double sx, sy;

        for (int y = 0; y < this.config.mode.gridValue && samples.size() < this.config.mode.numberSamples; y++) {
            for (int x = 0; x < this.config.mode.gridValue && samples.size() < this.config.mode.numberSamples; x++) {
                sx = (2.0 * (x + this.config.random.nextDouble()) / this.config.mode.gridValue) - 1.0;
                sy = (2.0 * (y + this.config.random.nextDouble()) / this.config.mode.gridValue) - 1.0;

                // Apply shape test
                if (config.shape == TargetAreaType.CIRCLE && sx * sx + sy * sy > 1.0) continue;

                // Map to actual radius (controls how much the transmission scatters)
                double du = sx * intersection.material.roughness;
                double dv = sy * intersection.material.roughness;

                // World-space target point (offset from the perfect transmission direction)
                Vector offset = u.scale(du).add(v.scale(dv));
                // Create target point along the scattered transmission direction
                Point targetPoint = intersection.point.add(intersection.rayDirection.add(offset).normalize().scale(this.config.distance));
                samples.add(targetPoint);
            }
        }
        return samples;
    }

    private List<Point> generateSamplesGrid(Intersectable.Intersection intersection, Vector u, Vector v) {
        List<Point> samples = new ArrayList<>(this.config.mode.numberSamples);
        double sx, sy;

        for (int y = 0; y < this.config.mode.gridValue && samples.size() < this.config.mode.numberSamples; y++) {
            for (int x = 0; x < this.config.mode.gridValue && samples.size() < this.config.mode.numberSamples; x++) {
                sx = (2.0 * x + 1) / this.config.mode.gridValue - 1.0;
                sy = (2.0 * y + 1) / this.config.mode.gridValue - 1.0;

                // Apply shape test
                if (config.shape == TargetAreaType.CIRCLE && sx * sx + sy * sy > 1.0) continue;

                // Map to actual radius (controls how much the transmission scatters)
                double du = sx * intersection.material.roughness;
                double dv = sy * intersection.material.roughness;

                // World-space target point (offset from the perfect transmission direction)
                Vector offset = u.scale(du).add(v.scale(dv));
                // Create target point along the scattered transmission direction
                Point targetPoint = intersection.point.add(intersection.rayDirection.add(offset).normalize().scale(this.config.distance));
                samples.add(targetPoint);
            }
        }
        return samples;
    }

    private List<Point> generateSamplesRandom(Intersectable.Intersection intersection, Vector u, Vector v) {
        List<Point> samples = new ArrayList<>(this.config.mode.numberSamples);
        double sx, sy;

        while (samples.size() < this.config.mode.numberSamples) {
            sx = 2.0 * this.config.random.nextDouble() - 1.0;
            sy = 2.0 * this.config.random.nextDouble() - 1.0;

            // Apply shape test
            if (config.shape == TargetAreaType.CIRCLE && sx * sx + sy * sy > 1.0) continue;

            // Map to actual radius (controls how much the transmission scatters)
            samples.add(intersection.point.add(intersection.rayDirection
                    .add(u.scale(sx * intersection.material.roughness))
                    .add(v.scale(sy * intersection.material.roughness))
                    .normalize()
                    .scale(this.config.distance)
            ));
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
