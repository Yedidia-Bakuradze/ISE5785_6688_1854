package scene;

import geometries.Geometries;
import lighting.AmbientLight;
import primitives.Color;

public class Scene {
    public String name;
    public Color backgroundColor = Color.BLACK;
    public AmbientLight ambientLight = AmbientLight.NONE ;
    public Geometries geometries = new Geometries();

    public Scene(String sceneName){
        name = sceneName;
    }

    public Scene setBackground(Color _color){
        backgroundColor = _color;
        return this;
    }

    public Scene setAmbientLight(AmbientLight _light){
        ambientLight = _light;
        return this;
    }

    public Scene setGeometries(Geometries _geometries){
        geometries = _geometries;
        return this;
    }
}
