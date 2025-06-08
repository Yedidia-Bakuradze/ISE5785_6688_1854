package renderer;

import geometries.Plane;
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

    double TOTAL_HEIGHT = 500; // Total height of the teapot

    Material midTriangleMaterial = new Material()
            .setKD(0.4)
            .setKS(0.6)
            .setShininess(80)
            .setKR(0.15)
            .setKT(0.4);

    Material topBottomTriagnlesMaterial = new Material()
            .setKD(0.4)
            .setKS(0.6)
            .setShininess(80)
            .setKR(0.15);

    Color midTriangleColor = new Color(80, 50, 25);
    Color topBottomTriangleColor = new Color(60, 40, 20);

    @Test
    void Jug() {
        // Generate teapot geometry
        int[] layerRadiuses = {100, 110, 150, 170, 180, 210, 210, 180, 170, 150, 150, 120, 120};
        // Layer heights
        int[] layerHeights = {0, 25, 50, 100, 150, 200, 220, 250, 300, 350, 400, 450, 500};
        int pointsPerLayer = 15;

        generateTeapotGeometry(400, 0, 100, layerRadiuses, layerHeights, pointsPerLayer);

        scene.geometries.add(
                new Plane(new Vector(0, 0, 1), new Point(200, 200, 70))
                        .setEmission(new Color(0, 1, 0))
                        .setMaterial(
                                new Material()
                                        .setKR(0.1)
                                        .setKD(0.3)
                                        .setKS(0.8)
                                        .setShininess(100)
                        )

        );

        // Set up lighting - using SpotLight positioned to create good shadows
        scene.setBackground(new Color(0, 79, 100));
        scene.setAmbientLight(new AmbientLight(new Color(50, 50, 50)));
        scene.lights.add(
                new SpotLight(new Color(800, 600, 400), new Point(200, -200, 400), new Vector(1, 1, -1))
                        .setKl(0.0004).setKq(0.0000006).setNarrowBeam(8)
        );
        scene.lights.add(
                new SpotLight(new Color(800, 600, 400), new Point(200, 200, 400), new Vector(1, -1, 0))
                        .setKl(0.0004).setKq(0.0000006).setNarrowBeam(100)
        );

        cameraBuilder
                .setLocation(new Point(-1000, 0, 650))
                .setDirection(new Vector(200, 0, -50), new Vector(50, 0, 200))
                .setVpDistance(1000)
                .setVpSize(600, 600)
                .setResolution(600, 600)
                .build()
                .renderImage()
                .writeToImage("Jug");
    }

    /**
     * Generate the teapot geometry using the layered approach with triangular mesh
     */
    private void generateTeapotGeometry(double x, double y, double z, int[] layerRadiuses, int[] layerHeights, int pointsPerLayer) {
        Point centerBase = new Point(x, y, z);

        // Generate all layers of points
        List<List<Point>> layers = generateTeapotLayers(centerBase, x, y, z, layerRadiuses, layerHeights, pointsPerLayer);

        // Create triangular mesh between layers
        createTeapotMesh(layers, centerBase, x, y, z);
    }

    /**
     * Generate all 7 layers of points for the teapot body
     */
    private List<List<Point>> generateTeapotLayers(Point centerBase, double x, double y, double z, int[] layerRadiuses, int[] layerHeights, int pointsPerLayer) {
        List<List<Point>> layers = new ArrayList<>();

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
        Point topCenter = new Point(x, y, z + TOTAL_HEIGHT);
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
                            .setEmission(topBottomTriangleColor)
                            .setMaterial(topBottomTriagnlesMaterial)
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
                            .setEmission(topBottomTriangleColor)
                            .setMaterial(topBottomTriagnlesMaterial)
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

