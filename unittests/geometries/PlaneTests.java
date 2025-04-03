package geometries;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

/**
 * Unit tests for {@link geometries.Plane}.
 */
public class PlaneTests {
    private final Point p1 = new Point(0, 1, 1);
    private final Point p2 = new Point(0, 0, 2);
    private final Point p3 = new Point(0, 0, 1);
    private final Point p4 = new Point(1, 0, 0);
    private final Vector v1 = new Vector(0, 1, 1);
    private final Vector v2 = new Vector(0, 0, 1);
    private final Vector v3 = new Vector(0, 1, 0);
    private final Vector v4 = new Vector(0, 0, 1);
    private final Plane plane = new Plane(v4,p3);
    /**
     * Default constructor - only to dismiss errors in JavaDoc generator.
     */
    public PlaneTests() {}

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
     * Tests ray intersections with the plane.
     */
    @Test
    void testFindIntersections() {
        // ============ Equivalence Partitions Tests ==============

        // *** Group 1: Ray is not orthogonal and not parallel to the plane ***
        // TC01: Ray's directional vector is to the plane
        assertEquals(List.of(p1), plane.findIntersections(new Ray(p2, new Vector(0, 1, -1))), "Failed to find the intersection point when the ray intersect the plane");
        // TC02: Ray's directional vector is to the opposite direction of the plane
        assertNull(plane.findIntersections(new Ray(p2, v1)), "Failed to find the intersection point when the ray does not intersect the plane");

        // =============== Boundary Values Tests ==================
        // *** Group 1: Ray is parallel to the plane ***
        // TC11: Ray is included in the plane
        assertNull(plane.findIntersections(new Ray(p1, v3)), "Failed to find the intersection point when the ray is parallel to the plane and included in the plane");

        // TC12: Ray is not included in the plane
        assertNull(plane.findIntersections(new Ray(p2, v3)), "Failed to find the intersection point when the ray is parallel to the plane");

        // *** Group 2: Ray is orthogonal to the plane ***
        // TC13: Ray starts before the plane
        assertEquals(List.of(p3), plane.findIntersections(new Ray(new Point(0, 0, -1), v2)), "Failed to find the intersection point when the ray is orthogonal to the plane");

        // TC14: Ray starts in the plane
        assertNull(plane.findIntersections(new Ray(p3, v2)), "Failed to find the intersection point when the ray is orthogonal to the plane and start in the plane");

        // TC15: Ray starts after the plane
        assertNull(plane.findIntersections(new Ray(p2, v2)), "Failed to find the intersection point when the ray is orthogonal to the plane and start outside the plane");

        // *** Group 3: General tests ***
        // TC16: Ray starts from the plane and isn't parallel or orthogonal to it
        assertNull(plane.findIntersections(new Ray(p1, v1)), "Failed to find the intersection point when the ray start at the plane");

        // TC17: Ray starts from the q point of the plane, and it isn't nigher parallel nor orthogonal to it
        assertNull(plane.findIntersections(new Ray(p3, v1)), "Failed to find the intersection point when the ray start at the plane at the point that sent to the constructor");
    }
}