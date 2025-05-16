package renderer;

import geometries.Intersectable;
import primitives.Color;
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
        List<Intersectable.Intersection> listOfIntersections = scene.geometries.calculateIntersections(ray);
        return listOfIntersections != null
                ? calcColor(ray.findClosestIntersection(listOfIntersections))
                : scene.backgroundColor;
    }

    /**
     * Calculates the color on the given point with the ambient light and the emission of the geometry
     *
     * @param intersection the intersection onto the calculation is made
     * @return the calculated color to paint that pixel with
     */
    private Color calcColor(Intersectable.Intersection intersection) {
        return this.scene.ambientLight.getIntensity()
                .add(intersection.geometry.getEmission()).scale(intersection.geometry.getMaterial().ka);
    }

}
