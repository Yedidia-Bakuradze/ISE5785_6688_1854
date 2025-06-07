package renderer;

import geometries.Geometry;
import geometries.Sphere;
import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Vector;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Integration tests for Camera ray construction with geometric intersections.
 * This class tests the integration between Camera and various geometric shapes.
 */
public class CameraIntersectionsIntegrationTests {
    /**
     * The number of pixels for the camera integration tests.
     */
    private static final int PIXELS = 3;

    /**
     * Default constructor for the test to make the JavaDoc error to be gone
     */
    public CameraIntersectionsIntegrationTests() {
    }

    /**
     * Vector representing the Y axis direction
     */
    private final Vector yAxis = new Vector(0, -1, 0);
    /**
     * Vector representing the Z axis direction
     */
    private final Vector zAxis = new Vector(0, 0, -1);

    /**
     * Camera builder configured with basic settings for tests
     */
    private final Camera.Builder cameraBuilder = Camera.getBuilder()
            .setDirection(zAxis, yAxis)
            .setVpDistance(1)
            .setVpSize(3, 3)
            .setResolution(PIXELS, PIXELS);

    /**
     * Camera instance positioned for intersection tests
     */
    private final Camera camera = cameraBuilder.setLocation(new Point(0, 0, 0.5)).build();

    /**
     * Helper method to count and verify intersections between camera rays and geometry
     *
     * @param camera         The camera to generate rays from
     * @param geometry       The geometry to test intersections with
     * @param expectedAmount Expected number of intersections
     */
    private void CompareCountOfIntersections(Camera camera, Geometry geometry, int expectedAmount) {
        int intersections = 0;
        for (int j = 0; j < PIXELS; j++)
            for (int i = 0; i < PIXELS; i++) {
                List<Point> intersectionsList = geometry.findIntersections(camera.constructRay(PIXELS, PIXELS, j, i));
                intersections += intersectionsList != null ? intersectionsList.size() : 0;
            }

        assertEquals(expectedAmount, intersections, "Wrong amount of intersections");
    }

    /**
     * Tests intersections between camera rays and spheres
     * Includes various cases with different numbers of intersections
     */
    @Test
    void testSphereIntersection() {
        // TC01: 2 intersections
        CompareCountOfIntersections(cameraBuilder.setLocation(Point.ZERO).build(), new Sphere(new Point(0, 0, -3), 1), 2);

        // TC02: 18 intersections
        CompareCountOfIntersections(camera, new Sphere(new Point(0, 0, -2.5), 2.5), 18);

        // TC03: 10 intersections
        CompareCountOfIntersections(camera, new Sphere(new Point(0, 0, -2), 2), 10);

        // TC04: 9 intersections
        CompareCountOfIntersections(camera, new Sphere(new Point(0, 0, -1), 4), 9);

        // TC05: 0 intersections
        CompareCountOfIntersections(camera, new Sphere(new Point(0, 0, 1), 0.5), 0);
    }

    /**
     * Tests intersections between camera rays and planes
     * Tests different plane orientations
     */
    @Test
    void testPlaneIntersection() {
        // TC01: 9 intersections
        CompareCountOfIntersections(camera, new geometries.Plane(new Vector(0, 0, -1), new Point(0, 0, -1)), 9);

        // TC02: 9 intersections
        CompareCountOfIntersections(camera, new geometries.Plane(new Vector(0, 1, -10), new Point(0, 0, -1)), 9);

        // TC03: 6 intersections
        CompareCountOfIntersections(camera, new geometries.Plane(new Vector(0, -1, -1), new Point(0, 0, -1)), 6);
    }

    /**
     * Tests intersections between camera rays and triangles
     * Tests different triangle positions and orientations
     */
    @Test
    void testTriangleIntersection() {
        // TC01: 1 intersections
        CompareCountOfIntersections(camera, new geometries.Triangle(new Point(0, 1, -2), new Point(1, -1, -2), new Point(-1, -1, -2)), 1);

        // TC02: 2 intersections
        CompareCountOfIntersections(camera, new geometries.Triangle(new Point(0, 20, -2), new Point(1, -1, -2), new Point(-1, -1, -2)), 2);
    }
}
