package sampling;

import geometries.Intersectable;
import primitives.Ray;

import java.util.List;

public class DiffuseGlassBeamGenerator implements BeamGenerator {
    @Override
    public List<Ray> generateRays(Ray centerRay, Intersectable.Intersection intersection) {
        return List.of();
    }
}
