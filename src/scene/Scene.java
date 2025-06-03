package scene;

import geometries.Geometries;
import lighting.AmbientLight;
import lighting.LightSource;
import primitives.Color;

import java.util.LinkedList;
import java.util.List;

/**
 * Represents a scene containing geometries, lights, and other properties.
 * This class provides methods to configure the scene's properties.
 */
public class Scene {

    /**
     * The name of the scene.
     */
    public final String name;

    /**
     * The background color of the scene.
     */
    public Color backgroundColor = Color.BLACK;

    /**
     * The ambient light of the scene.
     */
    public AmbientLight ambientLight = AmbientLight.NONE;

    /**
     * The geometries in the scene.
     */
    public Geometries geometries = new Geometries();

    /**
     * The list of light sources in the scene.
     */
    public List<LightSource> lights = new LinkedList<>();

    /**
     * Constructs a Scene with the specified name.
     *
     * @param sceneName The name of the scene.
     */
    public Scene(String sceneName) {
        name = sceneName;
    }

    /**
     * Sets the background color of the scene.
     *
     * @param _color The background color.
     * @return The current Scene instance for method chaining.
     */
    public Scene setBackground(Color _color) {
        backgroundColor = _color;
        return this;
    }

    /**
     * Sets the ambient light of the scene.
     *
     * @param _light The ambient light.
     * @return The current Scene instance for method chaining.
     */
    public Scene setAmbientLight(AmbientLight _light) {
        ambientLight = _light;
        return this;
    }

    /**
     * Sets the geometries in the scene.
     *
     * @param _geometries The geometries.
     * @return The current Scene instance for method chaining.
     */
    public Scene setGeometries(Geometries _geometries) {
        //TODO Fix parameter's naming
        geometries = _geometries;
        return this;
    }

    /**
     * Sets the light sources in the scene.
     *
     * @param lights The list of light sources to set.
     * @return The current Scene instance.
     */
    public Scene setLights(LinkedList<LightSource> lights) {
        this.lights = lights;
        return this;
    }
}
