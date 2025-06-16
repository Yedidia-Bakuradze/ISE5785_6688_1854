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
    public List<Point> getSamples() {
        return shape == TargetAreaShape.CIRCLE ? sampleCircle() : sampleSquare();
    }

    // Generate points inside a circle
    private List<Point> sampleCircle() {
        List<Point> list = new ArrayList<>(superSamplingMode.gridSizeSquared);
        for (int i = 0; i < superSamplingMode.gridSizeSquared; i++) {
            // polar sampling for uniform disk
            double u = rnd.nextDouble();
            double v = rnd.nextDouble();
            double r = Math.sqrt(u) * radius;
            double theta = 2 * Math.PI * v;
            double x = r * Math.cos(theta);
            double y = r * Math.sin(theta);
            list.add(new Point(x, y));
        }
        return list;
    }

    // Generate points inside a square ([-radius..+radius] x [-radius..+radius])
    private List<Point> sampleSquare() {
        int grid = superSamplingMode.gridSize;
        int count = superSamplingMode.gridSizeSquared;
        List<Point> list = new ArrayList<>(count);
        double cellSize = (2 * radius) / grid;

        return spreadingMode == RayBeamSpreadingMode.JITTER
                ? sampleSquareJitter()
                : sampleSquareUniform();
    }

    private List<Point> sampleSquareJitter() {
        List<Point> list = new ArrayList<>(superSamplingMode.gridSizeSquared);
        double cellSize = (2 * radius) / superSamplingMode.gridSize;
        for (int i = 0; i < superSamplingMode.gridSize; i++) {
            for (int j = 0; j < superSamplingMode.gridSize; j++) {
                double x = -radius + (i + rnd.nextDouble()) * cellSize;
                double y = -radius + (j + rnd.nextDouble()) * cellSize;
                list.add(new Point(x, y));
            }
        }
        return list;
    }

    public List<Point> sampleSquareUniform() {
        List<Point> list = new ArrayList<>(superSamplingMode.gridSizeSquared);
        double cellSize = (2 * radius) / superSamplingMode.gridSize;
        for (int i = 0; i < superSamplingMode.gridSize; i++) {
            for (int j = 0; j < superSamplingMode.gridSize; j++) {
                double x = -radius + (i + 0.5) * cellSize;
                double y = -radius + (j + 0.5) * cellSize;
                list.add(new Point(x, y));
            }
        }
        return list;
    }
}
