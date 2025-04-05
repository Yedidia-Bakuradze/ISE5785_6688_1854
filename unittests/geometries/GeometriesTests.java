package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link Geometries} class.
 */
class GeometriesTests {

    /**
     * Default constructor - only to dismiss errors in JavaDoc generator.
     */
    GeometriesTests() {
    }

    // ============ <b>Global Fields</b> ============

    /**
     * A collection of geometries to be used in the tests.
     */
    private final Geometries geometries = new Geometries(
            new Sphere(new Point(1, -4, 1), 1),
            new Plane(new Vector(0, -1, 1), new Point(1, 1, 1)),
            new Triangle(new Point(0, -6, 0), new Point(2, -6, 0), new Point(1, -5, 2))
    );

    /**
     * A diagonal (diagonal respected to x,y values) vector used in the tests.
     */
    private final Vector diagonal = new Vector(1, 1, 0);

    /**
     * A vector pointing in the y direction used in the tests.
     */
    private final Vector yVec = new Vector(0, 1, 0);

    /**
     * Test method for {@link geometries.Geometries#findIntersections(primitives.Ray)}.
     */
    @Test
    void testFindIntersections() {
        // Visual: https://www.geogebra.org/calculator/mdjktwhs

        // ======== Equivalence Partitions Tests: ========
        // TC01: Some objects - but not all of them - were intersected
        assertEquals(3, geometries.findIntersections(new Ray(new Point(-1, -6, 1), diagonal)).size(), "Error: Expected to hit twice the Sphere and once the Plane");

        // ======== Boundary Value Tests: ========
        // TC11: List of objects is empty
        assertNull(new Geometries().findIntersections(new Ray(new Point(-1, -6, 1), diagonal)), "Error: Expected to hit nothing since there is nothing...");

        // TC12: No object has been intersected
        assertNull(geometries.findIntersections(new Ray(new Point(-1, -6, 1), diagonal.scale(-1))), "Error: Expected to hit nothing");

        // TC13: All objects were intersected
        assertEquals(4, geometries.findIntersections(new Ray(new Point(1, -7, 1), yVec)).size(), "Error: Expected to hit all objects (1: Triangle, 2: Sphere, 1: Plane)");

        // TC14: Only one object has been intersected
        assertEquals(1, geometries.findIntersections(new Ray(new Point(1, -2, 1), yVec)).size(), "Error: Expected to only the Plane");
    }
}