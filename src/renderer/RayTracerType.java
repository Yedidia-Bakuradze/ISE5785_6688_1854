package renderer;

/**
 * Ray tracer types
 */
public enum RayTracerType {
    /**
     * Simple (basic) ray tracer
     */
    SIMPLE,
    /**
     * Ray tracer using regular grid
     */
    GRID,

    /**
     * Ray tracer with advanced effects like diffuse glass, soft shadows, and depth of field
     */
    EXTENDED,

    /**
     * Represents an extended ray tracer that uses a regular grid for acceleration.
     */
    GRID_EXTENDED,
}
