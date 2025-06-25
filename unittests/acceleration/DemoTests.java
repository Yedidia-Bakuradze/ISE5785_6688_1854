package acceleration;

import geometries.*;
import lighting.DirectionalLight;
import lighting.SpotLight;
import org.junit.jupiter.api.Test;
import primitives.*;
import renderer.*;
import sampling.*;
import scene.Scene;

import static java.lang.Math.*;
import static primitives.Util.random;

public class DemoTests {

    /**
     * Scene for the tests
     */
    private final Scene scene = new Scene("Test scene");
    /**
     * Camera builder for the tests with triangles
     */
    private final Camera.Builder cameraBuilder = Camera.getBuilder();     //

    /**
     * Produce a picture of a poke ball with stars in the background
     * with spotlights
     */
    @Test
    void pokeball() {
        scene.geometries.add(
                new Plane(new Vector(1, 0, 0), new Point(500, 0, 0)).setEmission(new Color(0, 0, 0))
                        .setMaterial(new Material().setKD(new Double3(0d, 0d, 0.3d)).setKS(0).setRoughness(0.04).setShininess(30).setKR(0)),
                new Plane(Vector.AXIS_Z, new Point(0, 0, -200))
                        .setMaterial(new Material().setKD(new Double3(0.4d, 0.75d, 1d)).setKS(0).setRoughness(0.04).setShininess(30).setKR(0.45)),
                new Sphere(new Point(0, 0, -0.001), 5d) //
                        .setMaterial(new Material().setKD(1).setKS(1).setShininess(300).setKT(0).setRoughness(0.04).setKR(0.1)), //
                new Sphere(new Point(0, 0, 0.001), 5d) //
                        .setMaterial(new Material().setKD(new Double3(0.7d, 0.1d, 0.1d)).setKS(2).setRoughness(0.04).setShininess(60).setKT(0).setKR(0.1)),
                new Sphere(new Point(0, 0, 0), 5.00005d) //
                        .setMaterial(new Material().setKD(0).setKS(0).setShininess(0).setRoughness(0.04).setKT(0).setKR(0)),
                new Sphere(new Point(-4.5, 0, 0), 1d) //
                        .setMaterial(new Material().setKD(0).setKS(0).setShininess(100).setRoughness(0.04).setKT(0)),
                new Sphere(new Point(-5, 0, 0), 0.63d) //
                        .setMaterial(new Material().setKD(1).setKS(0).setShininess(100).setRoughness(0.04).setKT(0)),
                new Sphere(new Point(-5.7, 0, 0), 0.35d) //
                        .setMaterial(new Material().setKD(new Double3(1.2, 0.6d, 0.6d)).setKS(0).setRoughness(0.04).setShininess(400).setKT(0)));
        scene.lights.add(new SpotLight(new Color(400, 400, 800), new Point(10, 0, -50), new Vector(1, 0, -1.7))
                .setKl(10E-50).setKq(10E-10));
        scene.lights.add(new SpotLight(new Color(250, 250, 250), new Point(-5, 4, 4), new Vector(5, -5, -5))
                .setKl(0.1).setKq(2E-10));
        scene.lights.add(new DirectionalLight(new Color(200, 200, 200), new Vector(1, 0, 0)));

        cameraBuilder
                .setGridConfiguration(AccelerationMode.PERFORMANCE)
                .setEffect(EffectType.DIFFUSIVE_GLASS, new SamplingConfiguration(SamplingMode.MEDIUM, TargetAreaType.CIRCLE, SamplingPattern.JITTERED, 0.5))
                .setRegularGrid(new RegularGrid(scene, RegularGridConfiguration.Factory.createConfiguration(AccelerationMode.PERFORMANCE)))
                .setRayTracer(scene, RayTracerType.EXTENDED)
                .setLocation(new Point(-10, 0, 0)) //
                .setMultithreading(-2)
                .setDirection(Point.ZERO, Vector.AXIS_Z) //
                .setVpDistance(4).setVpSize(16, 9) //
                .setResolution(1920, 1080) //
                .build() //
                .renderImage() //
                .writeToImage("pokeball");
    }

