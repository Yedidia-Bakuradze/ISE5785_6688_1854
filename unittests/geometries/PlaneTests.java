package geometries;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import primitives.Point;
import primitives.Vector;

/**
 * Unit tests for {@link geometries.Plane}.
 */
class PlaneTests {

    /**
     * Test method for {@link geometries.Plane#getNormal(primitives.Point)}.
     */
    @Test
    void testGetNormal() {
        // ============ Equivalence Partitions Tests ==============


        // =============== Boundary Values Tests ==================


        fail("No test implementation in Plane:testGetNormal");
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
}