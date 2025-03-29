package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Vector;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests for {@link geometries.Sphere}.
 */
class SphereTests {

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
}