package renderer;

import geometries.Sphere;
import geometries.Triangle;
import lighting.AmbientLight;
import lighting.SpotLight;
import org.junit.jupiter.api.Test;
import primitives.*;
import sampling.*;
import scene.Scene;

import static java.awt.Color.*;

/**
 * Tests for reflection and transparency functionality, test for partial
 * shadows
 * (with transparency)
 *
 * @author Dan Zilberstein
 */
class ReflectionRefractionTests {
    /**
     * Default constructor to satisfy JavaDoc generator
     */
    ReflectionRefractionTests() { /* to satisfy JavaDoc generator */ }

    /**
     * Scene for the tests
     */
    private final Scene scene = new Scene("Test scene");
    /**
     * Camera builder for the tests with triangles
     */
    private final Camera.Builder cameraBuilder = Camera.getBuilder()     //
            .setRayTracer(scene, RayTracerType.SIMPLE);

    /**
     * Produce a picture of a sphere lighted by a spotlight
     */
    @Test
    void twoSpheres() {
        scene.geometries.add( //
                new Sphere(new Point(0, 0, -50), 50d).setEmission(new Color(BLUE)) //
                        .setMaterial(new Material().setKD(0.4).setKS(0.3).setShininess(100).setKT(0.3)), //
                new Sphere(new Point(0, 0, -50), 25d).setEmission(new Color(RED)) //
                        .setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(100))); //
        scene.lights.add( //
                new SpotLight(new Color(1000, 600, 0), new Point(-100, -100, 500), new Vector(-1, -1, -2)) //
                        .setKl(0.0004).setKq(0.0000006));

