package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Unit tests for {@link geometries.Cylinder}.
 */
public class CylinderTests {
    /**
     * Default constructor - only to dismiss errors in JavaDoc generator.
     */
    public CylinderTests() {
    }

    /**
     * Test method for {@link geometries.Cylinder#getNormal(primitives.Point)}.
     */
    @Test
    void testGetNormal() {
        // ============ Equivalence Partitions Tests ==============

        // TC01: Test that the normal on the lateral surface of the cylinder is correct
        Vector axisVec = new Vector(0, 0, 1);
        Point axisBase = new Point(0, 0, 0);
        Ray axis = new Ray(axisBase, axisVec);
        Cylinder cylinder = new Cylinder(axis, 1, 2);

        Point p = new Point(1, 0, 1.5); // Point on the lateral surface
        Vector normal = cylinder.getNormal(p);

        // Ensure the normal is orthogonal to the lateral surface
        assertEquals(new Vector(1, 0, 0), normal, "ERROR: Normal on the lateral surface is not correct");

        // Ensure the normal has length 1
        assertEquals(1, normal.length(), "ERROR: Normal is not a unit vector");

        // TC02: Test that the normal on the bottom base is correct
        Point bottomBasePoint = new Point(0.5, 0.5, 0); // Point on the bottom base
        normal = cylinder.getNormal(bottomBasePoint);
        assertEquals(axisVec, normal, "ERROR: Normal on the bottom base is not correct");

        // TC03: Test that the normal on the top base is correct
        Point topBasePoint = new Point(0.5, 0.5, 2); // Point on the top base
        normal = cylinder.getNormal(topBasePoint);
        assertEquals(axisVec, normal, "ERROR: Normal on the top base is not correct");

        // =============== Boundary Values Tests ==================

        // TC11: Test that the normal at the center of the bottom base is correct
        Point bottomCenter = new Point(0, 0, 0); // Center of the bottom base
        normal = cylinder.getNormal(bottomCenter);
        assertEquals(axisVec, normal, "ERROR: Normal at the center of the bottom base is not correct");

        // TC12: Test that the normal at the center of the top base is correct
        Point topCenter = new Point(0, 0, 2); // Center of the top base
        normal = cylinder.getNormal(topCenter);
        assertEquals(axisVec, normal, "ERROR: Normal at the center of the top base is not correct");

        // TC13: Test that the normal at the edge between the lateral surface and the bottom base is correct
        Point bottomEdge = new Point(1, 0, 0); // Edge point
        normal = cylinder.getNormal(bottomEdge);
        assertEquals(new Vector(1, 0, 0), normal, "ERROR: Normal at the edge between the lateral surface and the bottom base is not correct");

        // TC14: Test that the normal at the edge between the lateral surface and the top base is correct
        Point topEdge = new Point(1, 0, 2); // Edge point
        normal = cylinder.getNormal(topEdge);
        assertEquals(new Vector(1, 0, 0), normal, "ERROR: Normal at the edge between the lateral surface and the top base is not correct");
    }

    /**
     * Test method for {@link geometries.Cylinder#findIntersections(primitives.Ray)}.
     */
    @Test
    void testFindIntersections() {
        fail("Not yet implemented");
    }
}