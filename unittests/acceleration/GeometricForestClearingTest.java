package acceleration;

import geometries.*;
import lighting.*;
import org.junit.jupiter.api.Test;
import primitives.*;
import renderer.Camera;
import renderer.RayTracerType;
import scene.Scene;

import java.util.Random;

/**
 * Test class for Regular Grid acceleration using a "Geometric Forest Clearing" scene.
 * This scene is designed to maximize the performance benefits of Regular Grid acceleration
 * by creating distinct regions of dense and sparse geometry distribution.
 * <p>
 * Scene Description:
 * - Dense forest areas with hundreds of tree trunks (spheres) and canopy (spheres)
 * - Ancient ruins made of triangular structures scattered throughout
 * - Large empty clearing in the center
 * - Multiple light sources creating complex shadows and reflections
 * <p>
 * Expected Performance: 30-50x improvement with Regular Grid vs without
 */
public class GeometricForestClearingTest {

    /**
     * Default constructor
     */
    public GeometricForestClearingTest() {
    }

    private final static Vector V_TO = new Vector(3, 2, -1);
    private final static Vector V_UP = new Vector(6, -4, 10);
    private final static int RESOLUTION_WIDTH = 800;
    private final static int RESOLUTION_HEIGHT = 800;
    private final static double IMAGE_WIDTH = RESOLUTION_WIDTH;
    private final static double IMAGE_HEIGHT = RESOLUTION_HEIGHT;
    private final static double DISTANCE = 1000;

    /**
     * Scene for all tests
     */
    private final Scene scene = new Scene("Geometric Forest Clearing");

    /**
     * Random generator for consistent randomization
     */
    private final Random random = new Random(42); // Fixed seed for reproducible results

    /**
     * Materials for different scene elements
     */
    private final Material treeTrunkMaterial = new Material()
            .setKD(0.6).setKS(0.2).setShininess(30).setKR(0.1);

    private final Material canopyMaterial = new Material()
            .setKD(0.7).setKS(0.3).setShininess(50).setKR(0.05);

    private final Material ruinMaterial = new Material()
            .setKD(0.5).setKS(0.4).setShininess(80).setKR(0.2);

    private final Material mysticalOrbMaterial = new Material()
            .setKD(0.2).setKS(0.8).setShininess(100).setKR(0.3).setKT(0.2);

    private final Material groundMaterial = new Material()
            .setKD(0.8).setKS(0.2).setShininess(20).setKR(0.1);

    private final Material skyMaterial = new Material()
            .setKD(0.9).setKS(0.1).setShininess(10).setKT(0.3);

    /**
     * Colors for scene elements
     */
    private final Color treeTrunkColor = new Color(101, 67, 33);      // Brown
    private final Color canopyColor = new Color(34, 139, 34);        // Forest Green
    private final Color ruinColor = new Color(169, 169, 169);        // Gray stone
    private final Color mysticalOrbColor = new Color(138, 43, 226);   // Blue Violet
    private final Color groundColor = new Color(107, 142, 35);       // Olive Drab
    private final Color skyColor = new Color(135, 206, 235);         // Sky Blue

    /**
     * Scene dimensions and parameters
     */
    private static final double SCENE_SIZE = 2000;
    private static final double CLEARING_RADIUS = 400;
    private static final int TREE_COUNT = 400;
    private static final int RUIN_COUNT = 15;
    private static final int MYSTICAL_ORB_COUNT = 3;

    /**
     * Test 1: Feature deactivated; MT deactivated
     */
    @Test
    void testRegularGridOff_MultithreadingOff() {
        setupScene();

        Camera.getBuilder()
                .setLocation(new Point(-1200, -800, 600))
                .setDirection(V_TO, V_UP)  // Orthogonal vectors
                .setVpDistance(DISTANCE)
                .setVpSize(IMAGE_WIDTH, IMAGE_HEIGHT)
                .setResolution(RESOLUTION_WIDTH, RESOLUTION_HEIGHT)
                .setRayTracer(scene, RayTracerType.SIMPLE)
                .setMultithreading(0)  // MT deactivated
                .setDebugPrint(0.1)
                .build()
                .renderImage()
                .writeToImage("ForestClearing_GridOff_MTOff");
    }

    /**
     * Test 2: Feature deactivated; MT activated
     */
    @Test
    void testRegularGridOff_MultithreadingOn() {
        setupScene();

        Camera.getBuilder()
                .setLocation(new Point(-1200, -800, 600))
                .setDirection(V_TO, V_UP)  // Orthogonal vectors
                .setVpDistance(DISTANCE)
                .setVpSize(IMAGE_WIDTH, IMAGE_HEIGHT)
                .setResolution(RESOLUTION_WIDTH, RESOLUTION_HEIGHT)
                .setRayTracer(scene, RayTracerType.SIMPLE)
                .setMultithreading(-2)  // MT activated
                .setDebugPrint(0.1)
                .build()
                .renderImage()
                .writeToImage("ForestClearing_GridOff_MTOn");
    }

