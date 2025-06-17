package renderer;

import geometries.*;
import lighting.AmbientLight;
import lighting.SpotLight;
import org.junit.jupiter.api.Test;
import primitives.*;
import sampling.*;
import scene.Scene;

import java.util.ArrayList;
import java.util.List;

/**
 * Test class for creating a jug shape using triangular meshes and rendering it with appropriate lighting.
 * This class demonstrates 3D object construction using layers of triangles to form a complex shape.
 */
public class PersonalTest {

    /**
     * Default constructor for PersonalTest class.
     */
    PersonalTest() {
    }

    /**
     * Center point for the jug/teapot geometry in scene coordinates.
     * This is the base point around which the jug is constructed.
     */
    Double3 center = new Double3(400, 0, 100);

    /**
     * The scene object containing all geometries, lighting and settings for rendering.
     */
    Scene scene = new Scene("Personal Test Scene");

    /**
     * Camera builder for setting up the viewpoint and rendering parameters.
     */
    public Camera.Builder cameraBuilder = Camera.getBuilder() //
            .setRayTracer(scene, RayTracerType.SIMPLE);

    /**
     * Total height of the jug/teapot object in scene units.
     */
    double TOTAL_HEIGHT = 500; // Total height of the teapot

    /**
     * Material properties for the middle triangles of the jug.
     * Contains diffuse, specular, shininess, reflection and transparency settings.
     */
    Material midTriangleMaterial = new Material()
            .setKD(0.4)
            .setKS(0.6)
            .setShininess(80)
            .setKR(0.15)
            .setKT(0.4);

    /**
     * Material properties for the top and bottom triangles of the jug.
     * Contains diffuse, specular, shininess and reflection settings.
     */
    Material topBottomTriagnlesMaterial = new Material()
            .setKD(0.4)
            .setKS(0.6)
            .setShininess(80)
            .setKR(0.15);

    /**
     * Color for the middle triangles of the jug.
     */
    Color midTriangleColor = new Color(80, 50, 25);

    /**
     * Color for the top and bottom triangles of the jug.
     */
    Color topBottomTriangleColor = new Color(60, 40, 20);

    /**
     * Test method to generate a teapot-like geometry using layered triangles
     * and render it with appropriate lighting and camera settings.
     */
    @Test
    void Jug() {
        //TODO: Add Spheres
        int[] layerRadiuses = {100, 110, 120, 130, 140, 150, 150, 160, 170, 170, 165, 160, 150, 150, 150, 120, 120};
        // Layer heights
        int[] layerHeights = {0, 25, 50, 100, 125, 150, 175, 200, 225, 250, 275, 300, 325, 350, 375, 425, 450};
        int pointsPerLayer = 15;

        generateTeapotGeometry(center.d1(), center.d2(), center.d3(), layerRadiuses, layerHeights, pointsPerLayer);

        scene.geometries.add(
                new Plane(new Vector(0, 0, 1), new Point(200, 200, 70))
                        .setEmission(new Color(0, 1, 0))
                        .setMaterial(
                                new Material()
                                        .setKR(0.1)
                                        .setKD(0.3)
                                        .setKT(0.1)
                                        .setKS(0.8)
                                        .setShininess(100)
                        ),
                new Plane(new Vector(1, 1, 0), new Point(0, 750, 0)).setMaterial(
                        new Material()
                                .setKD(0.3)
                                .setKS(0.3)
                                .setKT(0.4)
                                .setShininess(100)
                                .setKR(0.1)
                ),
                new Plane(new Vector(1, -1, 0), new Point(0, -750, 0)).setMaterial(
                        new Material()
                                .setKD(0.3)
                                .setKS(0.8)
                                .setShininess(100)
                                .setKR(0.1)
                ),
                new Plane(new Vector(0, 0, 1), new Point(0, 0, 700)).setMaterial(
                        new Material()
                                .setKD(0.3)
                                .setKS(0.8)
                                .setShininess(100)
                                .setKR(0.1)
                )
        );

        // Set up lighting - using SpotLight positioned to create good shadows
        scene.setBackground(new Color(0, 79, 100));
        scene.setAmbientLight(new AmbientLight(new Color(50, 50, 50)));
        scene.lights.add(
                new SpotLight(new Color(800, 600, 400), new Point(200, -200, 400), new Vector(1, 1, -1))
                        .setKl(0.0004)
                        .setKq(0.0000006)
                        .setNarrowBeam(8)
        );
        scene.lights.add(
                new SpotLight(new Color(800, 600, 400), new Point(200, 200, 400), new Vector(1, -1, 0))
                        .setKl(0.0004)
                        .setKq(0.0000006)
                        .setNarrowBeam(100)
        );

        cameraBuilder
                .setLocation(new Point(-1000, 0, 650))
                .setDirection(new Vector(200, 0, -50), new Vector(50, 0, 200))
                .setVpDistance(700)
                .setVpSize(800, 800)
                .setResolution(800, 800)
                .build()
                .renderImage()
                .writeToImage("Jug");
    }

