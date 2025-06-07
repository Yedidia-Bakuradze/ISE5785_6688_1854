package primitives;

import geometries.*;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link primitives.Ray}.
 * This class tests the functionality of ray operations, including finding closest points and constructing rays.
 */
class RayTests {

    /**
     * Default constructor - only to dismiss errors in JavaDoc generator.
     */
    RayTests() {
    }

    // ============ <b>Global Fields</b> ============
    /**
     * A ray to be used in the tests.
     */
    private final Ray ray = new Ray(new Point(1, 0, 0), new Vector(1, 0, 0));

    /**
     * A geometries object to be used in the tests.
     * It contains the closets object's point in the middle of the list,
     */
    private final Geometries middleClosestIntersectionPoint = new Geometries(
            new Triangle(
                    new Point(7, 1, -1),
                    new Point(7, -1, 0),
                    new Point(7, 1, 2)
            ),
            new Sphere(new Point(4, 0, 0), 1),
            new Plane(new Vector(1, 0, 0), new Point(8, 0, 0))
    );
    /**
     * A geometries object to be used in the tests.
     * It contains the closets object's point in the beginning of the list,
     */
    private final Geometries beginningClosestIntersectionPoint = new Geometries(
            new Sphere(new Point(4, 0, 0), 1),
            new Triangle(
                    new Point(7, 1, -1),
                    new Point(7, -1, 0),
                    new Point(7, 1, 2)
            ),
            new Plane(new Vector(1, 0, 0), new Point(8, 0, 0))
    );

    /**
     * A geometries object to be used in the tests.
     * It contains the closets object's point at the end of the list,
     */
    private final Geometries endPointClosestIntersectionPoint = new Geometries(
            new Triangle(
                    new Point(7, 1, -1),
                    new Point(7, -1, 0),
                    new Point(7, 1, 2)
            ),
            new Plane(new Vector(1, 0, 0), new Point(8, 0, 0)),
            new Sphere(new Point(4, 0, 0), 1)
    );

    /**
     * Test method for {@link Ray#getPoint(double)}.
     */
    @Test
    void testGetPoint() {
        // ======== Equivalence Partitions Tests ========
        // TC01: Test for positive values
        assertEquals(new Point(2, 0, 0), ray.getPoint(1), "ERROR: getPoint() for positive value is incorrect");

        // TC02: Test for negative values
        assertEquals(new Point(-1, 0, 0), ray.getPoint(-2), "ERROR: getPoint() for negative value is incorrect");

        // ======== Boundary Value Tests ========
        // TC11: Test for zero t value
        assertEquals(new Point(1, 0, 0), ray.getPoint(0), "ERROR: getPoint() for zero value is incorrect");

        // TC12: Test for t value that resets the point (goes from 1,1,1 to 0,0,0 by vector -1,-1,-1)
        assertEquals(Point.ZERO, ray.getPoint(-1), "ERROR: getPoint() for t value that resets the point is incorrect");
    }

    /**
     * Test method for {@link Ray#findClosestPoint(List)}
     */
    @Test
    void testFindClosestPoint() {
        // ======== Equivalence Partitions Tests ========
        // TC01: The closest point is in the middle of the provided list
        assertEquals(new Point(3, 0, 0), ray.findClosestPoint(middleClosestIntersectionPoint.findIntersections(ray)), "ERROR: findClosestPoint() for middle point is incorrect");

        // ======== Boundary Value Tests ========

        // TC11: The list is empty (null list provided)
        assertNull(ray.findClosestPoint(null), "ERROR: findClosestPoint() for middle point is incorrect");

        // TC12: The closet point is the first point on the list
        assertEquals(new Point(3, 0, 0), ray.findClosestPoint(beginningClosestIntersectionPoint.findIntersections(ray)), "ERROR: findClosestPoint() for middle point is incorrect");

        // TC13: The closet point is the last point on the list
        assertEquals(new Point(3, 0, 0), ray.findClosestPoint(endPointClosestIntersectionPoint.findIntersections(ray)), "ERROR: findClosestPoint() for middle point is incorrect");
    }

}