    /**
     * Test 3: Feature activated; MT deactivated
     */
    @Test
    void testRegularGridOn_MultithreadingOff() {
        setupScene();

        Camera.getBuilder()
                .setLocation(new Point(-1200, -800, 600))
                .setDirection(V_TO, V_UP)  // Orthogonal vectors
                .setVpDistance(DISTANCE)
                .setVpSize(IMAGE_WIDTH, IMAGE_HEIGHT)
                .setResolution(RESOLUTION_WIDTH, RESOLUTION_HEIGHT)
                .setRegularGrid(new RegularGrid(scene))
                .setRayTracer(scene, RayTracerType.GRID)
                .setMultithreading(0)  // MT deactivated
                .setDebugPrint(0.1)
                .build()
                .renderImage()
                .writeToImage("ForestClearing_GridOn_MTOff");
    }

    /**
     * Test 4: Feature activated; MT activated
     */
    @Test
    void testRegularGridOn_MultithreadingOn() {
        setupScene();

        Camera.getBuilder()
                .setLocation(new Point(-1200, -800, 600))
                .setDirection(V_TO, V_UP)
                .setVpDistance(DISTANCE)
                .setVpSize(IMAGE_WIDTH, IMAGE_HEIGHT)
                .setResolution(RESOLUTION_WIDTH, RESOLUTION_HEIGHT)
                .setRegularGrid(new RegularGrid(scene))
                .setRayTracer(scene, RayTracerType.GRID)
                .setMultithreading(-2)
                .setDebugPrint(0.1)
                .build()
                .renderImage()
                .writeToImage("ForestClearing_GridOn_MTOn");
    }

    /**
     * Set up the complete forest clearing scene with all geometries and lighting
     */
    private void setupScene() {
        // Clear any existing geometries
        scene.geometries = new Geometries();
        scene.lights.clear();

        // Create the forest clearing scene
        createGroundPlane();
        createSkyDome();
        createForestTrees();
        createAncientRuins();
        createMysticalOrbs();
        setupLighting();

        // Set scene properties
        scene.setBackground(new Color(25, 25, 35).scale(0.1));  // Dark night sky
        scene.setAmbientLight(new AmbientLight(new Color(15, 15, 20).scale(0.1)));
    }

    /**
     * Create the ground plane for the forest floor
     */
    private void createGroundPlane() {
        scene.geometries.add(
                new Plane(new Vector(0, 0, 1), new Point(0, 0, 0))
                        .setEmission(groundColor)
                        .setMaterial(groundMaterial)
        );
    }

    /**
     * Create a sky dome using a large sphere
     */
    private void createSkyDome() {
        scene.geometries.add(
                new Sphere(new Point(0, 0, 0), SCENE_SIZE * 2)
                        .setEmission(skyColor)
                        .setMaterial(skyMaterial)
        );
    }

    /**
     * Create the forest with clustered trees around the clearing
     */
    private void createForestTrees() {
        // Create 4 forest clusters around the clearing
        createTreeCluster(-800, -800, 100);  // Bottom-left cluster
        createTreeCluster(800, -800, 100);   // Bottom-right cluster
        createTreeCluster(-800, 800, 100);   // Top-left cluster
        createTreeCluster(800, 800, 100);    // Top-right cluster
    }

    /**
     * Create a cluster of trees at specified location
     */
    private void createTreeCluster(double centerX, double centerY, int treeCount) {
        for (int i = 0; i < treeCount; i++) {
            // Random position within cluster radius
            double angle = random.nextDouble() * 2 * Math.PI;
            double radius = random.nextDouble() * 300; // Cluster radius

            double x = centerX + radius * Math.cos(angle);
            double y = centerY + radius * Math.sin(angle);

            // Ensure trees don't spawn in the clearing
            if (Math.sqrt(x * x + y * y) > CLEARING_RADIUS) {
                createSingleTree(x, y);
            }
        }
    }

    /**
     * Create a single tree with trunk and canopy
     */
    private void createSingleTree(double x, double y) {
        // Tree trunk height varies
        double trunkHeight = 80 + random.nextDouble() * 60;
        double trunkRadius = 8 + random.nextDouble() * 6;

        // Tree trunk (using sphere for simplicity - could use cylinder)
        scene.geometries.add(
                new Sphere(new Point(x, y, trunkHeight / 2), trunkRadius)
                        .setEmission(treeTrunkColor)
                        .setMaterial(treeTrunkMaterial)
        );

        // Tree canopy
        double canopyRadius = 25 + random.nextDouble() * 20;
        double canopyHeight = trunkHeight + canopyRadius * 0.7;

        scene.geometries.add(
                new Sphere(new Point(x, y, canopyHeight), canopyRadius)
                        .setEmission(canopyColor)
                        .setMaterial(canopyMaterial)
        );
    }

