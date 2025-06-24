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

    GRID_EXTENDED,
}
