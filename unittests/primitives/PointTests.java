package primitives;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link primitives.Point}.
 */
class PointTests {

    /**
     * Test method for {@link primitives.Point#equals(Object)}.
     */
    @Test
    void testEquals() {
        // ============ Equivalence Partitions Tests ==============


        // =============== Boundary Values Tests ==================


        fail("No test implementation in Point:testEquals");
    }

    /**
     * Test method for {@link primitives.Point#toString()}.
     */
    @Test
    void testToString() {
        // ============ Equivalence Partitions Tests ==============


        // =============== Boundary Values Tests ==================


        fail("No test implementation in Point:testToString");
    }

    /**
     * Test method for {@link primitives.Point#add(primitives.Vector)}.
     */
    @Test
    void testAdd() {
        // ============ Equivalence Partitions Tests ==============


        // =============== Boundary Values Tests ==================


        fail("No test implementation in Point:testAdd");
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
        Point p1 = new Point(1, 2, 3);
        Point p2 = new Point(4, 6, 3);
        assertEquals(5, p1.distance(p2), "ERROR: distance() is incorrect");

        // =============== Boundary Values Tests ==================
        assertEquals(0, p1.distance(p1), "ERROR: distance() for the same point should be 0");
    }
}