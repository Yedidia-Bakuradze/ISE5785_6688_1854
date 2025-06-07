package lighting;

import primitives.Color;

/**
 * Represents the ambient light in the scene.
 * This class provides a default ambient light and methods to set its intensity.
 */
public class AmbientLight extends Light {

    /**
     * The default ambient light, which is black.
     */
    public static AmbientLight NONE = new AmbientLight(Color.BLACK);

    /**
     * Initiates the ambient light with provided intensity.
     *
     * @param intensity the intensity of the ambient light (Ia)
     */
    public AmbientLight(Color intensity) {
        super(intensity);
    }

}
