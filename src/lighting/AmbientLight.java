package lighting;

import primitives.Color;

public class AmbientLight {

    final private Color intensity;
    public static AmbientLight NONE = new AmbientLight(Color.BLACK);
    public AmbientLight(Color _intensity) {
        intensity = _intensity;
    }

    public Color getIntensity() {
        return intensity;
    }
}
