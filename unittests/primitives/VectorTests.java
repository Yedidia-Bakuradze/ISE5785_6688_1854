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

        //TC01: test that length of the dot-product of vectors with less than 90 degrees angle (45d) is correct
        Vector vec1 = new Vector(1,0,0);
        Vector vec2 = new Vector(1,0,-1);
        assertEquals(1, vec1.dotProduct(vec2), "ERROR: dotProduct() result on vectors with angle less than 90d is not correct");

        //TC02: test that length of the dot-product of vectors with a degree higher than 90 degrees angle (135d) is correct
        vec1 = new Vector(1,0,1);
        vec2 = new Vector(-1,0,-1);
        assertEquals(-2, vec1.dotProduct(vec2), "ERROR: dotProduct() result on vectors with angle more than 90d is not correct");

        // =============== Boundary Values Tests ==================

        //TC11: test that a dot-product of orthogonal vectors is zero
        vec1 = new Vector(1,4,-2);
        vec2 = new Vector(4,0,2);
        assertEquals(0, vec1.dotProduct(vec2), "ERROR: dotProduct() result of orthogonal vectors is not zero");

        //TC12: test that a dot-product of vectors which one of them is a unit vector
        vec1 = new Vector(1,0,0);
        vec2 = new Vector(4,12,3);
        assertEquals(4, vec1.dotProduct(vec2), "ERROR: dotProduct() result of unit vector and random vector is not correct");
    }

    /**
     * Test method for {@link primitives.Vector#crossProduct(primitives.Vector)}.
     */
    @Test
    void testCrossProduct() {
        float DELTA = 0.00001f;
        // ============ Equivalence Partitions Tests ==============

        //TC01: test that the result of the cross-product of vectors with less than 90 degrees angle (45d) is correct (length and orthogonality)
        Vector vec1 = new Vector(1,0,0);
        Vector vec2 = new Vector(1,0,-1);
        Vector vr = vec1.crossProduct(vec2);
        assertEquals(vec1.length()*vec2.length()*Math.sin(45), vr.length(), DELTA, "ERROR: crossProduct() wrong result length");
        assertEquals(0, vr.dotProduct(vec1), "ERROR: crossProduct() result is not orthogonal to 1st operand");
        assertEquals(0, vr.dotProduct(vec2), "ERROR: crossProduct() result is not orthogonal to 2nd operand");


        //TC02: test that the result of the cross-product of vectors with more than 90 degrees angle (135d) is correct (length and orthogonality)
        vec1 = new Vector(1,0,1);
        vec2 = new Vector(-1,0,-1);
        vr = vec1.crossProduct(vec2);
        assertEquals(vec1.length()*vec2.length()*Math.sin(135), vr.length(), DELTA, "ERROR: crossProduct() wrong result length");
        assertEquals(0, vr.dotProduct(vec1), "ERROR: crossProduct() result is not orthogonal to 1st operand");
        assertEquals(0, vr.dotProduct(vec2), "ERROR: crossProduct() result is not orthogonal to 2nd operand");

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