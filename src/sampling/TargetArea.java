package sampling;

import primitives.Point;

import java.util.*;

public class TargetArea {
    protected final RayBeamSpreadingMode spreadingMode;
    protected final SuperSamplingMode superSamplingMode;
    protected final TargetAreaShape shape;
    protected final double radius;
    protected final Random rnd = new Random();

    public TargetArea(RayBeamSpreadingMode spreadingMode, SuperSamplingMode superSamplingMode, TargetAreaShape shape, double radius) {
        this.spreadingMode = spreadingMode;
        this.superSamplingMode = superSamplingMode;
        this.shape = shape;
        this.radius = radius;
    }

    /**
     * Generate 2D sample offsets in the range [-radius...+radius] in the tangent plane.
     *
     * @return list of (x,y) offsets for beam generation
     */
    public List<Point> getSamples(Point center) {
        return shape == TargetAreaShape.CIRCLE ? sampleCircle(center) : sampleSquare(center);
    }

    // Generate points inside a circle
    private List<Point> sampleCircle(Point center) {
        int count = superSamplingMode.gridSize * superSamplingMode.gridSize;
        List<Point> list = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            double u = rnd.nextDouble();
            double v = rnd.nextDouble();
            double r = Math.sqrt(u) * radius;
            double theta = 2 * Math.PI * v;
            double dx = r * Math.cos(theta);
            double dy = r * Math.sin(theta);
            list.add(new Point(center.getX() + dx, center.getY() + dy));
        }
        return list;
    }

    // Generate points inside a square ([-radius..+radius] x [-radius..+radius])
    private List<Point> sampleSquare(Point center) {
        return spreadingMode == RayBeamSpreadingMode.JITTER
                ? sampleSquareJitter(center)
                : sampleSquareUniform(center);
    }

    private List<Point> sampleSquareJitter(Point center) {
        int grid = superSamplingMode.gridSize;
        double cellSize = (2 * radius) / grid;
        List<Point> list = new ArrayList<>(grid * grid);
        for (int i = 0; i < grid; i++) {
            for (int j = 0; j < grid; j++) {
                double dx = -radius + (i + rnd.nextDouble()) * cellSize;
                double dy = -radius + (j + rnd.nextDouble()) * cellSize;
                list.add(new Point(center.getX() + dx, center.getY() + dy));
            }
        }
        return list;
    }

    private List<Point> sampleSquareUniform(Point center) {
        int grid = superSamplingMode.gridSize;
        double cellSize = (2 * radius) / grid;
        List<Point> list = new ArrayList<>(grid * grid);
        for (int i = 0; i < grid; i++) {
            for (int j = 0; j < grid; j++) {
                double dx = -radius + (i + 0.5) * cellSize;
                double dy = -radius + (j + 0.5) * cellSize;
                list.add(new Point(center.getX() + dx, center.getY() + dy));
            }
        }
        return list;
    }
}
