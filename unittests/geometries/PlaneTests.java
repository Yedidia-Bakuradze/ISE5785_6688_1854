package geometries;

import org.junit.jupiter.api.Test;
import primitives.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link geometries.Plane}.
 * This class tests the functionality of the Plane class, including normal and intersection calculations.
 */
public class PlaneTests {

    // ============ <b>Global Fields</b> ============

    /**
     * Q point of the plane sent to its constructor.
     */
    private final Point qPoint = new Point(1, 1, 1);

    /**
     * A plane to be used in the tests.
     */
    private final Plane plane = new Plane(new Vector(0, -1, 1), qPoint);

    /**
     * A vector pointing to the x direction used in the tests.
     */
    private final Vector xVec = new Vector(1, 0, 0);

    /**
     * A vector pointing to the y direction used in the tests.
     */
    private final Vector yVec = new Vector(0, 1, 0);

    /**
     * A vector pointing to the z direction used in the tests.
     */
    private final Vector zVec = new Vector(0, 0, 1);

    /**
     * A vector orthogonal to the plane used in the tests.
     */
    private final Vector orthVec = new Vector(0, 1, -1);

    /**
     * Default constructor - only to dismiss errors in JavaDoc generator.
     */
    public PlaneTests() {
    }

    /**
     * Test method for {@link geometries.Plane#getNormal(primitives.Point)}.
     */
    @Test
    void testGetNormal() {
        // ============ Equivalence Partitions Tests ==============

        // TC01: Test that the normal of the plane is orthogonal to the plane
        Point p1 = new Point(0, 0, 0);
        Point p2 = new Point(1, 0, 0);
        Point p3 = new Point(0, 1, 0);
        Plane plane = new Plane(p1, p2, p3);

        Vector normal = plane.getNormal(p1);

        // Ensure the normal is orthogonal to the vectors formed by the points
        assertEquals(0, normal.dotProduct(p2.subtract(p1)), "ERROR: Normal is not orthogonal to the first vector");
        assertEquals(0, normal.dotProduct(p3.subtract(p1)), "ERROR: Normal is not orthogonal to the second vector");

        // Ensure the normal has length 1
        assertEquals(1, normal.length(), "ERROR: Normal is not a unit vector");
    }

    /**
     * Test method for {@link geometries.Plane#Plane(primitives.Point, primitives.Point, primitives.Point)}.
     */
    @Test
    void testConstructor() {
        // ============ Equivalence Partitions Tests ==============

        // TC01: Normal vector should be orthogonal to two vectors formed by the points and have length 1
        Point p1 = new Point(0, 0, 0);
        Point p2 = new Point(1, 0, 0);
        Point p3 = new Point(0, 2, 0);
        Plane plane = new Plane(p1, p2, p3);
        Vector normal = plane.getNormal(p1);

        // Ensure the normal is orthogonal to the vectors formed by the points
        assertEquals(0, normal.dotProduct(p2.subtract(p1)), "ERROR: Normal is not orthogonal to the first vector");
        assertEquals(0, normal.dotProduct(p3.subtract(p1)), "ERROR: Normal is not orthogonal to the second vector");

        // Ensure the normal has length 1
        assertEquals(1, normal.length(), "ERROR: Normal is not a unit vector");

        // =============== Boundary Values Tests ==================

        // TC11: First and second points are the same
        assertThrows(IllegalArgumentException.class, () -> new Plane(p1, p1, p3), "ERROR: Constructor does not throw an exception for identical first and second points");

        // TC12: First and third points are the same
        assertThrows(IllegalArgumentException.class, () -> new Plane(p1, p2, p1), "ERROR: Constructor does not throw an exception for identical first and third points");

        // TC13: Second and third points are the same
        assertThrows(IllegalArgumentException.class, () -> new Plane(p1, p2, p2), "ERROR: Constructor does not throw an exception for identical second and third points");

        // TC14: All points are the same
        assertThrows(IllegalArgumentException.class, () -> new Plane(p1, p1, p1), "ERROR: Constructor does not throw an exception for all points being identical");

        // TC15: All points are collinear but not identical
        Point p4 = new Point(2, 0, 0);
        assertThrows(IllegalArgumentException.class, () -> new Plane(p1, p2, p4), "ERROR: Constructor does not throw an exception for collinear points");
    }

    /**
     * Test method for {@link geometries.Plane#findIntersections(primitives.Ray)}.
     */
    @Test
    void testFindIntersections() {
        // Visualize the plane in 3D space: https://www.geogebra.org/calculator/gn9xcbrm

        // ======== Equivalence Partitions Tests ========

        // TC01: Ray is neither orthogonal nor parallel to the plane and intersects with it
        assertEquals(1, plane.findIntersections(new Ray(new Point(1, 0, 1), yVec)).size(), "ERROR: Ray should intersect with the plane at one point");

        // TC02: Ray is neither orthogonal nor parallel to the plane and doesn't intersect with it
        assertNull(plane.findIntersections(new Ray(new Point(1, 0, 1), yVec.scale(-1))), "ERROR: Ray shouldn't intersect with the plane");

        // ======== Boundary Value Tests ========
        // *** Group 1: Ray is parallel to the plane

        // TC11: Ray is included in the plane
        assertNull(plane.findIntersections(new Ray(new Point(2, 2, 2), xVec)), "ERROR: Ray which is included in the plane shouldn't intersect with the plane at all");

        // TC12: Ray isn't included in the plane
        assertNull(plane.findIntersections(new Ray(new Point(1, 0, 1), xVec)), "ERROR: Ray which is parallel to the plane shouldn't intersect with the plane at all");

        // *** Group 2: Ray's line is orthogonal to the plane

        // TC21: Ray's starting point is before the plane
        assertEquals(1, plane.findIntersections(new Ray(new Point(1, 0, 1), orthVec)).size(), "ERROR: Ray should intersect with the plane at one point");

        // TC22: Ray's starting point is on the plane
        assertNull(plane.findIntersections(new Ray(new Point(2, 2, 2), orthVec)), "ERROR: Ray which is orthogonal to the plane and its starting point at the plane shouldn't intersect with the plane at all");

        // TC23: Ray's starting point is after the plane
        assertNull(plane.findIntersections(new Ray(new Point(1, 2, 1), orthVec)), "ERROR: Ray which is orthogonal to the plane and its starting point after the plane shouldn't intersect with the plane at all");

        // *** Group 3: Ray is neither orthogonal nor parallel to the plane, and its starting point is at the plane

        // TC31: Ray's starting point is at the plane but not on its q point
        assertNull(plane.findIntersections(new Ray(new Point(3, 1, 1), zVec)), "ERROR: Ray which is neither orthogonal nor parallel to the plane and its starting point at the plane (Not Q) shouldn't intersect with the plane at all");

        // TC32: Ray's starting point is at the plane and at its q point
        assertNull(plane.findIntersections(new Ray(qPoint, zVec)), "ERROR: Ray which is neither orthogonal nor parallel to the plane and its starting point at the plane (Q) shouldn't intersect with the plane at all");
    }
}