package renderer;

import geometries.*;
import lighting.AmbientLight;
import lighting.SpotLight;
import org.junit.jupiter.api.Test;
import primitives.*;
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
     * Produce a picture of two triangles lighted by a spotlight with a
     * partially
     * transparent Sphere producing partial shadow
     */
    @Test
    void testDiffusiveGlass() {
        // https://www.geogebra.org/calculator/jxhczbc2
        scene.geometries.add(
                new Sphere(new Point(-500, 0, 0), 150)
                        .setEmission(new Color(0, 100, 0)),
                new Sphere(new Point(-500, 0, 0), 75),
                new Sphere(new Point(0, 0, 0), 150)
                        .setEmission(new Color(0, 100, 0)),
                new Sphere(new Point(0, 0, 0), 75),
                new Sphere(new Point(500, 0, 0), 150)
                        .setEmission(new Color(0, 100, 0)),
                new Sphere(new Point(500, 0, 0), 75),

                new Triangle(new Point(-650, -200, -100), new Point(-350, -200, -100), new Point(-350, 200, -100))
                        .setEmission(new Color(0, 0, 100)),
                new Triangle(new Point(-650, -200, -100), new Point(-650, 200, -100), new Point(-350, 200, -100))
                        .setEmission(new Color(0, 0, 100))
        );

        scene.setAmbientLight(new AmbientLight(new Color(40, 40, 40)));
        scene.setBackground(new Color(40, 40, 40));
        scene.lights.add(new SpotLight(new Color(700, 400, 400),
                new Point(100, 100, 200),
                new Vector(-1, -1, -2))
                .setKl(0.0005).setKq(0.00005));

        // Camera setup
        Camera.getBuilder()
                .setLocation(new Point(0, 0, 1000)) // looking forward
                .setDirection(Point.ZERO, new Vector(0, 1, 0)) // looking down -Z
                .setVpDistance(1000)
                .setVpSize(1600, 1600)
                .setResolution(800, 800)
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

