package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

public class SpotLight extends PointLight {
    private final Vector direction;

    public SpotLight(Color _intensity, Point _position, Vector _direction) {
        super(_intensity, _position);
        this.direction = _direction.normalize();
    }

    @Override
    public PointLight setKL(double kL) {
        super.setKL(kL);
        return (PointLight) this;
    }

    @Override
    public PointLight setKQ(double kQ) {
        super.setKQ(kQ);
        return (PointLight) this;
    }

    @Override
    public PointLight setKC(double kC) {
        super.setKC(kC);
        return (PointLight) this;
    }
}
