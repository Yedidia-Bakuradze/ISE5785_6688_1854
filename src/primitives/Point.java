package primitives;

import java.security.InvalidParameterException;

public class Point {
    protected final Double3 xyz;

    public static Point ZERO = new Point(Double3.ZERO);

    public Point(double x, double y, double z) {
        xyz = new Double3(x,y,z);
    }

    public Point(Double3 xyz) {
        this.xyz = xyz;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        return obj instanceof Point point && point.xyz.equals(xyz);
    }

    @Override
    public String toString() {
        return "Point: " + xyz.toString();
    }

    public Point add(Vector addVec) {
        return new Point(this.xyz.add(addVec.xyz));
    }

    public Vector subtract(Point rightPoint) {
        return new Vector(this.xyz.subtract(rightPoint.xyz));
    }

    public double distanceSquared(Point rightPoint) {
        Double3 tmp = this.xyz.subtract(rightPoint.xyz);
        return tmp.d1() * tmp.d1() + tmp.d2() * tmp.d2() + tmp.d3() * tmp.d3();
    }

    public double distance(Point rightPoint) {
        return Math.sqrt(distanceSquared(rightPoint));
    }



}
