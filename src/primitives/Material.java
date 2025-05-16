package primitives;

public class Material {

    public Double3 kA = Double3.ONE;

    public Double3 kD = Double3.ZERO;
    public Double3 kS = Double3.ZERO;
    public int nShininess = 0;

    /**
     * Ctor for JavaDoc
     */
    public Material() {
    }

    public Material setKA(Double3 _kA) {
        this.kA = _kA;
        return this;
    }

    public Material setKA(double _kA) {
        this.kA = new Double3(_kA);
        return this;
    }

    public Material setKD(Double3 _kD) {
        this.kD = _kD;
        return this;
    }

    public Material setKD(double _kD) {
        this.kD = new Double3(_kD);
        return this;
    }

    public Material setKS(Double3 _kS) {
        this.kS = _kS;
        return this;
    }

    public Material setKS(double _kS) {
        this.kS = new Double3(_kS);
        return this;
    }

    public Material setShininess(int _nShininess) {
        this.nShininess = _nShininess;
        return this;
    }
}
