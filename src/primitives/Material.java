package primitives;

public class Material {

    public Double3 kA = Double3.ONE;

    /**
     * Ctor for JavaDoc
     */
    public Material() {
    }

    public Material setKa(Double3 _kA) {
        this.kA = _kA;
        return this;
    }

    public Material setKa(double _kA) {
        this.kA = new Double3(_kA);
        return this;
    }


}
