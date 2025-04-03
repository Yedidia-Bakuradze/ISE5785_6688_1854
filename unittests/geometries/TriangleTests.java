package geometries;

import org.junit.jupiter.api.Test;
import primitives.Ray;

import static org.junit.jupiter.api.Assertions.*;

class TriangleTests {

    /**
     * Test method for {@link geometries.Triangle#findIntersections(Ray)}.
     * Tests ray intersections with the triangle.
     */
    @Test
    void testFindIntersections() {
        // ============ Equivalence Partitions Tests ==============

        // TC01: Ray intersects the triangle

        // TC02: Ray does not intersect the triangle

        // TC03: Ray does not intersect the triangle and its in front of a vertex of the triangle


        // =============== Boundary Values Tests ==================

        // TC11: Ray is interacting the vertex of the triangle

        // TC12: Ray is interacting the edge of the triangle

        // TC13: Ray is interacting the line of the edge of the triangle where its not a part of the edge

        fail("Test not implemented yet");
    }
}