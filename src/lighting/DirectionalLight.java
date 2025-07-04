package lighting;

import primitives.*;

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
     * @param intensity The intensity of the light.
     * @param direction The direction of the light.
     */
    public DirectionalLight(Color intensity, Vector direction) {
        super(intensity);
        this.direction = direction.normalize();
    }

    @Override
    public Color getIntensity(Point p) {
        return intensity;
    }

    @Override
    public Vector getL(Point p) {
        return direction;
    }

    @Override
    public double getDistance(Point point) {
        return Double.POSITIVE_INFINITY;
    }

}
