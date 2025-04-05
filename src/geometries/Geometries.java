package geometries;

import primitives.Point;
import primitives.Ray;
import java.util.LinkedList;
import java.util.List;

public class Geometries implements Intersectable{

    private final List<Intersectable> geometries = new LinkedList<Intersectable>();

    Geometries() {}

    public Geometries(Intersectable ... intersectable) {
        this.add(intersectable);
    }

    public void add(Intersectable ... intersectable) {
        for (Intersectable i : intersectable) {
            geometries.add(i);
        }
    }

    @Override
    public List<Point> findIntersections(Ray ray) {
        return null;
    }
}
