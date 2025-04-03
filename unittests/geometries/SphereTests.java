package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Vector;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Unit tests for {@link geometries.Sphere}.
 */
public class SphereTests {

    /**
     * Default constructor - only to dismiss errors in JavaDoc generator.
     */
    public SphereTests() {}

    /**
     * Test method for {@link geometries.Sphere#getNormal(primitives.Point)}.
     */
    @Test
    void testGetNormal() {
        // ============ Equivalence Partitions Tests ==============

        // TC01: Test that the normal of the sphere is orthogonal to the sphere
        Point center = new Point(0, 0, 0);
        Sphere sphere = new Sphere(center, 2);

        Point p = new Point(0, 2, 0);
        Vector normal = sphere.getNormal(p);

        // Ensure the normal is orthogonal to the sphere
        assertEquals(new Vector(0, 1, 0), normal, "ERROR: Normal is not correct");

        // Ensure the normal has length 1
        assertEquals(1, normal.length(), "ERROR: Normal is not a unit vector");
    }

    /**
     * Test method for {@link geometries.Sphere#findIntersections(primitives.Ray)}.
     * Tests ray intersections with the sphere.
     */
    @Test
    void testFindIntersections() {
        // ============ Equivalence Partitions Tests ==============

        // TC01: Ray that starts before the sphere and intersects it twice

        // TC02: Ray that starts inside the sphere and intersects it once

        // TC03: Ray that starts outside the sphere and the ray goes exactly in the opposite direction of the object

        // TC04: Ray that starts outside the sphere and the ray line doesn't intersect the object on any side

        // =============== Boundary Values Tests ==================

        // *** Group 1: Ray is tangent to the sphere ***
        // TC11: Ray that starts before the sphere and is tangent to it

        // TC12: Ray that starts from the tangent point

        // TC13: Ray that starts after the sphere and is tangent to it


        // *** Group 2: Ray's starting point is orthogonal to line between it and the center of the sphere ***

        // TC14: Ray's starting point is outside the sphere

        // TC15: Ray's starting point is on the sphere


        // *** Group 3: Ray's line crosses the sphere twice ***
        // TC16: Ray's starting point is on the sphere and goes into the sphere

        // TC17: Ray's starting point is on the sphere and goes out of the sphere


        // *** Group 4: Ray's line crosses the center of the sphere ***
        // TC18: Ray's starting point is outside the sphere and goes out of the sphere

        // TC19: Ray's starting point is the center of the sphere

        // TC20: Ray's starting point is on the sphere and goes out of the sphere

        // TC21: Ray's starting point is on the sphere and goes inside the sphere

        // TC22: Ray's starting point is inside the sphere and goes out of the sphere

        // TC23: Ray's starting point is inside the sphere and goes inside the sphere
    }
}