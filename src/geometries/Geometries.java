package geometries;

import primitives.Point;
import primitives.Ray;

import java.util.LinkedList;
import java.util.List;

public class Geometries implements Intersectable{

    private List<Intersectable> geometries = new LinkedList<Intersectable>();


    Geometries(){

    }

    Geometries(Intersectable ...geometries){
        this.add(geometries);
    }

    public void add(Intersectable... intersectable){
        for (Intersectable i : intersectable) {
            this.geometries.add(i);
        }
    }

    @Override
    public List<Point> findIntersections(Ray ray) {
        List<Point> resList = null;
        for (Intersectable i : geometries) {
            List<Point> intersections = i.findIntersections(ray);
            if (intersections != null) {
                if(resList == null) {
                    resList = new LinkedList<Point>(intersections);
                    continue;
                }
                resList.addAll(intersections);
            }
        }
        return resList;
    }
}
