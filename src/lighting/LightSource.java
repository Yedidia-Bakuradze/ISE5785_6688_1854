package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

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
}
