package primitives;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link Ray} class.
 */
class RayTests {

    /**
     * Default constructor - only to dismiss errors in JavaDoc generator.
     */
    RayTests() {
    }

    // ============ <b>Global Fields</b> ============
    /**
     * A ray to be used in the tests.
     */
    private final Ray ray = new Ray(new Point(1, 0, 0), new Vector(1, 0, 0));

    /**
     * Test method for {@link Ray#getPoint(double)}.
     */
    @Test
    void testGetPoint() {
        // ======== Equivalence Partitions Tests ========
        // TC01: Test for positive values
        assertEquals(new Point(2, 0, 0), ray.getPoint(1), "ERROR: getPoint() for positive value is incorrect");

        // TC02: Test for negative values
        assertEquals(new Point(-1, 0, 0), ray.getPoint(-2), "ERROR: getPoint() for negative value is incorrect");

        // ======== Boundary Value Tests ========
        // TC11: Test for zero t value
        assertEquals(new Point(1, 0, 0), ray.getPoint(0), "ERROR: getPoint() for zero value is incorrect");

        // TC12: Test for t value that resets the point (goes from 1,1,1 to 0,0,0 by vector -1,-1,-1)
        assertEquals(Point.ZERO, ray.getPoint(-1), "ERROR: getPoint() for t value that resets the point is incorrect");
    }
}