package primitives;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link primitives.Vector}.
 */
class VectorTests {

    /**
     * Test method for {@link primitives.Vector#length()}.
     */
    @Test
    void testLength() {
        // ============ Equivalence Partitions Tests ==============


        // =============== Boundary Values Tests ==================


        fail("No test implementation in Vector:testLength");
    }

    /**
     * Test method for {@link primitives.Vector#lengthSquared()}.
     */
    @Test
    void testLengthSquared() {
        // ============ Equivalence Partitions Tests ==============


        // =============== Boundary Values Tests ==================


        fail("No test implementation in Vector:testLengthSquared");
    }

    /**
     * Test method for {@link primitives.Vector#add(primitives.Vector)}.
     */
    @Test
    void testAdd() {
        // ============ Equivalence Partitions Tests ==============


        // =============== Boundary Values Tests ==================


        fail("No test implementation in Vector:testAdd");
    }

    /**
     * Test method for {@link primitives.Vector#add(primitives.Vector)}.
     */
    @Test
    void testSubtract() {
        // ============ Equivalence Partitions Tests ==============


        // =============== Boundary Values Tests ==================


        fail("No test implementation in Vector:testSubtract");
    }

    /**
     * Test method for {@link primitives.Vector#scale(double)}.
     */
    @Test
    void testScale() {
        // ============ Equivalence Partitions Tests ==============


        // =============== Boundary Values Tests ==================


        fail("No test implementation in Vector:testScale");
    }

    /**
     * Test method for {@link primitives.Vector#dotProduct(primitives.Vector)}.
     */
    @Test
    void testDotProduct() {
        // ============ Equivalence Partitions Tests ==============


        // =============== Boundary Values Tests ==================


        fail("No test implementation in Vector:testDotProduct");
    }

    /**
     * Test method for {@link primitives.Vector#crossProduct(primitives.Vector)}.
     */
    @Test
    void testCrossProduct() {
        float DELTA = 0.00001f;
        // ============ Equivalence Partitions Tests ==============

        //TC01: test that length of the cross-product of vectors with less than 90 degrees angle (45d) is correct
        Vector vec1 = new Vector(1,0,0);
        Vector vec2 = new Vector(1,0,-1);
        Vector vr = vec1.crossProduct(vec2);
        assertEquals(vec1.length()*vec2.length()*Math.sin(45), vr.length(), DELTA, "ERROR: crossProduct() wrong result length");

        //TC02: test that length of the cross-product of vectors with a degree higher than 90 degrees angle is correct
        vec1 = new Vector(1,0,1);
        vec2 = new Vector(-1,0,-1);
        vr = vec1.crossProduct(vec2);
        assertEquals(vec1.length()*vec2.length()*Math.sin(135), vr.length(), DELTA, "ERROR: crossProduct() wrong result length");

        //TC02: test that length of the cross-product of vectors with a degree equally 90 degrees angle is correct
        vec1 = new Vector(1,0,0);
        vec2 = new Vector(1,1,0);
        vr = vec1.crossProduct(vec2);
        assertEquals(vec1.length()*vec2.length()*1, vr.length(), DELTA, "ERROR: crossProduct() wrong result length");


        // =============== Boundary Values Tests ==================

        //TC11: test that a cross-product of parallel vectors throws an exception
        assertThrows(IllegalArgumentException.class ,()->new Vector(1,0,0).crossProduct(new Vector(3,0,0)), "ERROR: crossProduct() for parallel vectors does not throw an exception");

        //TC12: test that a cross-product of negating vectors with the same size equals to zero
        Vector vec3 = new Vector(1,2,3);
        Vector vec4 = new Vector(-1,-2,-3);
        assertEquals(Vector.ZERO,vec3.crossProduct(vec4), "ERROR: crossProduct() for negating vectors with same sizes is not a zero vector");

        //TC13: test that a cross-product of negating vectors with the different sizes equals to zero
        vec3 = new Vector(7,0,0);
        vec4 = new Vector(-5,0,0);
        assertEquals(Vector.ZERO,vec3.crossProduct(vec4), "ERROR: crossProduct() for negating vectors with different sizes is not a zero vector");

        //TC14: test that a cross-product of negating vectors with the different sizes equals to zero
        vec3 = new Vector(7,7,0);
        vec4 = new Vector(7,7,0);
        assertEquals(Vector.ZERO,vec3.crossProduct(vec4), "ERROR: crossProduct() for vectors with same sizes and same direction is not a zero vector");
    }

    /**
     * Test method for {@link primitives.Vector#normalize()}.
     */
    @Test
    void testNormalize() {
        // ============ Equivalence Partitions Tests ==============


        // =============== Boundary Values Tests ==================


        fail("No test implementation in Vector:testNormalize");
    }


}