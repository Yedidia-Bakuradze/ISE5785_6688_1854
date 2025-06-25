package acceleration;

import geometries.*;
import lighting.*;
import org.junit.jupiter.api.Test;
import primitives.*;
import renderer.Camera;
import renderer.RayTracerType;
import scene.Scene;

/**
 * Test class for Regular Grid Acceleration using a complex scene (Geometric Forest Clearing)
 */
public class RegularGridForestClearingTest {
    /**
     * Creates the mystical forest clearing scene with hundreds of objects and multiple light sources.
     *
     * @return the constructed Scene
     */
    private Scene createForestClearingScene() {
        Scene scene = new Scene("Geometric Forest Clearing");
        scene.setAmbientLight(new AmbientLight(new Color(80, 80, 120).scale(0.2)));

        // Ground plane
        scene.geometries.add(new Plane(new Vector(0, 1, 0), new Point(0, 0, 0))
                .setMaterial(new Material().setKD(0.7).setKS(0.2).setShininess(30))
                .setEmission(new Color(34, 139, 34)));

        // Tree trunks (cylinders or thin spheres)
        int numClusters = 7;
        int trunksPerCluster = 50;
        double clusterRadius = 120;
        double clearingRadius = 60;
        for (int c = 0; c < numClusters; c++) {
            double angle = 2 * Math.PI * c / numClusters;
            double cx = Math.cos(angle) * clusterRadius;
            double cz = Math.sin(angle) * clusterRadius;
            for (int t = 0; t < trunksPerCluster; t++) {
                double theta = 2 * Math.PI * t / trunksPerCluster;
                double r = 10 + Math.random() * 15;
                double x = cx + Math.cos(theta) * r + (Math.random() - 0.5) * 3;
                double z = cz + Math.sin(theta) * r + (Math.random() - 0.5) * 3;
                double h = 40 + Math.random() * 30;
//                scene.geometries.add(new Cylinder(0.8, new Ray(new Point(x, 0, z), new Vector(0, 1, 0)), h)
//                        .setMaterial(new Material().setKD(0.5).setKS(0.3).setShininess(20))
//                        .setEmission(new Color(60, 40, 20)));
                // Canopy sphere
                scene.geometries.add(new Sphere(new Point(x, h + 2, z), 4 + Math.random() * 2)
                        .setMaterial(new Material().setKD(0.3).setKS(0.5).setShininess(60).setKT(0.1))
                        .setEmission(new Color(34 + (int) (Math.random() * 20), 139 + (int) (Math.random() * 40), 34)));
            }
        }

        // Ancient ruins (pyramids made of triangles)
        int numRuins = 12;
        for (int i = 0; i < numRuins; i++) {
            double rx = (Math.random() - 0.5) * clearingRadius * 1.5;
            double rz = (Math.random() - 0.5) * clearingRadius * 1.5;
            double base = 6 + Math.random() * 3;
            double height = 7 + Math.random() * 3;
            Point p1 = new Point(rx, 0, rz);
            Point p2 = new Point(rx + base, 0, rz);
            Point p3 = new Point(rx + base / 2, 0, rz + base);
            Point apex = new Point(rx + base / 2, height, rz + base / 3);
            scene.geometries.add(new Triangle(p1, p2, apex)
                    .setMaterial(new Material().setKD(0.4).setKS(0.5).setShininess(80))
                    .setEmission(new Color(180, 180, 120)));
            scene.geometries.add(new Triangle(p2, p3, apex)
                    .setMaterial(new Material().setKD(0.4).setKS(0.5).setShininess(80))
                    .setEmission(new Color(180, 180, 120)));
            scene.geometries.add(new Triangle(p3, p1, apex)
                    .setMaterial(new Material().setKD(0.4).setKS(0.5).setShininess(80))
                    .setEmission(new Color(180, 180, 120)));
            scene.geometries.add(new Triangle(p1, p2, p3)
                    .setMaterial(new Material().setKD(0.4).setKS(0.5).setShininess(80))
                    .setEmission(new Color(120, 120, 80)));
        }

        // Central mystical orbs
        for (int i = 0; i < 3; i++) {
            double angle = 2 * Math.PI * i / 3;
            double r = 10;
            double x = Math.cos(angle) * r;
            double z = Math.sin(angle) * r;
            scene.geometries.add(new Sphere(new Point(x, 7, z), 5)
                    .setMaterial(new Material().setKD(0.2).setKS(0.8).setShininess(200).setKT(0.7))
                    .setEmission(new Color(180, 220, 255)));
        }

        // Sky dome (large sphere above)
        scene.geometries.add(new Sphere(new Point(0, 200, 0), 180)
                .setMaterial(new Material().setKD(0.1).setKS(0.1).setShininess(10))
                .setEmission(new Color(120, 180, 255)));

        // Lighting
        // Sun (directional)
        scene.lights.add(new DirectionalLight(new Color(255, 255, 220), new Vector(-1, -2, -1)));
        // Moon (ambient)
        scene.setAmbientLight(new AmbientLight(new Color(80, 80, 120).scale(0.3)));
        // Mystical orbs (point lights)
        scene.lights.add(new PointLight(new Color(120, 180, 255), new Point(0, 7, 0)).setKl(0.0005).setKq(0.0002));
        scene.lights.add(new PointLight(new Color(255, 120, 180), new Point(8, 7, 8)).setKl(0.0005).setKq(0.0002));
        scene.lights.add(new PointLight(new Color(180, 255, 120), new Point(-8, 7, -8)).setKl(0.0005).setKq(0.0002));
        // Fireflies (random point lights)
        for (int i = 0; i < 10; i++) {
            double x = (Math.random() - 0.5) * 80;
            double y = 5 + Math.random() * 30;
            double z = (Math.random() - 0.5) * 80;
            scene.lights.add(new PointLight(new Color(255, 255, 180), new Point(x, y, z)).setKl(0.002).setKq(0.001));
        }
        // Ruin glow (colored spotlights)
        for (int i = 0; i < numRuins; i++) {
            double rx = (Math.random() - 0.5) * clearingRadius * 1.5;
            double rz = (Math.random() - 0.5) * clearingRadius * 1.5;
            scene.lights.add(new PointLight(new Color(255, 200, 120), new Point(rx, 2, rz)).setKl(0.002).setKq(0.001));
        }
        return scene;
    }

