package sampling;

import geometries.Intersectable;
import primitives.Ray;

import java.util.List;

/**
 * A generic sampling strategy that produces one or more rays
 * based on an initial primary ray and optional intersection info.
 * <p>
 * Pre-hit samplers ignore the intersection (scene geometry),
 * post-hit samplers use the hit record to generate secondary rays.
 */
public interface SamplingStrategy {
    /**
     * Generate rays for tracing.
     *
     * @param primaryRay the original ray, starting at camera or previous bounce
     * @param hit        optional intersection info; null for pre-hit samplers
     * @return list of rays to trace next; must return primaryRay at minimum
     */
    List<Ray> generateRays(Ray primaryRay, Intersectable.Intersection hit);
}
