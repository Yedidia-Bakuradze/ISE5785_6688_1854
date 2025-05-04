package renderer;

import primitives.Color;
import primitives.Ray;
import scene.Scene;

public abstract class RayTracerBase {

    final private Scene scene;

    RayTracerBase(Scene _scene){
        scene = _scene;
    }

    //Returns the intensity of the cast ray
    public abstract Color traceRay(Ray ray);
}
