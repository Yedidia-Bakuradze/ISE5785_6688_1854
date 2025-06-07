package lighting;

import primitives.*;

/**
 * Interface for light sources in the scene.
 * Provides methods to calculate light intensity and direction at a given point.
 */
public interface LightSource {
    /**
     * Calculates the intensity of the light at a given point.
     *
     * @param p The point in space.
     * @return The color intensity of the light at the given point.
     */
    Color getIntensity(Point p);

    /**
     * Calculates the direction of the light at a given point.
     *
     * @param p The point in space.
     * @return The direction vector of the light at the given point.
     */
    Vector getL(Point p);

    /**
     * Returns the distance from the light source to a given point.
     *
     * @param point The point in space.
     * @return The distance from the light source to the point.
     */
    double getDistance(Point point);
}