    /**
     * Create ancient ruins using triangular pyramid structures
     */
    private void createAncientRuins() {
        for (int i = 0; i < RUIN_COUNT; i++) {
            // Random position avoiding the center clearing
            double angle = random.nextDouble() * 2 * Math.PI;
            double radius = CLEARING_RADIUS + random.nextDouble() * (SCENE_SIZE / 2 - CLEARING_RADIUS);

            double x = radius * Math.cos(angle);
            double y = radius * Math.sin(angle);

            createPyramidRuin(x, y);
        }
    }

    /**
     * Create a pyramid ruin structure using triangles
     */
    private void createPyramidRuin(double x, double y) {
        double baseSize = 30 + random.nextDouble() * 40;
        double height = 40 + random.nextDouble() * 60;

        // Base points
        Point p1 = new Point(x - baseSize / 2, y - baseSize / 2, 0);
        Point p2 = new Point(x + baseSize / 2, y - baseSize / 2, 0);
        Point p3 = new Point(x + baseSize / 2, y + baseSize / 2, 0);
        Point p4 = new Point(x - baseSize / 2, y + baseSize / 2, 0);
        Point apex = new Point(x, y, height);

        // Create pyramid faces
        scene.geometries.add(
                // Base
                new Triangle(p1, p2, p3).setEmission(ruinColor).setMaterial(ruinMaterial),
                new Triangle(p1, p3, p4).setEmission(ruinColor).setMaterial(ruinMaterial),
                // Sides
                new Triangle(p1, p2, apex).setEmission(ruinColor).setMaterial(ruinMaterial),
                new Triangle(p2, p3, apex).setEmission(ruinColor).setMaterial(ruinMaterial),
                new Triangle(p3, p4, apex).setEmission(ruinColor).setMaterial(ruinMaterial),
                new Triangle(p4, p1, apex).setEmission(ruinColor).setMaterial(ruinMaterial)
        );
    }

    /**
     * Create mystical orbs in the clearing center
     */
    private void createMysticalOrbs() {
        for (int i = 0; i < MYSTICAL_ORB_COUNT; i++) {
            double angle = (2 * Math.PI * i) / MYSTICAL_ORB_COUNT;
            double radius = 100 + i * 50;

            double x = radius * Math.cos(angle);
            double y = radius * Math.sin(angle);
            double z = 80 + i * 30;

            scene.geometries.add(
                    new Sphere(new Point(x, y, z), 40 + i * 10)
                            .setEmission(mysticalOrbColor)
                            .setMaterial(mysticalOrbMaterial)
            );
        }
    }

    /**
     * Setup complex lighting with 5+ light sources
     */
    private void setupLighting() {
        // 1. Sun - Main directional light (dimmer)
        scene.lights.add(
                new DirectionalLight(new Color(60, 55, 40), new Vector(1, 1, -2))
        );

        // 2. Mystical Orb lights - Point lights inside the orbs (dimmer, more attenuation)
        scene.lights.add(
                new PointLight(new Color(40, 15, 60), new Point(100, 0, 80))
                        .setKl(0.0015).setKq(0.0000015)
        );
        scene.lights.add(
                new PointLight(new Color(15, 40, 60), new Point(-50, 87, 110))
                        .setKl(0.0015).setKq(0.0000015)
        );
        scene.lights.add(
                new PointLight(new Color(60, 40, 15), new Point(-50, -87, 140))
                        .setKl(0.0015).setKq(0.0000015)
        );

        // 3. Firefly lights - Small scattered point lights (dimmer, more attenuation)
        for (int i = 0; i < 8; i++) {
            double angle = (2 * Math.PI * i) / 8;
            double radius = 200 + random.nextDouble() * 600;
            double x = radius * Math.cos(angle);
            double y = radius * Math.sin(angle);
            double z = 20 + random.nextDouble() * 40;

            scene.lights.add(
                    new PointLight(new Color(20, 20, 8), new Point(x, y, z))
                            .setKl(0.003).setKq(0.0003)
            );
        }

        // 4. Moon - Soft ambient spotlight from above (dimmer, more attenuation)
        scene.lights.add(
                new SpotLight(new Color(15, 15, 25), new Point(0, 0, 1000), new Vector(0, 0, -1))
                        .setKl(0.0002).setKq(0.000002).setNarrowBeam(45)
        );

        // 5. Ruin glow - Colored lights from ruin structures (dimmer, more attenuation)
        scene.lights.add(
                new SpotLight(new Color(10, 40, 10), new Point(-600, -600, 100), new Vector(1, 1, -1))
                        .setKl(0.002).setKq(0.0002).setNarrowBeam(30)
        );
        scene.lights.add(
                new SpotLight(new Color(40, 10, 10), new Point(600, 600, 100), new Vector(-1, -1, -1))
                        .setKl(0.002).setKq(0.0002).setNarrowBeam(30)
        );
    }
}