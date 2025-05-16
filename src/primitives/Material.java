package primitives;

public class Material {

    public Double3 ka = Double3.ONE;

    /**
     * Ctor for JavaDoc
     */
    public Material() {
    }

    public Material setKa(Double3 _ka) {
        this.ka = _ka;
        return this;
    }

    public Material setKd(double _kd) {
        this.ka = new Double3(_kd);
        return this;
    }


}