    /**
     * Improved testDiffusiveGlass with darker, more realistic appearance
     */
    @Test
    void testDiffusiveGlass() {
        Material clearGlass = new Material()
                .setKT(0.9)           // High transparency - can see through but blurred
                .setKR(0.1)           // Low reflection like normal glass
                .setKD(0.0)           // No diffuse scattering
                .setKS(0.05)          // Very low specular
                .setShininess(20)
                .setRoughness(0.1)    // Light scattering
                .setIor(1.5);         // Standard glass IOR

        Material mediumBlurGlass = new Material()
                .setKT(0.8)           // Good transparency but more scattering
                .setKR(0.15)          // Slightly more reflection
                .setKD(0.0)           // No diffuse scattering
                .setKS(0.03)          // Very low specular
                .setShininess(15)
                .setRoughness(0.2)    // Medium scattering
                .setIor(1.5);         // Standard glass IOR

        Material strongBlurGlass = new Material()
                .setKT(0.7)           // Reduced transparency due to heavy scattering
                .setKR(0.2)           // More reflection from surface irregularities
                .setKD(0.0)           // No diffuse scattering
                .setKS(0.02)          // Very low specular
                .setShininess(10)
                .setRoughness(0.4)    // Heavy scattering
                .setIor(1.5);         // Standard glass IOR

        // Darker outer sphere material
        Material outerSphereMaterial = new Material()
                .setKD(0.6)      // increased diffuse for less brightness
                .setKS(0.3)      // reduced specular
                .setKR(0.02)     // minimal reflection
                .setKT(0.7)
                .setShininess(50);

        // Darker inner sphere material
        Material innerSphereMaterial = new Material()
                .setKD(0.8)
                .setKS(0.2)      // reduced specular
                .setKR(0.05)
                .setKT(0.0)
                .setShininess(60);

        Material backgroundMaterial = new Material()
                .setKD(0.7)
                .setKS(0.2)      // reduced specular
                .setKR(0.25)     // moderate reflection
                .setShininess(40);

        // Much darker, more subdued colors
        Color darkBlue = new Color(60, 80, 120);        // darker blue
        Color darkWhite = new Color(100, 90, 85);       // darker off-white
        Color darkPink = new Color(100, 70, 85);        // darker pink
        Color darkOrange = new Color(100, 60, 30);      // darker orange
        Color darkGreen = new Color(40, 80, 40);        // darker green
        Color darkPurple = new Color(70, 45, 90);       // darker purple
        Color veryLightGlass = new Color(240, 245, 250); // very subtle glass tint
        Color brightMirrorWhite = new Color(50, 45, 60); // very subtle white for glass panels

        scene.geometries.add(
                // Left sphere pair
                new Sphere(new Point(-400, 0, -500), 120)
                        .setEmission(darkBlue)
                        .setMaterial(outerSphereMaterial),
                new Sphere(new Point(-400, 0, -500), 65)
                        .setEmission(darkOrange)
                        .setMaterial(innerSphereMaterial),

                // Center sphere pair
                new Sphere(new Point(0, 0, -500), 120)
                        .setEmission(darkWhite)
                        .setMaterial(outerSphereMaterial),
                new Sphere(new Point(0, 0, -500), 65)
                        .setEmission(darkGreen)
                        .setMaterial(innerSphereMaterial),

                // Right sphere pair
                new Sphere(new Point(400, 0, -500), 120)
                        .setEmission(darkPink)
                        .setMaterial(outerSphereMaterial),
                new Sphere(new Point(400, 0, -500), 65)
                        .setEmission(darkPurple)
                        .setMaterial(innerSphereMaterial),

                // Glass panels with minimal emission
                // Left panel - clear glass
                new Triangle(new Point(-400, -150, 50), new Point(-200, -150, 50), new Point(-200, 150, 50))
                        .setEmission(brightMirrorWhite)   // no emission for glass
                        .setMaterial(clearGlass),
                new Triangle(new Point(-400, -150, 50), new Point(-400, 150, 50), new Point(-200, 150, 50))
                        .setEmission(brightMirrorWhite)
                        .setMaterial(clearGlass),

                // Center panel - medium blur
                new Triangle(new Point(-80, -150, 40), new Point(80, -150, 40), new Point(80, 150, 40))
                        .setEmission(Color.BLACK)
                        .setMaterial(mediumBlurGlass),
                new Triangle(new Point(-80, -150, 40), new Point(-80, 150, 40), new Point(80, 150, 40))
                        .setEmission(darkGreen)
                        .setMaterial(mediumBlurGlass),

                // Right panel - strong blur
                new Triangle(new Point(200, -150, 50), new Point(300, -150, 50), new Point(300, 150, 50))
                        .setEmission(brightMirrorWhite)
                        .setMaterial(strongBlurGlass),
                new Triangle(new Point(200, -150, 50), new Point(200, 150, 50), new Point(300, 150, 50))
                        .setEmission(brightMirrorWhite)
                        .setMaterial(strongBlurGlass),

                // Darker background plane
                new Plane(new Vector(0, 0, 1), new Point(0, 0, -1000))
                        .setEmission(new Color(25, 25, 30))  // darker background
                        .setMaterial(backgroundMaterial)
        );

        // Much darker lighting setup
        scene.setAmbientLight(new AmbientLight(new Color(15, 18, 22)));  // very low ambient
        scene.setBackground(new Color(8, 10, 12));  // very dark background

        // Reduced light intensities
        scene.lights.add(new SpotLight(new Color(400, 350, 250),  // much dimmer main light
                new Point(-300, -200, 300),
                new Vector(1, 1, -1))
                .setKl(0.0005)     // increased attenuation
                .setKq(0.000002)   // increased attenuation
                .setNarrowBeam(12));

        // Dimmer fill light
        scene.lights.add(new SpotLight(new Color(200, 220, 250),  // dimmer fill
                new Point(300, -200, 250),
                new Vector(-1, 1, -1))
                .setKl(0.0008)
                .setKq(0.000003)
                .setNarrowBeam(18));

        // Very subtle backlight
        scene.lights.add(new SpotLight(new Color(80, 90, 120),    // very dim backlight
                new Point(0, 1200, 0),
                new Vector(0, -1, -0.5))
                .setKl(0.002)
                .setKq(0.000008)
                .setNarrowBeam(30));

        // Same camera setup
        Camera.getBuilder()
                .setLocation(new Point(0, 0, 1000))
                .setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0))
                .setVpDistance(1000)
                .setVpSize(1000, 800)
                .setResolution(1200, 960)
                .enableDiffusiveGlass()
                .setSamplingMode(SamplingMode.EASY)
                .setTargetAreaType(TargetAreaType.CIRCLE)
                .setSamplingPattern(SamplingPattern.JITTERED)
                .setRayTracer(scene, RayTracerType.SIMPLE)
                .build()
                .renderImage()
                .writeToImage("Diffusive Glass Test");
    }

    /**
     * Generate the teapot geometry using the layered approach with triangular mesh.
     *
     * @param x              X-coordinate of the center base point
     * @param y              Y-coordinate of the center base point
     * @param z              Z-coordinate of the center base point
     * @param layerRadiuses  Array of radiuses for each layer of the teapot
     * @param layerHeights   Array of height offsets for each layer
     * @param pointsPerLayer Number of points to generate per layer (more points = smoother surface)
     */
    private void generateTeapotGeometry(double x, double y, double z, int[] layerRadiuses, int[] layerHeights, int pointsPerLayer) {
        Point centerBase = new Point(x, y, z);

        // Generate all layers of points
        List<List<Point>> layers = generateTeapotLayers(x, y, z, layerRadiuses, layerHeights, pointsPerLayer);

        // Create triangular mesh between layers
        createTeapotMesh(layers, centerBase, x, y, z);
    }

    /**
     * Generate all layers of points for the teapot body based on the specified radiuses and heights.
     *
     * @param x              X-coordinate of the center
     * @param y              Y-coordinate of the center
     * @param z              Z-coordinate of the center
     * @param layerRadiuses  Array of radiuses for each layer
     * @param layerHeights   Array of height offsets for each layer
     * @param pointsPerLayer Number of points to generate per layer
     * @return List of layers, where each layer is a list of points forming a circular shape
     * @throws IllegalArgumentException if the number of radiuses doesn't match the number of heights
     */
    private List<List<Point>> generateTeapotLayers(double x, double y, double z, int[] layerRadiuses, int[] layerHeights, int pointsPerLayer) {
        List<List<Point>> layers = new ArrayList<>();

        if (layerRadiuses.length != layerHeights.length) {
            throw new IllegalArgumentException("Number of radiuses must match number of layers");
        }

        for (int i = 0; i < layerHeights.length; i++) {
            int height = layerHeights[i];
            double radius = layerRadiuses[i];

            List<Point> layer = generateCircularLayer(x, y, z, height, radius, pointsPerLayer);
            layers.add(layer);
        }

        return layers;
    }

    /**
     * Generate a circular layer of points around a center point at the specified height.
     *
     * @param centerX      X-coordinate of the center
     * @param centerY      Y-coordinate of the center
     * @param centerZ      Z-coordinate of the center
     * @param heightOffset Vertical offset from the base for this layer
     * @param radius       Radius of the circular layer
     * @param pointCount   Number of points to generate in the circular layer
     * @return List of points forming a circular shape
     */
    private List<Point> generateCircularLayer(double centerX, double centerY, double centerZ, int heightOffset, double radius, int pointCount) {
        List<Point> points = new ArrayList<>();

        for (int i = 0; i < pointCount; i++) {
            double angle = (2 * Math.PI * i) / pointCount;
            double x = centerX + radius * Math.cos(angle);
            double y = centerY + radius * Math.sin(angle);
            double z = centerZ + heightOffset;

            points.add(new Point(x, y, z));
        }

        return points;
    }

    /**
     * Create the triangular mesh for the teapot body by connecting adjacent layers.
     *
     * @param layers     List of layers, each containing points forming a circular shape
     * @param centerBase The center base point of the teapot
     * @param x          X-coordinate of the center
     * @param y          Y-coordinate of the center
     * @param z          Z-coordinate of the center
     */
    private void createTeapotMesh(List<List<Point>> layers, Point centerBase, double x, double y, double z) {
        // Create bottom triangles (first layer to center point)
        createBottomTriangles(layers.getFirst(), centerBase);

        // Create side triangles between adjacent layers
        for (int i = 0; i < layers.size() - 1; i++) {
            createLayerTriangles(layers.get(i), layers.get(i + 1));
        }

        // Create top triangles (last layer to center point)
        Point topCenter = new Point(x, y, z + TOTAL_HEIGHT);
        createTopTriangles(layers.getLast(), topCenter);
    }

    /**
     * Create triangles from the first layer to the bottom center point to form the base of the teapot.
     *
     * @param bottomLayer List of points forming the bottom layer
     * @param centerBase  The center point of the bottom layer
     */
    private void createBottomTriangles(List<Point> bottomLayer, Point centerBase) {
        int pointCount = bottomLayer.size();

        for (int i = 0; i < pointCount; i++) {
            Point p1 = bottomLayer.get(i);
            Point p2 = bottomLayer.get((i + 1) % pointCount);

            // Create triangle with center point
            scene.geometries.add(
                    new Triangle(centerBase, p1, p2)
                            .setEmission(topBottomTriangleColor)
                            .setMaterial(topBottomTriagnlesMaterial)
            );
        }
    }

    /**
     * Create triangles from the last layer to the top center point to form the top of the teapot.
     *
     * @param topLayer  List of points forming the top layer
     * @param topCenter The center point of the top layer
     */
    private void createTopTriangles(List<Point> topLayer, Point topCenter) {
        int pointCount = topLayer.size();

        for (int i = 0; i < pointCount; i++) {
            Point p1 = topLayer.get(i);
            Point p2 = topLayer.get((i + 1) % pointCount);

            // Create triangle with center point
            scene.geometries.add(
                    new Triangle(topCenter, p2, p1)
                            .setEmission(topBottomTriangleColor)
                            .setMaterial(topBottomTriagnlesMaterial)
            );
        }
    }

    /**
     * Create triangular mesh between two adjacent layers to form the sides of the teapot.
     * Each quad between adjacent layers is divided into two triangles.
     *
     * @param lowerLayer List of points forming the lower layer
     * @param upperLayer List of points forming the upper layer
     */
    private void createLayerTriangles(List<Point> lowerLayer, List<Point> upperLayer) {
        int pointCount = lowerLayer.size(); // Assuming both layers have same number of points

        for (int i = 0; i < pointCount; i++) {
            Point p1 = lowerLayer.get(i);
            Point p2 = lowerLayer.get((i + 1) % pointCount);
            Point p3 = upperLayer.get(i);
            Point p4 = upperLayer.get((i + 1) % pointCount);

            // Create two triangles to form a quad between the layers
            // Triangle 1: p1, p2, p3
            scene.geometries.add(
                    new Triangle(p1, p2, p3)
                            .setEmission(midTriangleColor)
                            .setMaterial(midTriangleMaterial)

            );

            // Triangle 2: p2, p4, p3
            scene.geometries.add(
                    new Triangle(p2, p4, p3)
                            .setEmission(midTriangleColor)
                            .setMaterial(midTriangleMaterial)
            );
        }
    }
}

