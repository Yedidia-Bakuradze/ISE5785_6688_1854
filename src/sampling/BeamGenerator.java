package sampling;

import geometries.Intersectable;
import primitives.Ray;

import java.util.List;

public interface BeamGenerator {

    List<Ray> generateRays(Ray centerRay, Intersectable.Intersection intersection);
}
