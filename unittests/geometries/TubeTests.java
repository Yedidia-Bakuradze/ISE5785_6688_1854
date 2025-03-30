package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests for {@link geometries.Tube}.
 */
class TubeTests {
    protected final double DELTA = 0.00001;
    /**
     * Test method for {@link geometries.Tube#getNormal(primitives.Point)}.
     */
    @Test
    void testGetNormal() {
        // ============ Equivalence Partitions Tests ==============

        // TC01: Test that the normal of the tube is orthogonal to the tube
        Ray axis = new Ray(new Point(0, 0, 0), new Vector(0, 0, 1));
        Tube tube = new Tube(axis, 2);

        Point p = new Point(2, 0, 2); // Point on the surface of the tube, not on the same line of the origin
        Vector normal = tube.getNormal(p);

        // Ensure the normal has length 1
        assertEquals(1, normal.length(), DELTA,"ERROR: Normal is not a unit vector");

        // Ensure the normal is orthogonal to the tube
        assertEquals(new Vector(1,0,0), normal, "ERROR: Normal is not correct");

        // =============== Boundary Values Tests ==================

        // TC11: Test that the normal at a point directly above the tube's axis is correct
        Point boundaryPoint = new Point(2, 0, 0); // Point directly above the axis
        normal = tube.getNormal(boundaryPoint);
        
        assertEquals(1, normal.length(), "ERROR: Normal is not a unit vector");
        assertEquals(new Vector(1, 0, 0), normal, "ERROR: Normal at boundary point is not correct");
    }
}