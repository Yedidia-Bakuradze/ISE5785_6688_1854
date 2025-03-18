package primitives;

/**
 * Represents a vector in 3D space.
 * Provides methods for vector arithmetic and operations such as dot product and cross product.
 */
public class Vector extends Point{

    public Vector(double x, double y, double z) {
        super(x, y, z);
        if(xyz.equals(Double3.ZERO))
            throw new IllegalArgumentException("Vector zero is not allowed");
    }

    public Vector(Double3 xyz) {
        super(xyz);
        if(xyz.equals(Double3.ZERO))
            throw new IllegalArgumentException("Vector zero is not allowed");
    }


    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;
        return obj instanceof Vector vec && (vec.xyz.equals(xyz));
    }

    @Override
    public String toString() {
        return "Vector: "+ super.toString();
    }

    public double length() {
        return Math.sqrt(lengthSquared());
    }

    public double lengthSquared() {
        return this.dotProduct(this);
    }

    public Vector add(Vector rightVector) {
        return new Vector(xyz.add(rightVector.xyz));
    }

    public Vector scale(double scaleFactor) {
        return new Vector(xyz.scale(scaleFactor));
    }

    public double dotProduct(Vector rightVec){
        return this.xyz.d1() * rightVec.xyz.d1()
             + this.xyz.d2() * rightVec.xyz.d2()
             + this.xyz.d3() * rightVec.xyz.d3();
    }

    public Vector crossProduct(Vector rightVec){
        //Check if right vector is zero
        if(rightVec.equals(Point.ZERO)) throw new IllegalArgumentException("Cross-Product of zero vectors is not allowed");

        //Check if vectors are parallel
        if(this.xyz.d1() / rightVec.xyz.d1() == this.xyz.d2() / rightVec.xyz.d2() && this.xyz.d2() / rightVec.xyz.d2() == this.xyz.d3() / rightVec.xyz.d3())
            throw new IllegalArgumentException("Cross-Product of parallel vectors is not allowed");

        return  new Vector(
                this.xyz.d2()*rightVec.xyz.d3() - this.xyz.d3()*rightVec.xyz.d2(),
                this.xyz.d2()* rightVec.xyz.d1() - this.xyz.d1()* rightVec.xyz.d3(),
                this.xyz.d1()*rightVec.xyz.d2() - this.xyz.d2()*rightVec.xyz.d1()
                );
    }

    public Vector normalize(){
        return new Vector(xyz.scale(1/this.length()));
    }
}
