package lighting;

import primitives.Color;

/**
 * Abstract class representing a light source.
 * This class provides the base implementation for light intensity.
 */
abstract class Light {

    /**
     * The intensity of the ambient light.
     */
    final protected Color intensity;

    /**
     * Default constructor for Light.
     *
     * @param intensity the color intensity of the light
     */
    protected Light(Color intensity) {
        this.intensity = intensity;
    }

    /**
     * Returns the intensity of the ambient light.
     *
     * @return the intensity of the ambient light
     */
    public Color getIntensity() {
        return intensity;
    }

}
