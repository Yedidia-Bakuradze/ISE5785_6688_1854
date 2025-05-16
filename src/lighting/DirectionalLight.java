package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

public class DirectionalLight extends Light implements LightSource {

    private final Vector direction;

    public DirectionalLight(Color _intensity, Vector _direction) {
        super(_intensity);
        this.direction = _direction.normalize();
    }

    @Override
    public Color getIntensity(Point p) {
        return intensity;
    }

    @Override
    public Vector getL(Point p) {
        return direction;
    }

}
