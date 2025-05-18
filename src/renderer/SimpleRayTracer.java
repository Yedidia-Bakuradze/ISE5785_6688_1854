package renderer;

import geometries.Intersectable;
import lighting.LightSource;
import primitives.Color;
import primitives.Double3;
import primitives.Ray;
import primitives.Vector;
import scene.Scene;

import java.util.List;

import static primitives.Util.alignZero;

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
                .add(calcColorLocalEffects(intersection));
    }


    private boolean preprocessIntersection(Intersectable.Intersection intersection, Vector rayDirection) {
        intersection.rayDirection = rayDirection;
        intersection.normal = intersection.geometry.getNormal(intersection.point);
        intersection.rayNormalProduct = alignZero(intersection.rayDirection.dotProduct(intersection.normal));
        return intersection.rayNormalProduct != 0;
    }

    private boolean setLightSource(Intersectable.Intersection intersection, LightSource lightSource) {
        intersection.lightSource = lightSource;
        intersection.lightDirection = lightSource.getL(intersection.point);
        intersection.lightNormalProduct = alignZero(intersection.lightDirection.dotProduct(intersection.normal));
        return intersection.lightNormalProduct * intersection.rayNormalProduct >= 0;
    }

    Color calcColorLocalEffects(Intersectable.Intersection intersection) {
        Color color = intersection.geometry.getEmission();
        for (LightSource lightSource : scene.lights) {
            if (intersection.lightNormalProduct * intersection.rayNormalProduct > 0) { // sign(nl) == sign(nv)
                Color iL = lightSource.getIntensity(intersection.point);
                color = color.add(
                        iL.scale(calcDiffusive(intersection)
                                .add(calcSpecular(intersection))));
            }
        }
        return color;
    }

    Double3 calcSpecular(Intersectable.Intersection intersection) {
        double factor = -intersection.rayNormalProduct <= 0 ? 0 : intersection.rayNormalProduct;
        factor = Math.pow(factor, intersection.material.nShininess);
        return intersection.material.kS.scale(factor);
    }

    Double3 calcDiffusive(Intersectable.Intersection intersection) {
        return intersection.material.kD.scale(intersection.lightNormalProduct);
    }
}
