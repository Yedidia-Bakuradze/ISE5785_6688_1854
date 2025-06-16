package sampling;

import geometries.Intersectable;
import primitives.Ray;

import java.util.List;

public class DiffuseGlassBeamGenerator extends BeamGenerator {

    @Override
    public DiffuseGlassBeamGenerator setSpreadingMode(RayBeamSpreadingMode spreadingMode) {
        return (DiffuseGlassBeamGenerator) super.setSpreadingMode(spreadingMode);
    }

    @Override
    public DiffuseGlassBeamGenerator setSamplingMode(SuperSamplingMode superSamplingMode) {
        return (DiffuseGlassBeamGenerator) super.setSamplingMode(superSamplingMode);
    }

    @Override
    public DiffuseGlassBeamGenerator setShape(TargetAreaShape shape) {
        return (DiffuseGlassBeamGenerator) super.setShape(shape);
    }

    @Override
    public List<Ray> generateRays(Ray centerRay, Intersectable.Intersection intersection) {
        return List.of();
    }
}
