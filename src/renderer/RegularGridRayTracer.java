package renderer;

import sampling.TargetAreaBase;
import scene.Scene;

import java.util.Map;

public class RegularGridRayTracer extends RayTracerBase {
    private final RayTracerBase secondaryRayTracer;

    private final RegularGridConfiguration configuration;

    RegularGridRayTracer(Scene scene, RegularGridConfiguration configuration) {
        super(scene);
        this.configuration = configuration;
    }

    RegularGridRayTracer(Scene scene, RegularGridConfiguration configuration, Map<EffectType, TargetAreaBase> targetArea) {
        super(scene);
        this.secondaryRayTracer = new ExtendedRayTracer(scene, targetArea);
        this.configuration = new RegularGridConfiguration();
    }
}
