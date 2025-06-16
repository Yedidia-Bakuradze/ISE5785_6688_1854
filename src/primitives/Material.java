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

    /**
     * Transparency coefficient.
     */
    public Double3 kT = Double3.ZERO;

    /**
     * Reflection coefficient.
     */
    public Double3 kR = Double3.ZERO;

    /**
     * Shininess factor for specular reflection.
     */
    public int nShininess = 0;

    public double ior = 1.0; // Index of refraction for transparency and reflection

    /**
     * Default constructor for Material.
     */
    public Material() {
    }

    /**
     * Set the ambient coefficient.
     *
     * @param kA the ambient coefficient
     * @return the Material object
     */
    public Material setKA(Double3 kA) {
        this.kA = kA;
        return this;
    }

    /**
     * Set the ambient coefficient.
     *
     * @param kA the ambient coefficient
     * @return the Material object
     */
    public Material setKA(double kA) {
        this.kA = new Double3(kA);
        return this;
    }

    /**
     * Set the diffuse coefficient.
     *
     * @param kD the diffuse coefficient
     * @return the Material object
     */
    public Material setKD(Double3 kD) {
        this.kD = kD;
        return this;
    }

    /**
     * Set the diffuse coefficient.
     *
     * @param kD the diffuse coefficient
     * @return the Material object
     */
    public Material setKD(double kD) {
        this.kD = new Double3(kD);
        return this;
    }

    /**
     * Set the specular coefficient.
     *
     * @param kS the specular coefficient
     * @return the Material object
     */
    public Material setKS(Double3 kS) {
        this.kS = kS;
        return this;
    }

    /**
     * Set the specular coefficient.
     *
     * @param kS the specular coefficient
     * @return the Material object
     */
    public Material setKS(double kS) {
        this.kS = new Double3(kS);
        return this;
    }

    /**
     * Set the transparency coefficient.
     *
     * @param kT the transparency coefficient
     * @return the Material object
     */
    public Material setKT(Double3 kT) {
        this.kT = kT;
        return this;
    }

    /**
     * Set the transparency coefficient.
     *
     * @param kT the transparency coefficient
     * @return the Material object
     */
    public Material setKT(double kT) {
        this.kT = new Double3(kT);
        return this;
    }

    /**
     * Set the reflection coefficient.
     *
     * @param kR the reflection coefficient
     * @return the Material object
     */
    public Material setKR(Double3 kR) {
        this.kR = kR;
        return this;
    }

    /**
     * Set the reflection coefficient.
     *
     * @param kR the reflection coefficient
     * @return the Material object
     */
    public Material setKR(double kR) {
        this.kR = new Double3(kR);
        return this;
    }

    /**
     * Set the shininess exponent.
     *
     * @param nShininess the shininess exponent
     * @return the Material object
     */
    public Material setShininess(int nShininess) {
        this.nShininess = nShininess;
        return this;
    }

    /**
     * Set the index of refraction.
     *
     * @param ior the index of refraction
     * @return the Material object
     */
    public Material setIOR(double ior) {
        this.ior = ior;
        return this;
    }
}
