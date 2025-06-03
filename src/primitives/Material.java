package primitives;

/**
 * Represents the material properties of a geometry.
 * This class provides methods to set ambient, diffuse, and specular coefficients.
 */
public class Material {

    /**
     * Ambient reflection coefficient.
     */
    public Double3 kA = Double3.ONE;

    /**
     * Diffuse reflection coefficient.
     */
    public Double3 kD = Double3.ZERO;

    /**
     * Specular reflection coefficient.
     */
    public Double3 kS = Double3.ZERO;

    public Double3 kT = Double3.ZERO;
    public Double3 kR = Double3.ZERO;

    /**
     * Shininess factor for specular reflection.
     */
    public int nShininess = 0;

    /**
     * Ctor for JavaDoc
     */
    public Material() {
    }

    /**
     * Set the ambient coefficient.
     *
     * @param _kA the ambient coefficient
     * @return the Material object
     */
    public Material setKA(Double3 _kA) {
        this.kA = _kA;
        return this;
    }

    /**
     * Set the ambient coefficient.
     *
     * @param _kA the ambient coefficient
     * @return the Material object
     */
    public Material setKA(double _kA) {
        this.kA = new Double3(_kA);
        return this;
    }

    /**
     * Set the diffuse coefficient.
     *
     * @param _kD the diffuse coefficient
     * @return the Material object
     */
    public Material setKD(Double3 _kD) {
        this.kD = _kD;
        return this;
    }

    /**
     * Set the diffuse coefficient.
     *
     * @param _kD the diffuse coefficient
     * @return the Material object
     */
    public Material setKD(double _kD) {
        this.kD = new Double3(_kD);
        return this;
    }

    /**
     * Set the specular coefficient.
     *
     * @param _kS the specular coefficient
     * @return the Material object
     */
    public Material setKS(Double3 _kS) {
        this.kS = _kS;
        return this;
    }

    /**
     * Set the specular coefficient.
     *
     * @param _kS the specular coefficient
     * @return the Material object
     */
    public Material setKS(double _kS) {
        this.kS = new Double3(_kS);
        return this;
    }

    /**
     * Set the transparency coefficient.
     *
     * @param _kT the transparency coefficient
     * @return the Material object
     */
    public Material setKT(Double3 _kT) {
        this.kT = _kT;
        return this;
    }

    /**
     * Set the reflection coefficient.
     *
     * @param kR the transparency coefficient
     * @return the Material object
     */
    public Material setKR(Double3 kR) {
        this.kR = kR;
        return this;
    }

    /**
     * Set the shininess exponent.
     *
     * @param _nShininess the shininess exponent
     * @return the Material object
     */
    public Material setShininess(int _nShininess) {
        this.nShininess = _nShininess;
        return this;
    }
}
