package lighting;

import primitives.Color;

public class AmbientLight {

    final private Color intensity;
    public static Color BLACK = Color.BLACK;
    AmbientLight(Color _intensity) {
        intensity = _intensity;
    }

    public Color getIntensity() {
        return intensity;
    }
}
