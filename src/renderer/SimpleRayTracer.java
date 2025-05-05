package renderer;

import primitives.Color;
import primitives.Point;
import primitives.Ray;
import scene.Scene;

import java.util.List;

public class SimpleRayTracer extends RayTracerBase {

    SimpleRayTracer(Scene scene) {
        super(scene);
    }

    @Override
    public Color traceRay(Ray ray) {
        List<Point> listOfIntersections = scene.geometries.findIntersections(ray);
        return listOfIntersections != null
                ? calcColor(ray.findClosestPoint(listOfIntersections))
                : scene.backgroundColor;
    }

    private Color calcColor(Point p){
        return this.scene.ambientLight.getIntensity();
    }

}
