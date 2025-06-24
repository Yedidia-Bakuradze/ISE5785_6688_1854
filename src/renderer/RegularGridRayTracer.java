package renderer;

import acceleration.RegularGridConfiguration;
import primitives.Color;
import primitives.Ray;
import sampling.TargetAreaBase;
import scene.Scene;

import java.util.Map;

public class RegularGridRayTracer extends RayTracerBase {
    private final RayTracerBase secondaryRayTracer;

    private final RegularGridConfiguration configuration;

    public RegularGridRayTracer(Scene scene, RegularGridConfiguration configuration) {
        super(scene);
        this.secondaryRayTracer = new SimpleRayTracer(scene);
        this.configuration = configuration;
    }

    public RegularGridRayTracer(Scene scene, RegularGridConfiguration configuration, Map<EffectType, TargetAreaBase> targetArea) {
        super(scene);
        this.secondaryRayTracer = new ExtendedRayTracer(scene, targetArea);
        this.configuration = configuration;
    }

    @Override
    public Color traceRay(Ray ray) {
        return null; //TODO: Implement the ray tracing logic using the regular grid configuration
    }
}
