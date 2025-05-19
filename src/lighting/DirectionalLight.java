package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

/**
 * Represents a directional light source in the scene.
 * This light source has a fixed direction and uniform intensity.
 */
public class DirectionalLight extends Light implements LightSource {
    /**
     * The direction of the light.
     */
    private final Vector direction;

    /**
     * Constructs a directional light with the specified intensity and direction.
     *
     * @param _intensity The intensity of the light.
     * @param _direction The direction of the light.
     */
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
