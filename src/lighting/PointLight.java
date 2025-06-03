package lighting;

import primitives.*;

/**
 * Represents a point light source in the scene.
 * This class provides methods to calculate light intensity and direction.
 */
public class PointLight extends Light implements LightSource {

    /**
     * The position of the point light in the scene.
     */
    protected final Point position;

    /**
     * The attenuation coefficients for the light.
     * kC - constant attenuation
     */
    private double kC = 1;

    /**
     * kL - linear attenuation
     */
    private double kL = 0;

    /**
     * kQ - quadratic attenuation
     */
    private double kQ = 0;

    /**
     * Constructs a point light with the given intensity and position.
     *
     * @param _intensity the color intensity of the light
     * @param _position  the position of the light in the scene
     */
    public PointLight(Color _intensity, Point _position) {
        super(_intensity);
        this.position = _position;
    }

    /**
     * Returns the intensity of the light at a given point in space.
     *
     * @param p The point in space.
     * @return The color intensity of the light at the given point.
     */
    @Override
    public Color getIntensity(Point p) {
        double distance = p.distance(position);
        double attenuationFactor = 1d / (kC + kL * distance + kQ * distance * distance);
        return intensity.scale(attenuationFactor);
    }

    /**
     * Returns the direction of the light from the given point.
     *
     * @param p The point in space.
     * @return The direction vector from the light source to the point.
     */
    @Override
    public Vector getL(Point p) {
        return p.subtract(position).normalize();
    }

    @Override
    public double getDistance(Point point) {
        return point.distance(position);
    }

    /**
     * Sets the constant attenuation coefficient.
     *
     * @param kC The constant attenuation coefficient.
     * @return The current instance of PointLight.
     */
    public PointLight setKc(double kC) {
        this.kC = kC;
        return this;
    }

    /**
     * Sets the linear attenuation coefficient.
     *
     * @param kL The linear attenuation coefficient.
     * @return The current instance of PointLight.
     */
    public PointLight setKl(double kL) {
        this.kL = kL;
        return this;
    }

    /**
     * Sets the quadratic attenuation coefficient.
     *
     * @param kQ The quadratic attenuation coefficient.
     * @return The current instance of PointLight.
     */
    public PointLight setKq(double kQ) {
        this.kQ = kQ;
        return this;
    }
}
