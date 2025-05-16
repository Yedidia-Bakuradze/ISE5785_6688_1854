package lighting;

import primitives.Color;

/**
 * Represents the light that shines all the objects in the scene
 */
public class AmbientLight extends Light {


    /**
     * The default ambient light, which is black
     */
    public static AmbientLight NONE = new AmbientLight(Color.BLACK);

    /**
     * Initiates the ambient light with provided intensity
     *
     * @param _intensity the intensity of the ambient light (Ia)
     */
    public AmbientLight(Color _intensity) {
        super(_intensity);
    }

}
