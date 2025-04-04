package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.*;

class TriangleTests {

    private final Triangle triangle = new Triangle(new Point(1,1,1),new Point(3,1,1),new Point(2,2,3));
    private final Vector yVec = new Vector(0,1,0);
    /**
     * Tests the {@link Triangle#findIntersections(primitives.Ray ray)} method.
     */
    @Test
    void testFindIntersections() {
        // Tests: https://www.geogebra.org/calculator/n5fnz3mq

        // =========== Equivalence Partitions Tests ===========
        // TC01: Ray's interception point is within the plane that contains the triangle inside the triangle.
        assertEquals(1,triangle.findIntersections(new Ray(new Point(2,0,2),yVec)).size(), "ERROR: Ray should intersect with the sphere");

        // TC02: Ray's interception point is within the plane that contains the triangle, outside the triangle in front of a triangle's side.
        assertNull(triangle.findIntersections(new Ray(new Point(1,0,2),yVec)), "ERROR: Ray shouldn't intersect with the sphere");

        // TC03: Ray's interception point is within the plane that contains the triangle, outside the triangle in front of a triangle's vertex.
        assertNull(triangle.findIntersections(new Ray(new Point(2,0,3.5),yVec)), "ERROR: Ray shouldn't intersect with the sphere");

        // =========== Boundary Value Tests ===========

        // TC11: Ray's interception point is within the plane that contains the triangle, on one of the vertexes.
        assertNull(triangle.findIntersections(new Ray(new Point(2,0,3),yVec)), "ERROR: Ray shouldn't intersect with the sphere");

        // TC12: Ray's interception point is within the plane that contains the triangle on one of the sides.
        assertNull(triangle.findIntersections(new Ray(new Point(2,0,1),yVec)), "ERROR: Ray shouldn't intersect with the sphere");

        // TC13 Ray's interception point is within the plane that contains the triangle, on one of the side's line outside the triangle area.
        assertNull(triangle.findIntersections(new Ray(new Point(4,0,1),yVec)), "ERROR: Ray shouldn't intersect with the sphere");
    }
}