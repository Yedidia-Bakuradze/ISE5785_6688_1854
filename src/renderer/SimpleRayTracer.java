package renderer;

import primitives.Color;
import primitives.Point;
import primitives.Ray;
import scene.Scene;

import java.util.List;

/**
 * Represents a type of ray tracer which is the simplest and most basic ray
 */
public class SimpleRayTracer extends RayTracerBase {

    /**
     * Initiates the simple ray tracer with a scene containing the objects
     *
     * @param scene the scene which contains the geometries that are a part of that image and scene
     */
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

    /**
     * Calculates the color on the given point
     * TODO: Needed to be implemented, for now it returns the ambient light
     *
     * @param p the point onto the calculation is made
     * @return the calculated color to paint that pixel with
     */
    private Color calcColor(Point p) {
        return this.scene.ambientLight.getIntensity();
    }

}
