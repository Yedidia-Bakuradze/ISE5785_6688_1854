package renderer;

import geometries.Geometry;
import geometries.Sphere;
import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Vector;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CameraIntersectionsIntegrationTests {
    private final Vector yAxis = new Vector(0, -1, 0);
    private final Vector zAxis = new Vector(0, 0, -1);

    private final Camera.Builder cameraBuilder = Camera.getBuilder()
            .setDirection(zAxis, yAxis)
            .setDistance(1)
            .setSize(3, 3);

    private final Camera camera = cameraBuilder.setLocation(new Point(0, 0, 0.5)).build();

    /**
     * helper function to test the amount of intersections
     */
    private void CompareCountOfIntersections(Camera camera, Geometry geometry, int expectedAmount) {
        int intersections = 0;
        for (int j = 0; j < 3; j++)
            for (int i = 0; i < 3; i++) {
                List<Point> intersectionsList = geometry.findIntersections(camera.constructRay(3, 3, j, i));
                intersections += intersectionsList != null ? intersectionsList.size() : 0;
            }

        assertEquals(expectedAmount, intersections, "Wrong amount of intersections");
    }


    /**
     * Test method for
     * {@link renderer.Camera#constructRay(int, int, int, int)}.
     */
    @Test
    void testSphereIntersection() {
        // TC01: 2 intersections
        CompareCountOfIntersections(cameraBuilder.setLocation(Point.ZERO).build(), new Sphere(new Point(0, 0, -3),1), 2);

        // TC02: 18 intersections
        CompareCountOfIntersections(camera, new Sphere(new Point(0, 0, -2.5),2.5), 18);

        // TC03: 10 intersections
        CompareCountOfIntersections(camera, new Sphere(new Point(0, 0, -2),2), 10);

        // TC04: 9 intersections
        CompareCountOfIntersections(camera, new Sphere(new Point(0, 0, -1),4), 9);

        // TC05: 0 intersections
        CompareCountOfIntersections(camera, new Sphere( new Point(0, 0, 1),0.5), 0);
    }

    /**
     * Test method for
     * {@link renderer.Camera#constructRay(int, int, int, int)}.
     */
    @Test
    void testPlaneIntersection() {
        // TC01: 9 intersections
        CompareCountOfIntersections(camera, new geometries.Plane(new Vector(0, 0, -1),new Point(0, 0, -1)), 9);

        // TC02: 9 intersections
        CompareCountOfIntersections(camera, new geometries.Plane(new Vector(0, 1, -10),new Point(0, 0, -1)), 9);

        // TC03: 6 intersections
        CompareCountOfIntersections(camera, new geometries.Plane(new Vector(0, -1, -1),new Point(0, 0, -1)), 6);
    }

    /**
     * Test method for
     * {@link renderer.Camera#constructRay(int, int, int, int)}.
     */
    @Test
    void testTriangleIntersection() {
        // TC01: 1 intersections
        CompareCountOfIntersections(camera, new geometries.Triangle(new Point(0, 1, -2), new Point(1, -1, -2), new Point(-1, -1, -2)), 1);

        // TC02: 2 intersections
        CompareCountOfIntersections(camera, new geometries.Triangle(new Point(0, 20, -2), new Point(1, -1, -2), new Point(-1, -1, -2)), 2);
    }
}
