package acceleration;

import geometries.Intersectable;

import java.util.*;

/**
 * Represents a single voxel (grid cell) in the RegularGrid acceleration structure.
 * Holds references to all geometries that overlap this cell.
 */
public class Voxel {
    private final List<Intersectable> geometries = new ArrayList<>();

    /**
     * Adds a geometry to this voxel.
     * Duplicate additions of the same object are allowed but may be
     * filtered during traversal if deduplication is enabled.
     *
     * @param geom the geometry to add
     */
    public Voxel addGeometry(Intersectable geom) {
        if (geom == null) throw new IllegalArgumentException("Cannot add null geometry to voxel");
        geometries.add(geom);
        return this;
    }

    /**
     * Returns an unmodifiable list of geometries in this voxel.
     *
     * @return list of geometries (empty if none)
     */
    public List<Intersectable> getGeometries() {
        return Collections.unmodifiableList(geometries);
    }

    /**
     * Indicates whether this voxel is empty (contains no geometries).
     *
     * @return true if no geometries have been added
     */
    public boolean isEmpty() {
        return geometries.isEmpty();
    }

    /**
     * Gets the number of geometries in this voxel
     *
     * @return The geometry count
     */
    public int size() {
        return geometries.size();
    }

    @Override
    public String toString() {
        return String.format("Voxel[%d geometries]", geometries.size());
    }
}
