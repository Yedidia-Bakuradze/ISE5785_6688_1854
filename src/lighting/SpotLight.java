package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

/**
 * Represents a spotlight in the scene.
 * This class extends {@link PointLight} and adds directionality to the light.
 */
public class SpotLight extends PointLight {
    /**
     * The direction the spotlight is pointing.
     */
    private final Vector direction;

    /**
     * The narrowness of the beam.
     */
    private double narrowBeam = 1d;

    /**
     * Constructs a spotlight with the given intensity, position, and direction.
     *
     * @param _intensity the color intensity of the light
     * @param _position  the position of the light in the scene
     * @param _direction the direction the light is pointing
     */
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

    /**
     * Calculates the intensity of the light at a given point.
     *
     * @param p the point in space
     * @return the color intensity of the light at the given point
     */
    @Override
    public Color getIntensity(Point p) {
        double additionalFactor = Math.max(0, direction.dotProduct(getL(p)));
        additionalFactor = Math.pow(additionalFactor, narrowBeam);
        return super.getIntensity(p).scale(additionalFactor);
    }

    /**
     * Sets the narrowness of the beam.
     *
     * @param narrowBeam the narrowness factor, must be greater than zero
     * @return the current SpotLight instance
     */
    public SpotLight setNarrowBeam(double narrowBeam) {
        if (narrowBeam < 0d) throw new IllegalArgumentException("narrowBeam must be greater than zero");
        this.narrowBeam = narrowBeam;
        return (SpotLight) this;
    }
}
