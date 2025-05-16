package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

public class PointLight extends Light implements LightSource {

    protected final Point position;

    private double kC = 1;
    private double kL = 0;
    private double kQ = 0;

    public PointLight(Color _intensity, Point _position) {
        super(_intensity);
        this.position = _position;
    }


    @Override
    public Color getIntensity(Point p) {
        return intensity;
    }

    @Override
    public Vector getL(Point p) {
        return p.subtract(position).normalize();
    }

    public PointLight setKC(double kC) {
        this.kC = kC;
        return this;
    }

    public PointLight setKL(double kL) {
        this.kL = kL;
        return this;
    }

    public PointLight setKQ(double kQ) {
        this.kQ = kQ;
        return this;
    }
}
