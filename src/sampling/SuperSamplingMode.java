package sampling;

public enum SuperSamplingMode {
    EASY(2),
    DEMO(9),
    PRODUCTION(33);

    public final int gridSize;
    public final int gridSizeSquared;

    SuperSamplingMode(int gridSize) {
        this.gridSize = gridSize;
        this.gridSizeSquared = gridSize * gridSize;
    }
}
