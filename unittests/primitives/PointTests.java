package primitives;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit tests for {@link primitives.Point}.
 */
public class PointTests {
    /**
     * Default constructor - only to dismiss errors in JavaDoc generator.
     */
    public PointTests() {
    }

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

        v1 = new Vector(-1, -1, -1);
        expected = new Point(0, 1, 2);
        assertEquals(expected, p1.add(v1), "ERROR: Adding a negative vector to a point is incorrect");

        // =============== Boundary Values Tests ==================

        // TC11: Test that adding opposite coordinate vector to a point results the origin point
        assertEquals(Point.ZERO, new Point(1, 2, 3).add(new Vector(-1, -2, -3)), "ERROR: Adding a zero vector to a point does not return the same point");
    }

    /**
     * Test method for {@link primitives.Point#subtract(primitives.Point)}.
     */
    @Test
    void testSubtract() {
        // ============ Equivalence Partitions Tests ==============
        Point p1 = new Point(3, 2, 1);
        Point p2 = new Point(1, 1, 1);
        Vector expected = new Vector(2, 1, 0);
        assertEquals(expected, p1.subtract(p2), "ERROR: subtract() does not return the correct result");

        // =============== Boundary Values Tests ==================
        assertThrows(IllegalArgumentException.class, () -> p1.subtract(p1), "ERROR: subtract() should throw exception for identical points");
    }

    /**
     * Test method for {@link primitives.Point#distanceSquared(primitives.Point)}.
     */
    @Test
    void testDistanceSquared() {
        // ============ Equivalence Partitions Tests ==============
        Point p1 = new Point(1, 2, 3);
        Point p2 = new Point(4, 6, 3);
        assertEquals(25, p1.distanceSquared(p2), "ERROR: distanceSquared() is incorrect");

        // =============== Boundary Values Tests ==================
        assertEquals(0, p1.distanceSquared(p1), "ERROR: distanceSquared() for the same point should be 0");
    }


    /**
     * Test method for {@link primitives.Point#distance(primitives.Point)}.
     */
    @Test
    void testDistance() {
        // ============ Equivalence Partitions Tests ==============
        // TODO: Add comments
        Point p1 = new Point(1, 2, 3);
        Point p2 = new Point(4, 6, 3);
        assertEquals(5, p1.distance(p2), "ERROR: distance() is incorrect");

        // =============== Boundary Values Tests ==================
        assertEquals(0, p1.distance(p1), "ERROR: distance() for the same point should be 0");
    }
}




