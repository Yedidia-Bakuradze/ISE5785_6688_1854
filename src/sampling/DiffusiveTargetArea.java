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
    protected List<Point> getSamplePoints(Intersectable.Intersection intersection) {
        // Calculate the perfect refraction direction using Snell's law (transmission through object)
        Vector T0 = intersection.rayDirection.calcSnellRefraction(intersection.normal, Material.AIR_IOR, intersection.material.ior);

        List<Vector> listOfCoordinates = Vector.getNewCoordinateSystems(T0, Vector.AXIS_X);
        Vector u = listOfCoordinates.get(0);
        Vector v = listOfCoordinates.get(1);

        return switch (this.config.pattern) {
            case SamplingPattern.JITTERED -> generateSamplesJittered(intersection, T0, u, v);
            case SamplingPattern.RANDOM -> generateSamplesRandom(intersection, T0, u, v);
            case SamplingPattern.GRID -> generateSamplesGrid(intersection, T0, u, v);
        };
    }

    /**
     * Generates sample points in a jittered grid pattern around the intersection point.
     *
     * @param intersection The intersection point and material properties
     * @param T0           The transmission vector calculated from Snell's law
     * @param u            The first orthogonal vector in the new coordinate system
     * @param v            The second orthogonal vector in the new coordinate system
     * @return A list of sample points for the diffusive effect
     */
    private List<Point> generateSamplesJittered(Intersectable.Intersection intersection, Vector T0, Vector u, Vector v) {
        List<Point> samples = new ArrayList<>(this.config.mode.numberSamples);
        double sx, sy;

        for (int y = 0; y < this.config.mode.gridValue && samples.size() < this.config.mode.numberSamples; y++) {
            for (int x = 0; x < this.config.mode.gridValue && samples.size() < this.config.mode.numberSamples; x++) {
                sx = (2.0 * (x + this.config.random.nextDouble()) / this.config.mode.gridValue) - 1.0;
                sy = (2.0 * (y + this.config.random.nextDouble()) / this.config.mode.gridValue) - 1.0;

                // Apply shape test
                if (config.shape == TargetAreaType.CIRCLE && sx * sx + sy * sy > 1.0) continue;

                // Map to actual radius (controls how much the transmission scatters)
                samples.add(intersection.point.add(T0
                        .add(u.scale(sx * intersection.material.roughness))
                        .add(v.scale(sy * intersection.material.roughness))
                        .normalize()
                        .scale(this.config.distance)
                ));
            }
        }
        return samples;
    }

    /**
     * Generates sample points in a grid pattern around the intersection point.
     *
     * @param intersection The intersection point and material properties
     * @param T0           The transmission vector calculated from Snell's law
     * @param u            The first orthogonal vector in the new coordinate system
     * @param v            The second orthogonal vector in the new coordinate system
     * @return A list of sample points for the diffusive effect
     */
    private List<Point> generateSamplesGrid(Intersectable.Intersection intersection, Vector T0, Vector u, Vector v) {
        List<Point> samples = new ArrayList<>(this.config.mode.numberSamples);
        double sx, sy;

        for (int y = 0; y < this.config.mode.gridValue && samples.size() < this.config.mode.numberSamples; y++) {
            for (int x = 0; x < this.config.mode.gridValue && samples.size() < this.config.mode.numberSamples; x++) {
                sx = (2.0 * x + 0.5) / this.config.mode.gridValue - 1.0;
                sy = (2.0 * y + 0.5) / this.config.mode.gridValue - 1.0;

                // Apply shape test
                if (config.shape == TargetAreaType.CIRCLE && sx * sx + sy * sy > 1.0) continue;

                // Map to actual radius (controls how much the transmission scatters)
                samples.add(intersection.point.add(T0
                        .add(u.scale(sx * intersection.material.roughness))
                        .add(v.scale(sy * intersection.material.roughness))
                        .normalize()
                        .scale(this.config.distance)
                ));
            }
        }
        return samples;
    }

    /**
     * Generates sample points in a random pattern around the intersection point.
     *
     * @param intersection The intersection point and material properties
     * @param T0           The transmission vector calculated from Snell's law
     * @param u            The first orthogonal vector in the new coordinate system
     * @param v            The second orthogonal vector in the new coordinate system
     * @return A list of sample points for the diffusive effect
     */
    private List<Point> generateSamplesRandom(Intersectable.Intersection intersection, Vector T0, Vector u, Vector v) {
        List<Point> samples = new ArrayList<>(this.config.mode.numberSamples);
        double sx, sy;

        while (samples.size() < this.config.mode.numberSamples) {
            sx = 2.0 * this.config.random.nextDouble() - 1.0;
            sy = 2.0 * this.config.random.nextDouble() - 1.0;

            // Apply shape test
            if (config.shape == TargetAreaType.CIRCLE && sx * sx + sy * sy > 1.0) continue;

            // Map to actual radius (controls how much the transmission scatters)
            samples.add(intersection.point.add(T0
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
        return getSamplePoints(intersection).stream()
                .map(point -> new Ray(intersection.point, point.subtract(intersection.point).normalize(), intersection.normal))
                .toList();
    }

}
