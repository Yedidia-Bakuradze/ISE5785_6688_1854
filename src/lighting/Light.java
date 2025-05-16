package lighting;

import primitives.Color;

abstract class Light {

    /**
     * The intensity of the ambient light
     */
    final protected Color intensity;

    protected Light(Color _intensity) {
        this.intensity = _intensity;
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
