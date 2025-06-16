package sampling;

import geometries.Intersectable;
import primitives.Ray;

import java.util.List;

public abstract class BeamGenerator {

    public RayBeamSpreadingMode spreadingMode = RayBeamSpreadingMode.JITTER;
    public SuperSamplingMode superSamplingMode = SuperSamplingMode.EASY;
    public TargetAreaShape shape = TargetAreaShape.CIRCLE;
    protected double radius = 1.0;              // physical blur radius

    public BeamGenerator setSpreadingMode(RayBeamSpreadingMode spreadingMode) {
        this.spreadingMode = spreadingMode;
        return this;
    }

    public BeamGenerator setSamplingMode(SuperSamplingMode superSamplingMode) {
        this.superSamplingMode = superSamplingMode;
        return this;
    }

    public BeamGenerator setShape(TargetAreaShape shape) {
        this.shape = shape;
        return this;
    }

    public BeamGenerator setRadius(double radius) {
        this.radius = radius;
        return this;
    }

    public abstract List<Ray> generateRays(Ray centerRay, Intersectable.Intersection intersection);
}
