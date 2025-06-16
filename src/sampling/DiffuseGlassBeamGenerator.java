package sampling;

import geometries.Intersectable;
import primitives.*;

import java.util.ArrayList;
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
    public BeamGenerator setRadius(double radius) {
        return (DiffuseGlassBeamGenerator) super.setRadius(radius);
    }

    @Override
    public List<Ray> generateRays(Ray centerRay, Intersectable.Intersection intersection) {
        // 1. Build sampler
        TargetArea sampler = new TargetArea(spreadingMode, superSamplingMode, shape, radius);
        List<Point> samples2D = sampler.getSamples();

        // 2. Compute main refracted direction via Snell's law
        Vector normal = intersection.normal;
        double ior = intersection.material.ior;
        Vector refractedDir = centerRay.getDirection().refract(normal, ior);

        // 3. Build orthonormal basis (u,v) on tangent plane
        Vector u = normal.anyPerpendicular().normalize();
        Vector v = normal.crossProduct(u).normalize();

        // 4. Generate perturbed rays
        List<Ray> rays = new ArrayList<>();
        for (Point p : samples2D) {
            // offset in tangent plane
            Vector offset = u.scale(p.getX()).add(v.scale(p.getY()));
            Vector dir = refractedDir.add(offset).normalize();
            // filter rays pointing into the surface
            if (dir.dotProduct(normal) > 0) {
                rays.add(new Ray(intersection.point, dir));
            }
        }
        return rays;
    }
}
