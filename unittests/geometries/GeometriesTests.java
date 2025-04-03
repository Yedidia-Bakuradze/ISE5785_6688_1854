package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.*;

class GeometriesTests {

    @Test
    void testAdd() {
        // Create a new Geometries object
        Geometries geometries = new Geometries();
        
        // Test that initially it has no geometries
        assertNull(geometries.findIntersections(new Ray(new Point(0, 0, 0), new Vector(1, 0, 0))));
        
        // Add a sphere and test that it exists
        geometries.add(new Sphere(new Point(2, 0, 0), 1));
        assertNotNull(geometries.findIntersections(new Ray(new Point(0, 0, 0), new Vector(1, 0, 0))));
    }

    @Test
    void testFindIntersections() {
        Geometries emptyGeometries = new Geometries();
        Geometries geometries = new Geometries(
            // Sphere at x=2 with radius 1
            new Sphere(new Point(2, 0, 0), 1),
            
            // Plane at x=4 perpendicular to X-axis
            new Plane(new Vector(1, 0, 0),new Point(4, 0, 0)),
            
            // Triangle at x=6 in YZ plane
            new Triangle(new Point(6, -1, -1), new Point(6, 1, -1), new Point(6, 0, 1)),
            
            // Polygon at x=8 in YZ plane
            new Polygon(
                new Point(8, -1, -1),
                new Point(8, 1, -1),
                new Point(8, 1, 1),
                new Point(8, -1, 1)
            ),
            
            // Tube along X-axis centered at y=3, z=0 with radius 0.5
            new Tube(new Ray(new Point(0, 3, 0), new Vector(0, -1, 0)), 0.5),
            
            // Cylinder along X-axis from x=10 to x=12, centered at y=0, z=0 with radius 1
            new Cylinder(new Ray(new Point(10, 0, 0), new Vector(1, 0, 0)), 1, 2)
        );

        // ============ Equivalence Partitions Tests ==============
        // TC01: Some shapes (but not all) intersect
        Ray ray = new Ray(new Point(6, 4, 0), new Vector(-4, -4, 0));
        assertEquals(5, geometries.findIntersections(ray).size(), "EP: Ray intersects some but not all geometries");

        // =============== Boundary Values Tests ==================
        // TC11: Empty collection
        ray = new Ray(new Point(0, 0.5, 0), new Vector(1, 0, 0));
        assertEquals(0,emptyGeometries.findIntersections(ray).size(), "BVA: The ray should have been intersected with zero objects");

        // TC12: No shape intersects
        ray = new Ray(new Point(-1, 0, 0), new Vector(0, 0, 1));
        assertEquals(0,geometries.findIntersections(ray).size(), "BVA: The ray should have been intersected with zero objects");

        // TC13: Only one shape intersects (The plane)
        ray = new Ray(new Point(-2, 0, 0), new Vector(-1, 0, 0));
        assertEquals(1, geometries.findIntersections(ray).size(), "BVA: Ray intersects must be only one geometry");

        // TC14: All shapes intersect
        ray = new Ray(new Point(15, 0, 0), new Vector(-1, 0, 0));
        assertEquals(9, geometries.findIntersections(ray).size(), "BVA: Ray intersects all geometries");
    }
}