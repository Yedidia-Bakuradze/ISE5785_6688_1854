package lighting;

import primitives.*;

import static java.lang.Math.pow;
import static primitives.Util.alignZero;

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
     * @param intensity the color intensity of the light
     * @param position  the position of the light in the scene
     * @param direction the direction the light is pointing
     */
    public SpotLight(Color intensity, Point position, Vector direction) {
        super(intensity, position);
        this.direction = direction.normalize();
    }

    /**
     * Sets the linear attenuation coefficient.
     *
     * @param kL The linear attenuation coefficient.
     * @return The current instance of SpotLight.
     */
    @Override
    public SpotLight setKl(double kL) {
        return (SpotLight) super.setKl(kL);
    }

    /**
     * Sets the quadratic attenuation coefficient.
     *
     * @param kQ The quadratic attenuation coefficient.
     * @return The current instance of SpotLight.
     */
    @Override
    public SpotLight setKq(double kQ) {
        return (SpotLight) super.setKq(kQ);
    }

    /**
     * Sets the constant attenuation coefficient.
     *
     * @param kC The constant attenuation coefficient.
     * @return The current instance of SpotLight.
     */
    @Override
    public SpotLight setKc(double kC) {
        return (SpotLight) super.setKc(kC);
    }

    /**
     * Calculates the intensity of the light at a given point.
     *
     * @param p the point in space
     * @return the color intensity of the light at the given point
     */
    @Override
    public Color getIntensity(Point p) {
        double additionalFactor = alignZero(direction.dotProduct(getL(p)));
        return additionalFactor <= 0 ? Color.BLACK : super.getIntensity(p).scale(pow(additionalFactor, narrowBeam));
    }

    /**
     * Sets the narrowness of the beam.
     *
     * @param narrowBeam the narrowness factor, must be greater than zero
     * @return the current SpotLight instance
     */
    public SpotLight setNarrowBeam(double narrowBeam) {
        if (narrowBeam < 1d) throw new IllegalArgumentException("narrowBeam must be greater than zero");
        this.narrowBeam = narrowBeam;
        return this;
    }
}
