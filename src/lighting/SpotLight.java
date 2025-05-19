package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

public class SpotLight extends PointLight {
    private final Vector direction;

    private double narrowBeam = 1d;

    public SpotLight(Color _intensity, Point _position, Vector _direction) {
        super(_intensity, _position);
        this.direction = _direction.normalize();
    }

    @Override
    public SpotLight setKl(double kL) {
        super.setKl(kL);
        return (SpotLight) this;
    }

    @Override
    public SpotLight setKq(double kQ) {
        super.setKq(kQ);
        return (SpotLight) this;
    }

    @Override
    public SpotLight setKc(double kC) {
        super.setKc(kC);
        return (SpotLight) this;
    }

    @Override
    public Color getIntensity(Point p) {
        double additionalFactor = Math.max(0, direction.dotProduct(getL(p)));
        return super.getIntensity(p).scale(additionalFactor);
    }

    public SpotLight setNarrowBeam(double narrowBeam) {
        this.narrowBeam = narrowBeam;
        return (SpotLight) this;
    }
}
