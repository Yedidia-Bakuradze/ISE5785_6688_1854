package sampling;

public enum SuperSamplingMode {
    EASY(2),
    DEMO(3),
    PRODUCTION(4);

    public final int gridSize;
    public final int gridSizeSquared;

    SuperSamplingMode(int gridSize) {
        this.gridSize = gridSize;
        this.gridSizeSquared = gridSize * gridSize;
    }
}
