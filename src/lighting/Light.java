package lighting;

import primitives.Color;

abstract class Light {
    final protected Color intensity;

    protected Light(Color _intensity) {
        this.intensity = _intensity;
    }

    public Color getIntensity() {
        return intensity;
    }


}