    /**
     * Test rendering with and without Regular Grid and Multithreading
     */
    @Test
    public void testForestClearing_GridAndMT() {
        Scene scene = createForestClearingScene();
        Camera camera = Camera.getBuilder()
                .setLocation(new Point(0, 60, 180))
                .setDirection(new Vector(0, -1, -0.3), new Vector(0, 1, 0)) // vTo and vUp are now orthogonal
                .setVpSize(200, 200)
                .setVpDistance(180)
                .setResolution(800, 800)
                .setRayTracer(scene, RayTracerType.GRID)
                .setMultithreading(-1)
                .build();
        long start = System.currentTimeMillis();
        camera.renderImage();
        camera.writeToImage("forest_clearing_grid_on_mt_on");
        long end = System.currentTimeMillis();
        System.out.println("[Grid ON, MT ON] Render time: " + (end - start) + " ms");
    }

    @Test
    public void testForestClearing_GridOn_MTOff() {
        Scene scene = createForestClearingScene();
        Camera camera = Camera.getBuilder()
                .setLocation(new Point(0, 60, 180))
                .setDirection(new Vector(0, -1, -0.3), new Vector(0, 1, 0)) // vTo and vUp are now orthogonal
                .setVpSize(200, 200)
                .setVpDistance(180)
                .setResolution(800, 800)
                .setRayTracer(scene, RayTracerType.GRID)
                .setMultithreading(0)
                .build();
        long start = System.currentTimeMillis();
        camera.renderImage();
        camera.writeToImage("forest_clearing_grid_on_mt_off");
        long end = System.currentTimeMillis();
        System.out.println("[Grid ON, MT OFF] Render time: " + (end - start) + " ms");
    }

    @Test
    public void testForestClearing_GridOff_MTOn() {
        Scene scene = createForestClearingScene();
        Camera camera = Camera.getBuilder()
                .setLocation(new Point(0, 60, 180))
                .setDirection(new Vector(0, -1, -0.3), new Vector(0, 1, 0)) // vTo and vUp are now orthogonal
                .setVpSize(200, 200)
                .setVpDistance(180)
                .setResolution(800, 800)
                .setRayTracer(scene, RayTracerType.SIMPLE)
                .setMultithreading(-1)
                .build();
        long start = System.currentTimeMillis();
        camera.renderImage();
        camera.writeToImage("forest_clearing_grid_off_mt_on");
        long end = System.currentTimeMillis();
        System.out.println("[Grid OFF, MT ON] Render time: " + (end - start) + " ms");
    }

    @Test
    public void testForestClearing_GridOff_MTOff() {
        Scene scene = createForestClearingScene();
        Camera camera = Camera.getBuilder()
                .setLocation(new Point(0, 60, 180))
                .setDirection(new Vector(0, -1, -0.3), new Vector(0, 1, 0)) // vTo and vUp are now orthogonal
                .setVpSize(200, 200)
                .setVpDistance(180)
                .setResolution(800, 800)
                .setRayTracer(scene, RayTracerType.SIMPLE)
                .setMultithreading(0)
                .build();
        long start = System.currentTimeMillis();
        camera.renderImage();
        camera.writeToImage("forest_clearing_grid_off_mt_off");
        long end = System.currentTimeMillis();
        System.out.println("[Grid OFF, MT OFF] Render time: " + (end - start) + " ms");
    }
}