    /**
     * the field is used for creating the triangles
     */
    private static Geometries triangles;
    /**
     * the field is used for creating the stars
     */
    private static Geometries stars;
    /**
     * the field is used for creating the stars
     */
    private static double cylinderX;
    /**
     * the field is used for creating the stars
     */
    private static double cylinderY;
    /**
     * the field is used for creating the stars
     */
    private static double cylinderZ;
    /**
     * the field is used for creating the stars
     */
    private static double radius;
    /**
     * the field is used for creating the stars
     */
    private static double maxCylinderLength;
    /**
     * the field is used for creating the stars
     */
    private static double minStarRadius;
    /**
     * the field is used for creating the stars
     */
    private static double maxStarRadius;
    /**
     * the field is used for creating the stars
     */
    private static Double3 ks;
    /**
     * the field is used for creating the stars
     */
    private static Double3 kt;
    /**
     * the field is used for creating the stars
     */
    private static Double3 kr;
    /**
     * the field is used for creating the stars
     */
    private static double starMinKD;
    /**
     * the field is used for creating the stars
     */
    private static double starMaxKD;

    /**
     * Set the cone for generating stars
     *
     * @param x         X coordinate for the cone top
     * @param y         Y coordinate for the cone top
     * @param z         Z coordinate for the cone top
     * @param r         the radius of the cone at height = 1
     * @param maxlength maximum height in the cone for generating the stars
     */
    private static void setCylinder(double x, double y, double z, double r, double maxlength) {
        cylinderX = x;
        cylinderY = y;
        cylinderZ = z;
        radius = r;
        maxCylinderLength = maxlength;
    }

    /**
     * Setter for minimal and maximal size of the stars
     *
     * @param min minimal size
     * @param max maximal size
     */
    private static void setStarSize(double min, double max) {
        minStarRadius = min;
        maxStarRadius = max;
    }

    /**
     * Setter for stars material parameters
     *
     * @param minKD minimal diffusive factor
     * @param maxKD maximal diffusive factor
     * @param ks    specular factor
     * @param kr    reflection factor
     * @param kt    transparency factor
     */
    private static void setStarMaterial(double minKD, double maxKD, double ks, double kr, double kt) {
        starMinKD = minKD;
        starMaxKD = maxKD;
        DemoTests.ks = new Double3(ks);
        DemoTests.kr = new Double3(kr);
        DemoTests.kt = new Double3(kt);
    }

    /**
     * Generate a star
     *
     * @param y the height in cone for the center of the star
     * @return the star sphere
     */
    private static Sphere getRandomStar(double y) {
        double coneR = radius;
        double randomR = random(0, coneR);
        double angle = random(0, 2 * PI);
        double z = randomR * cos(angle);
        double x = randomR * sin(angle);
        Point o = new Point(cylinderX + x, cylinderY + y, cylinderZ + z);
        double r = random(minStarRadius, maxStarRadius);
        Double3 kd = new Double3(1,                            //
                1,                            //
                1);
        var material = new Material().setKD(0).setKS(ks).setKR(kr).setKT(kt).setRoughness(0.04);
        return (Sphere) new Sphere(o, r).setMaterial(material).setEmission(new Color(255, 255, 255));
    }

    /**
     * Generate a triangle
     *
     * @param y the height in cone for the center of the triangle
     * @return the triangle
     */
    private static Triangle getRandomTriangle(double y) {
        double coneR = radius;
        double randomR = random(0, coneR);
        double angle = random(0, 2 * PI);
        double z = randomR * cos(angle);
        double x = randomR * sin(angle);
        Point a = new Point(cylinderX + x, cylinderY + y, cylinderZ + z);
        Point b = new Point(cylinderX + x + random(1, 5), cylinderY + y + random(-5, 5), cylinderZ + z + random(-3, 3));
        Point c = new Point(cylinderX + x + random(1, 5), cylinderY + y + random(-2, 2), cylinderZ + z + random(-3, 3));
        Double3 kd = new Double3(random(0, 0.3),                            //
                random(0, 0.3),                            //
                random(0.6, 1));
        var material = new Material().setKD(kd).setKS(ks).setKR(kr).setKT(kt);
        return (Triangle) new Triangle(a, b, c).setMaterial(material);
    }

    /**
     * Create a composite geometry containing the stars (generate the stars)
     *
     * @param amount amount of stars to generate
     */
    private static void prepareStars(int amount) {
        stars = new Geometries();
        while (amount-- > 0) {
            stars.add(getRandomStar(random(0, maxCylinderLength)));
        }
    }

    /**
     * Create a composite geometry containing the triangles (generate the triangles)
     *
     * @param amount amount of triangles to generate
     */
    private static void prepareTriangles(int amount) {
        triangles = new Geometries();
        while (amount-- > 0) {
            triangles.add(getRandomTriangle(random(0, maxCylinderLength)));
        }
    }
}
