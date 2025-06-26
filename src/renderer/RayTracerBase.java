package renderer;

import primitives.Color;
import primitives.Ray;
import scene.Scene;

/**
 * An abstract base class for ray tracers.
 */
public abstract class RayTracerBase {

    /**
     * The scene to be rendered.
     */
    final protected Scene scene;

    /**
     * Constructs a RayTracerBase with the specified scene.
     *
     * @param scene The scene to be rendered.
     */
    RayTracerBase(Scene scene) {
        this.scene = scene;
    }

    /**
     * Traces a ray and calculates the color at the intersection point.
     *
     * @param ray The ray to trace.
     * @return The color at the intersection point.
     */
    public abstract Color traceRay(Ray ray);
    
}
