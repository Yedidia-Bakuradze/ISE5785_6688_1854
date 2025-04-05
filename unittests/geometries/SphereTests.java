package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link geometries.Sphere}.
 */
public class SphereTests {

    private final Sphere sphere = new Sphere(new Point(1,1,1),1);
    private final Vector upVec = new Vector(0,0,1);
    private final Vector diagonalVec = new Vector(1,1,0);
    private final Vector xVec = new Vector(1,0,0);
    private final Vector yVec = new Vector(0,1,0);
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
     */
    @Test
    void testFindIntersections(){
        // Visual: https://www.geogebra.org/calculator/hjc4tu2w

        // ============ Equivalence Partitions Tests ==============

        // TC01: Ray is entirely outside the sphere
        assertNull(sphere.findIntersections(new Ray(Point.ZERO, upVec)), "ERROR: Ray should not intersect with the sphere");

        // TC02: Ray's line intersects with the sphere, and its starting point is before the sphere
        assertEquals(2,sphere.findIntersections(new Ray(new Point(0,0,1),diagonalVec)).size(), "ERROR: Ray should intersect with the sphere at two point");

        // TC03: Ray's line intersects with the sphere, and its starting point is after the sphere
        assertNull(sphere.findIntersections(new Ray(new Point(2,2,1),diagonalVec)), "ERROR: Ray shouldn't intersect with the sphere at all");

        // TC04: Ray's line intersects with the sphere, and its starting point is inside the sphere
        assertEquals(1,sphere.findIntersections(new Ray(new Point(1.5,1.5,1),diagonalVec)).size(), "ERROR: Ray should intersect with the sphere at one point");
        // =============== Boundary Values Tests ==================

        // *** Group 1: Ray's line is tangent to the sphere

        // TC11: Ray's starting point is before the sphere
        assertNull(sphere.findIntersections(new Ray(new Point(0,0,1) , xVec)), "ERROR: Ray shouldn't intersect with the sphere on the tangent point");

        // TC12: Ray's starting point is on the sphere
        assertNull(sphere.findIntersections(new Ray(new Point(1,0,1), xVec)), "ERROR: Ray shouldn't intersect with the sphere on the tangent point on starting point of the ray");

        // TC13: Ray's starting point is after the sphere
        assertNull(sphere.findIntersections(new Ray(new Point(2,0,1),xVec)), "Error: Ray's back shouldn't intersect with any object");

        // *** Group 2: The ray is orthogonal to the segment of the starting point and the center of the sphere

        // TC21: Ray's starting point is outside the sphere
        assertNull(sphere.findIntersections(new Ray(new Point(1,-1,1), xVec)), "ERROR: Ray shouldn't intersect with the sphere when it doesn't directs to it");

        // TC22: Ray's starting point is inside the sphere
        assertEquals(1,sphere.findIntersections(new Ray(new Point(1,0.5,1), xVec)).size(), "ERROR: Ray shouldn't intersect with the sphere when it doesn't directs to it");


        // *** Group 3: The ray's line crosses the sphere twice (doesn't pass through the center)

        // TC31: Ray's starting point is on sphere
        assertEquals(1,sphere.findIntersections(new Ray(new Point(0.5,-Math.sqrt((double) 3 /4) + 1,1), yVec)).size(), "ERROR: Ray should intersect with the sphere at one point");

        // TC32: Ray's starting point is inside the sphere
        assertEquals(1,sphere.findIntersections(new Ray(new Point(0.5,0.5,1), yVec)).size(), "ERROR: Ray should intersect with the sphere at one point");

        // *** Group 4: The ray's line crosses the sphere in its center

        // TC41: Ray's starting point is after the sphere's center
        assertNull(sphere.findIntersections(new Ray(new Point(1,3,1),yVec)), "ERROR: Ray should not intersect with the sphere which is before it");

        // TC42: Ray's starting point is at the center of the sphere
        assertEquals(1,sphere.findIntersections(new Ray(new Point(1,1,1),yVec)).size(), "ERROR: Ray should intersect with the sphere at one point");

        // TC43: Ray's starting point is on the sphere after the center
        assertNull(sphere.findIntersections(new Ray(new Point(1,2,1),yVec)), "ERROR: Ray should not intersect with the sphere which is before it");

        // TC44: Ray's starting point is on the sphere before the center
        assertEquals(1,sphere.findIntersections(new Ray(new Point(1,0,1),yVec)).size(), "ERROR: Ray should intersect with the sphere at one point");

        // TC45: Ray's starting point is inside the sphere after the center
        assertEquals(1,sphere.findIntersections(new Ray(new Point(1,1.5,1),yVec)).size(), "ERROR: Ray should intersect with the sphere at one point");

        // TC 46: Ray's starting point is outside the sphere before the center
        assertEquals(2,sphere.findIntersections(new Ray(new Point(1,-1,1),yVec)).size(), "ERROR: Ray should intersect with the sphere at two points");
    }
}