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
        Sphere sphere = new Sphere(new Point(0,0,1),2);
        // ============ Equivalence Partitions Tests ==============
        Ray ray;
        
        // TC01: Ray that starts before the sphere and intersects it twice
        ray = new Ray(new Point(2,2,1),new Vector(-2,-2,0));
        assertEquals(2, sphere.findIntersections(ray).size(), "Error: Ray that starts before the sphere and intersects it twice has not found two points");
        
        // TC02: Ray that starts inside the sphere and intersects it once
        ray = new Ray(new Point(0,0,1.5),new Vector(1,1,-0.5));
        assertEquals(1, sphere.findIntersections(ray).size(), "Error: Ray starts from inside and didn't find one point");

        // TC03: Ray that starts outside the sphere and the ray goes exactly in the opposite direction of the object
        ray = new Ray(new Point(2,2,1),new Vector(2,2,0));
        assertEquals(0, sphere.findIntersections(ray).size(), "Error: Ray starts outside of the sphere and goes in the opposite direction found some intersections");

        // TC04: Ray that starts outside the sphere and the ray line doesn't intersect the object on any side
        ray = new Ray(new Point(0,3,0),new Vector(0,0,1));
        assertEquals(0, sphere.findIntersections(ray).size(), "Error: Ray starts outside of the sphere and doesn't direct at all to the sphere, found some intersections");

        // =============== Boundary Values Tests ==================

        // *** Group 1: Ray is tangent to the sphere ***
        // TC11: Ray that starts before the sphere and is tangent to it

        // TC12: Ray that starts from the tangent point

        // TC13: Ray that starts after the sphere and is tangent to it


        // *** Group 2: Ray's starting point is orthogonal to line between it and the center of the sphere ***

        // TC21: Ray's starting point is outside the sphere

        // TC22: Ray's starting point is on the sphere


        // *** Group 3: Ray's line crosses the sphere twice ***
        // TC31: Ray's starting point is on the sphere and goes into the sphere

        // TC32: Ray's starting point is on the sphere and goes out of the sphere


        // *** Group 4: Ray's line crosses the center of the sphere ***
        // TC41: Ray's starting point is outside the sphere and goes out of the sphere

        // TC42: Ray's starting point is the center of the sphere

        // TC43: Ray's starting point is on the sphere and goes out of the sphere

        // TC44: Ray's starting point is on the sphere and goes inside the sphere

        // TC45: Ray's starting point is inside the sphere and goes out of the sphere

        // TC46: Ray's starting point is inside the sphere and goes inside the sphere


        fail("Test not implemented yet");
    }
}