        cameraBuilder
                .setLocation(new Point(0, 0, 1000)) //
                .setDirection(Point.ZERO, Vector.AXIS_Y) //
                .setVpDistance(1000).setVpSize(150, 150) //
                .setResolution(500, 500) //
                .build() //
                .renderImage() //
                .writeToImage("refractionTwoSpheres");
    }

    /**
     * Produce a picture of a sphere lighted by a spotlight
     */
    @Test
    void twoSpheresOnMirrors() {
        scene.geometries.add( //
                new Sphere(new Point(-950, -900, -1000), 400d).setEmission(new Color(0, 50, 100)) //
                        .setMaterial(new Material().setKD(0.25).setKS(0.25).setShininess(20) //
                                .setKT(new Double3(0.5, 0, 0))), //
                new Sphere(new Point(-950, -900, -1000), 200d).setEmission(new Color(100, 50, 20)) //
                        .setMaterial(new Material().setKD(0.25).setKS(0.25).setShininess(20)), //
                new Triangle(new Point(1500, -1500, -1500), new Point(-1500, 1500, -1500), //
                        new Point(670, 670, 3000)) //
                        .setEmission(new Color(20, 20, 20)) //
                        .setMaterial(new Material().setKR(1)), //
                new Triangle(new Point(1500, -1500, -1500), new Point(-1500, 1500, -1500), //
                        new Point(-1500, -1500, -2000)) //
                        .setEmission(new Color(20, 20, 20)) //
                        .setMaterial(new Material().setKR(new Double3(0.5, 0, 0.4))));
        scene.setAmbientLight(new AmbientLight(new Color(26, 26, 26)));
        scene.lights.add(new SpotLight(new Color(1020, 400, 400), new Point(-750, -750, -150), new Vector(-1, -1, -4)) //
                .setKl(0.00001).setKq(0.000005));

        cameraBuilder
                .setLocation(new Point(0, 0, 10000)) //
                .setDirection(Point.ZERO, Vector.AXIS_Y) //
                .setVpDistance(10000).setVpSize(2500, 2500) //
                .setResolution(500, 500) //
                .build() //
                .renderImage() //
                .writeToImage("reflectionTwoSpheresMirrored");
    }

    /**
     * Produce a picture of two triangles lighted by a spotlight with a
     * partially
     * transparent Sphere producing partial shadow
     */
    @Test
    void trianglesTransparentSphere() {
        scene.geometries.add(
                new Triangle(new Point(-150, -150, -115), new Point(150, -150, -135),
                        new Point(75, 75, -150))
                        .setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(60)),
                new Triangle(new Point(-150, -150, -115), new Point(-70, 70, -140), new Point(75, 75, -150))
                        .setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(60)),
                new Sphere(new Point(60, 50, -50), 30d).setEmission(new Color(BLUE))
                        .setMaterial(new Material().setKD(0.2).setKS(0.2).setShininess(30).setKT(0.6)));
        scene.setAmbientLight(new AmbientLight(new Color(38, 38, 38)));
        scene.lights.add(
                new SpotLight(new Color(700, 400, 400), new Point(60, 50, 0), new Vector(0, 0, -1))
                        .setKl(4E-5).setKq(2E-7));

        cameraBuilder
                .setLocation(new Point(0, 0, 1000)) //
                .setDirection(Point.ZERO, Vector.AXIS_Y) //
                .setVpDistance(1000).setVpSize(200, 200) //
                .setResolution(600, 600) //
                .build() //
                .renderImage() //
                .writeToImage("refractionShadow");
    }

    /**
     * Produce a picture of two triangles lighted by a spotlight with a
     * partially
     * transparent Sphere producing partial shadow
     */
    @Test
    void testDualMirrorRefractionShadow() {
        // https://www.geogebra.org/calculator/jxhczbc2
        scene.geometries.add(
                // Outer transparent sphere (glass)
                new Sphere(new Point(0, 0, -70), 60d)
                        .setEmission(new Color(0, 0, 100))
                        .setMaterial(new Material()
                                .setKD(0.2).setKS(0.3).setShininess(100)
                                .setKT(0.6).setIOR(1000)),

                // Inner solid sphere (red center)
                new Sphere(new Point(0, 0, -70), 30d)
                        .setEmission(new Color(0, 0, 0))
                        .setMaterial(new Material()
                                .setKD(0.4).setKS(0.3).setShininess(50)),

                // Mirror Reflects to Camera
                new Triangle(
                        new Point(150, -150, -200),
                        new Point(200, 150, -200),
                        new Point(-50, 150, -250))
                        .setEmission(new Color(10, 10, 10))
                        .setMaterial(new Material().setKR(1).setKD(0.2).setKS(0.3).setShininess(50)),

                // Mirror Reflects to Mirror
                new Triangle(
                        new Point(-150, -150, -300),
                        new Point(-200, 100, -250),
                        new Point(-50, 150, -250))
                        .setEmission(new Color(30, 30, 30))
                        .setMaterial(new Material().setKR(1)),

                // Floor plane (to show shadow)
                new Triangle(new Point(-300, 20, -500), new Point(300, 20, -500), new Point(0, -180, 200))
                        .setEmission(new Color(0, 0, 100))
                        .setMaterial(new Material().setKD(0.6).setKS(0.2).setShininess(30))
        );

        // Ambient light to see shadow properly
        scene.setAmbientLight(new AmbientLight(new Color(40, 40, 40)));
        scene.setBackground(new Color(40, 40, 40));
        // Spotlight aimed at the center sphere (simulate sun/spotlight)
        scene.lights.add(new SpotLight(new Color(700, 400, 400),
                new Point(100, 100, 200),
                new Vector(-1, -1, -2))
                .setKl(0.0005).setKq(0.00005));

        // Camera setup
        Camera.getBuilder()
                .setLocation(new Point(0, 0, 1000)) // looking forward
                .setDirection(Point.ZERO, new Vector(0, 1, 0)) // looking down -Z
                .setVpDistance(800)
                .setVpSize(250, 250)
                .setResolution(600, 600)
                .setRayTracer(scene, RayTracerType.SIMPLE)
                .build()
                .renderImage()
                .writeToImage("testDualMirrorRefractionShadow");
    }

    /**
     * Produce a picture of two triangles lighted by a spotlight with a
     * partially
     * transparent Sphere producing partial shadow
     */
    @Test
    void testDiffusiveGlass() {
        // https://www.geogebra.org/calculator/jxhczbc2
        scene.geometries.add(
                // Outer transparent sphere (glass)
                new Triangle(new Point(-30, -30, 70), new Point(30, -30, 70), new Point(30, 30, 70))
                        .setEmission(new Color(0, 40, 0))
                        .setMaterial(new Material()
                                .setKD(0.2).setKS(0.3).setShininess(100)
                                .setKT(0.6)),

                new Triangle(new Point(30, 30, 70), new Point(-30, 30, 70), new Point(-30, -30, 70))
                        .setEmission(new Color(0, 0, 40))
                        .setMaterial(new Material()
                                .setKD(0.2).setKS(0.3).setShininess(100)
                                .setKT(0.6).setIOR(0.1)),

                // Inner solid sphere (red center)
                new Sphere(new Point(0, 0, -200), 20d)
                        .setEmission(new Color(5, 3, 20))
                        .setMaterial(new Material()
                                .setKD(0.4).setKS(0.3).setShininess(50)),

                // Mirror Reflects to Camera
                new Triangle(
                        new Point(150, -150, -200),
                        new Point(200, 150, -200),
                        new Point(-50, 150, -250))
                        .setEmission(new Color(10, 10, 10))
                        .setMaterial(new Material().setKR(1).setKD(0.2).setKS(0.3).setShininess(50)),

                // Mirror Reflects to Mirror
                new Triangle(
                        new Point(-150, -150, -300),
                        new Point(-200, 100, -250),
                        new Point(-50, 150, -250))
                        .setEmission(new Color(30, 30, 30))
                        .setMaterial(new Material().setKR(1)),

                // Floor plane (to show shadow)
                new Triangle(new Point(-300, 20, -500), new Point(300, 20, -500), new Point(0, -180, 200))
                        .setEmission(new Color(0, 0, 100))
                        .setMaterial(new Material().setKD(0.6).setKS(0.2).setShininess(30))
        );

        // Ambient light to see shadow properly
        scene.setAmbientLight(new AmbientLight(new Color(40, 40, 40)));
        scene.setBackground(new Color(40, 40, 40));
        // Spotlight aimed at the center sphere (simulate sun/spotlight)
        scene.lights.add(new SpotLight(new Color(700, 400, 400),
                new Point(100, 100, 200),
                new Vector(-1, -1, -2))
                .setKl(0.0005).setKq(0.00005));

        // Camera setup
        Camera.getBuilder()
                .setLocation(new Point(0, 0, 1000)) // looking forward
                .setDirection(Point.ZERO, new Vector(0, 1, 0)) // looking down -Z
                .setVpDistance(800)
                .setVpSize(250, 250)
                .setResolution(600, 600)
                .enableDiffusiveGlass()
                .setTargetArea(RayBeamSpreadingMode.JITTER, SuperSamplingMode.DEMO, TargetAreaShape.CIRCLE, 0.5)
                .setRayTracer(scene, RayTracerType.SIMPLE)
                .build()
                .renderImage()
                .writeToImage("DiffusiveGlassTest");
    }

    /**
     * Produce a picture of colored spheres behind a white semi-reflective wall
     * demonstrating diffusive glass/mirror effect
     */
    @Test
    void whiteBlurryMirrorTest() {
        // Set up a clean scene
        Scene testScene = new Scene("White Blurry Mirror Test");

        // Add geometries
        testScene.geometries.add(
                // Semi-transparent white wall with reflective properties
                new Triangle(
                        new Point(-150, -150, 50),
                        new Point(150, -150, 50),
                        new Point(0, 150, 50))
                        .setEmission(new Color(200, 200, 200))
                        .setMaterial(new Material()
                                .setKD(0.2).setKS(0.3).setShininess(50)
                                .setKT(0.3).setKR(0.4)), // Partial transparency and reflection

                // Colored spheres positioned behind the wall
                new Sphere(new Point(-50, 0, -100), 30d)
                        .setEmission(new Color(200, 30, 30)) // Red sphere
                        .setMaterial(new Material()
                                .setKD(0.6).setKS(0.4).setShininess(100)),

                new Sphere(new Point(50, 0, -100), 30d)
                        .setEmission(new Color(30, 30, 200)) // Blue sphere
                        .setMaterial(new Material()
                                .setKD(0.6).setKS(0.4).setShininess(100)),

                new Sphere(new Point(0, 70, -100), 25d)
                        .setEmission(new Color(30, 200, 30)) // Green sphere
                        .setMaterial(new Material()
                                .setKD(0.6).setKS(0.4).setShininess(100)),

                // Floor plane to show shadows
                new Triangle(
                        new Point(-200, -100, -200),
                        new Point(200, -100, -200),
                        new Point(0, -100, 100))
                        .setEmission(new Color(80, 80, 100))
                        .setMaterial(new Material()
                                .setKD(0.6).setKS(0.2).setShininess(30))
        );

        // Set background color and ambient light
        testScene.setBackground(new Color(50, 50, 50));
        testScene.setAmbientLight(new AmbientLight(new Color(30, 30, 30)));

        // Add light sources - REDUCED INTENSITY AND MOVED FARTHER AWAY
        testScene.lights.add(
                new SpotLight(new Color(40, 40, 40),
                        new Point(-200, 200, 500), // Moved farther away
                        new Vector(1, -1, -2))
                        .setKl(0.0001).setKq(0.0000002)); // Reduced attenuation

        testScene.lights.add(
                new SpotLight(new Color(40, 40, 40),
                        new Point(200, 100, 500), // Moved farther away
                        new Vector(-1, 0, -2))
                        .setKl(0.0001).setKq(0.0000002)); // Reduced attenuation

        // Camera setup with diffusive glass effect
        Camera.getBuilder()
                .setLocation(new Point(0, 0, 300))
                .setDirection(new Point(0, 0, 0), Vector.AXIS_Y)
                .setVpDistance(200)
                .setVpSize(200, 200)
                .setResolution(800, 800)
                .enableDiffusiveGlass()
                .setTargetArea(RayBeamSpreadingMode.JITTER, SuperSamplingMode.DEMO, TargetAreaShape.CIRCLE, 0.7)
                .setRayTracer(testScene, RayTracerType.SIMPLE)
                .build()
                .renderImage()
                .writeToImage("whiteBlurryMirrorTest");
    }
}