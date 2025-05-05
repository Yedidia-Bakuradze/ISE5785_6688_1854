package renderer;

import primitives.Color;
import primitives.Point;
import primitives.Ray;
import scene.Scene;

public class SimpleRayTracer extends RayTracerBase {

    SimpleRayTracer(Scene scene) {
        super(scene);
    }

    @Override
    public Color traceRay(Ray ray) {
        throw new UnsupportedOperationException("Tray method has been implemented yet in the son's class");
    }

    private Color calcColor(Point p){
        return this.
    }

}
