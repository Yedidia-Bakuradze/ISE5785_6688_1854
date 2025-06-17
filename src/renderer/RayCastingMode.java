package renderer;

/**
 * Enumeration representing different modes for casting rays in the renderer.
 * Determines whether rays are cast as single rays or as beams of multiple rays.
 */
public enum RayCastingMode {
    /**
     * Multiple rays are cast in a beam pattern for effects like soft shadows or depth of field
     */
    BEAM,

    /**
     * A single ray is cast for each pixel or calculation
     */
    SINGLE,
}
