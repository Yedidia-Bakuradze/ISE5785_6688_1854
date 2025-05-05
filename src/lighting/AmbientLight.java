package lighting;

import primitives.Color;

/**
 * Represents the light that shines all the objects in the scene
 */
public class AmbientLight {

    /**
     * The intensity of the ambient light
     */
    final private Color intensity;

    /**
     * The default ambient light, which is black
     */
    public static AmbientLight NONE = new AmbientLight(Color.BLACK);

    /**
     * Initiates the ambient light with provided intensity
     *
     * @param _intensity the intensity of the ambient light
     */
    public AmbientLight(Color _intensity) {
        intensity = _intensity;
    }

    /**
     * Returns the intensity of the ambient light
     *
     * @return the intensity of the ambient light
     */
    public Color getIntensity() {
        return intensity;
    }
}
