package primitives;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link primitives.Point}.
 */
class PointTests {
    /**
     * Test method for {@link primitives.Point#add(primitives.Vector)}.
     */
    @Test
    void testAdd() {
        // ============ Equivalence Partitions Tests ==============

        // TC01: Test that adding a positive vector to a point results the right coordinate point
        Point p1 = new Point(1, 2, 3);
        Vector v1 = new Vector(1, 1, 1);
        Point expected = new Point(2, 3, 4);
        assertEquals(expected, p1.add(v1), "ERROR: Adding a positive vector to a point is incorrect");

        // TC02: Test that adding a negative vector to a point results the right coordinate point
        p1 = new Point(1, 2, 3);
        v1 = new Vector(-1, -1, -1);
        expected = new Point(0, 1, 2);
        assertEquals(expected, p1.add(v1), "ERROR: Adding a negative vector to a point is incorrect");

        // =============== Boundary Values Tests ==================

        // TC11: Test that adding a zero vector to a point results the point itself
        assertEquals(p1, p1.add(new Vector(0,0,0)), "ERROR: Adding a zero vector to a point does not return the same point");

        fail("No test implementation in Point:testAdd");
        // TC12: Test that adding opposite coordinate vector to a point results the origin point
        v1 = new Vector(-1,-2,-3);
        assertEquals(Vector.ZERO, p1.add(v1), "ERROR: Adding a zero vector to a point does not return the same point");
    }

    /**
     * Test method for {@link primitives.Point#subtract(primitives.Point)}.
     */
    @Test
    void testSubtract() {
        // ============ Equivalence Partitions Tests ==============


        // =============== Boundary Values Tests ==================


        fail("No test implementation in Point:testSubtract");
    }

    /**
     * Test method for {@link primitives.Point#distanceSquared(primitives.Point)}.
     */
    @Test
    void testDistanceSquared() {
        // ============ Equivalence Partitions Tests ==============


        // =============== Boundary Values Tests ==================


        fail("No test implementation in Point:testDistanceSquared");
    }

    /**
     * Test method for {@link primitives.Point#distance(primitives.Point)}.
     */
    @Test
    void testDistance() {
        // ============ Equivalence Partitions Tests ==============


        // =============== Boundary Values Tests ==================


        fail("No test implementation in Point:testDistance");
    }
}