package scene;

import geometries.Geometries;
import lighting.AmbientLight;
import primitives.Color;

public class Scene {
    String name;
    Color backgroundColor = Color.BLACK;
    AmbientLight ambientLight = AmbientLight.NONE ;
    Geometries geometries = new Geometries();

    Scene(String sceneName){
        name = sceneName;
    }

    Scene setBackgroundColor(Color _color){
        backgroundColor = _color;
        return this;
    }

    Scene setAmbientLight(AmbientLight _light){
        ambientLight = _light;
        return this;
    }

    Scene setGeometries(Geometries _geometries){
        geometries = _geometries;
        return this;
    }
}
