package sampling;

import geometries.Intersectable;
import primitives.Ray;

import java.util.List;

public class NoSamplingStrategy implements SamplingStrategy {
    @Override
    public List<Ray> generateRays(Ray primaryRay, Intersectable.Intersection hit) {
        return List.of(primaryRay);
    }
}
