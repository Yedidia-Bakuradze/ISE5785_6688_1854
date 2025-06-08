package renderer;

import geometries.Triangle;
import lighting.AmbientLight;
import lighting.SpotLight;
import org.junit.jupiter.api.Test;
import primitives.*;
import scene.Scene;

import java.util.ArrayList;
import java.util.List;

public class PersonalTest {

    Scene scene = new Scene("Personal Test Scene");
    public Camera.Builder cameraBuilder = Camera.getBuilder() //
            .setRayTracer(scene, RayTracerType.SIMPLE);

    @Test
    void testTeaPot() {
        // Generate teapot geometry
        generateTeapotGeometry();

        // Set up lighting - using SpotLight positioned to create good shadows
        scene.setBackground(new Color(100, 100, 100));
        scene.setAmbientLight(new AmbientLight(new Color(30, 30, 30)));
        scene.lights.add(
                new SpotLight(new Color(800, 600, 400), new Point(200, -200, 400), new Vector(1, 1, -1))
                        .setKl(0.0001).setKq(0.00001)
        );

        cameraBuilder
                .setLocation(new Point(-1000, 0, 650))
                .setDirection(new Vector(200, 0, -50), new Vector(50, 0, 200))
                .setVpDistance(1000)
                .setVpSize(600, 600)
                .setResolution(600, 600)
                .build()
                .renderImage()
                .writeToImage("Tea Pot");
    }

    /**
     * Generate the teapot geometry using the layered approach with triangular mesh
     */
    private void generateTeapotGeometry() {
        double x = 400;
        double y = 0;
        double z = 100;

        Point centerBase = new Point(x, y, z);

        // Define custom radiuses for each layer
//        double[] layerRadiuses = {150, 200, 250, 300, 250, 200, 200};
        double[] layerRadiuses = {100, 150, 200, 250, 200, 150, 150};

        // Generate all layers of points
        List<List<Point>> layers = generateTeapotLayers(centerBase, x, y, z, layerRadiuses);

        // Create triangular mesh between layers
        createTeapotMesh(layers, centerBase, x, y, z);
    }

    /**
     * Generate all 7 layers of points for the teapot body
     */
    private List<List<Point>> generateTeapotLayers(Point centerBase, double x, double y, double z, double[] layerRadiuses) {
        List<List<Point>> layers = new ArrayList<>();

        // Layer heights
        int[] layerHeights = {0, 25, 50, 100, 150, 200, 250};
        int pointsPerLayer = 20;

        if (layerRadiuses.length != layerHeights.length) {
            throw new IllegalArgumentException("Number of radiuses must match number of layers");
        }

        for (int i = 0; i < layerHeights.length; i++) {
            int height = layerHeights[i];
            double radius = layerRadiuses[i];

            List<Point> layer = generateCircularLayer(x, y, z, centerBase, height, radius, pointsPerLayer);
            layers.add(layer);
        }

        return layers;
    }

    /**
     * Generate a circular layer of points around a center
     */
    private List<Point> generateCircularLayer(double centerX, double centerY, double centerZ, Point center, int heightOffset, double radius, int pointCount) {
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
     * Create the triangular mesh for the teapot body
     */
    private void createTeapotMesh(List<List<Point>> layers, Point centerBase, double x, double y, double z) {
        // Create bottom triangles (first layer to center point)
        createBottomTriangles(layers.getFirst(), centerBase);

        // Create side triangles between adjacent layers
        for (int i = 0; i < layers.size() - 1; i++) {
            createLayerTriangles(layers.get(i), layers.get(i + 1));
        }

        // Create top triangles (last layer to center point)
        Point topCenter = new Point(x, y, z + 60);
        createTopTriangles(layers.getLast(), topCenter);
    }

    /**
     * Create triangles from the first layer to the bottom center point
     */
    private void createBottomTriangles(List<Point> bottomLayer, Point centerBase) {
        int pointCount = bottomLayer.size();

        for (int i = 0; i < pointCount; i++) {
            Point p1 = bottomLayer.get(i);
            Point p2 = bottomLayer.get((i + 1) % pointCount);

            // Create triangle with center point
            scene.geometries.add(
                    new Triangle(centerBase, p1, p2)
                            .setEmission(new Color(60, 40, 20))
                            .setMaterial(new Material()
                                    .setKD(0.4)
                                    .setKS(0.6)
                                    .setShininess(80)
                                    .setKR(0.1))
            );
        }
    }

    /**
     * Create triangles from the last layer to the top center point
     */
    private void createTopTriangles(List<Point> topLayer, Point topCenter) {
        int pointCount = topLayer.size();

        for (int i = 0; i < pointCount; i++) {
            Point p1 = topLayer.get(i);
            Point p2 = topLayer.get((i + 1) % pointCount);

            // Create triangle with center point
            scene.geometries.add(
                    new Triangle(topCenter, p2, p1)
                            .setEmission(new Color(60, 40, 20))
                            .setMaterial(new Material()
                                    .setKD(0.4)
                                    .setKS(0.6)
                                    .setShininess(80)
                                    .setKR(0.1))
            );
        }
    }

    /**
     * Create triangular mesh between two adjacent layers
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
                            .setEmission(new Color(80, 50, 25))
                            .setMaterial(new Material()
                                    .setKD(0.4)
                                    .setKS(0.6)
                                    .setShininess(80)
                                    .setKR(0.15)
                                    .setKT(0.4)
                            )

            );

            // Triangle 2: p2, p4, p3
            scene.geometries.add(
                    new Triangle(p2, p4, p3)
                            .setEmission(new Color(80, 50, 25))
                            .setMaterial(new Material()
                                    .setKD(0.4)
                                    .setKS(0.6)
                                    .setShininess(80)
                                    .setKR(0.15)
                                    .setKT(0.4)
                            )
            );
        }
    }
}

