package acceleration;

import geometries.*;
import lighting.*;
import org.junit.jupiter.api.Test;
import primitives.*;
import renderer.*;
import sampling.*;
import scene.Scene;

/**
 * Test class for creating a chess board made of triangles with a mirror reflection.
 * This test demonstrates the Regular Grid acceleration feature with multithreading
 * as required for MP2.
 */
public class ChessBoardTest {

    /**
     * Default constructor for ChessBoardTest class.
     */
    ChessBoardTest() {
    }

    /**
     * The scene object containing all geometries, lighting and settings for rendering.
     */
    public Scene scene = new Scene("Chess Board Test Scene");

    /**
     * Size of each chess square in scene units.
     */
    private static final double SQUARE_SIZE = 100.0;

    /**
     * Total size of the chess board (8x8 squares).
     */
    private static final double BOARD_SIZE = 8 * SQUARE_SIZE;

    /**
     * Height of the chess board squares.
     */
    private static final double BOARD_HEIGHT = 5.0;

    private static final Vector V_TO = new Vector(0, 1, -0.5);
    private static final Vector V_UP = new Vector(0, 0.5, 1);
    private static final Point CAMERA_LOCATION = new Point(0, -600, 300);

    private static final int RESOLUTION_WIDTH = 1200;
    private static final int RESOLUTION_HEIGHT = 1000;

    private static final double IMAGE_WIDTH = RESOLUTION_WIDTH;
    private static final double IMAGE_HEIGHT = RESOLUTION_HEIGHT;

    private static final double DISTANCE = 400;

    /**
     * Camera builder for setting up the viewpoint and rendering parameters.
     */
    public Camera.Builder cameraBuilder = Camera.getBuilder()
            .setLocation(CAMERA_LOCATION)
            .setDirection(V_TO, V_UP)
            .setVpDistance(DISTANCE)
            .setVpSize(IMAGE_WIDTH, IMAGE_HEIGHT)
            .setResolution(RESOLUTION_WIDTH, RESOLUTION_HEIGHT)
            .setEffect(EffectType.DIFFUSIVE_GLASS, new SamplingConfiguration(SamplingMode.EASY, TargetAreaType.CIRCLE, SamplingPattern.JITTERED, 0.5));

    /**
     * Material for white chess pieces (frosted glass).
     */
    Material whitePieceMaterial = new Material().setKD(0.2).setKS(0.3).setShininess(30).setKR(0.1);

    /**
     * Material for black chess pieces.
     */
    Material blackPieceMaterial = new Material().setKD(0.8).setKS(0.2).setShininess(40).setKR(0.05);

    /**
     * White chess piece color (frosted glass) - dimmer than board squares.
     */
    Color whitePieceColor = new Color(176, 196, 222); // Dimmed from (250, 250, 255)

    /**
     * Black chess piece color - dimmer than board squares.
     */
    Color blackPieceColor = new Color(30, 15, 20); // Dimmed from (30, 30, 30)

    /**
     * Height offset for pieces above the board.
     */
    private static final double PIECE_HEIGHT_OFFSET = 4.0;

    /**
     * Material for white squares.
     */
    Material whiteMaterial = new Material().setKD(0.6).setKS(0.4).setShininess(60).setKR(0.1);

    /**
     * Material for black squares.
     */
    Material blackMaterial = new Material().setKD(0.7).setKS(0.3).setShininess(40).setKR(0.05);

    /**
     * Material for the base/floor.
     */
    Material floorMaterial = new Material().setKD(0.8).setKS(0.2).setShininess(30).setKR(0.9);

    /**
     * Material for trophy base (gold).
     */
    Material trophyBaseMaterial = new Material().setKD(0.3).setKS(0.7).setShininess(100).setKR(0.3);

    /**
     * Material for trophy ball (sparkling gold).
     */
    Material trophyBallMaterial = new Material().setKD(0.2).setKS(0.2).setKR(0.4).setKT(0.1);

    /**
     * Gold color for trophy base.
     */
    Color trophyGoldColor = new Color(255, 215, 0); // Pure gold

    /**
     * Dark gold color for trophy accents.
     */
    Color trophyDarkColor = new Color(184, 134, 11); // Dark gold, not too dark

    /**
     * Sparkling gold color for trophy ball.
     */
    Color trophySparkleColor = new Color(255, 223, 0); // Bright sparkling gold

    /**
     * White square color.
     */
    Color whiteColor = new Color(240, 240, 240);

    /**
     * Black square color.
     */
    Color blackColor = new Color(60, 60, 60);

    /**
     * Mirror color (slightly tinted).
     */
    Color mirrorColor = new Color(220, 230, 240);

    /**
     * Floor color.
     */
    Color floorColor = new Color(101, 67, 33);// Dark walnut

    // Background: Soft neutral
    Color backgroundColor = Color.BLACK;

    /**
     * Material for trophy display case (diffusive glass).
     */
    Material trophyCaseMaterial = new Material().setKD(0.1).setKS(0.3).setShininess(60).setKT(0.7).setRoughness(0.3);

    /**
     * Color for trophy display case (clear glass with slight tint).
     */
    Color trophyCaseColor = new Color(12, 120, 56).scale(0.3);

    /**
     * Material for chess clock body (dark plastic/metal).
     */
    Material clockBoxMaterial = new Material().setKD(0.7).setKS(0.3).setShininess(50).setKR(0.1);

    /**
     * Material for clock buttons (smooth plastic).
     */
    Material clockButtonMaterial = new Material().setKD(0.5).setKS(0.5).setShininess(80).setKR(0.2);

    /**
     * Material for clock faces (white display).
     */
    Material clockFaceMaterial = new Material().setKD(0.8).setKS(0.2).setShininess(30).setKR(0.05);

    /**
     * Material for clock hands (black indicators).
     */
    Material clockHandMaterial = new Material().setKD(0.9).setKS(0.1).setShininess(20).setKR(0.0);

    /**
     * Color for chess clock body.
     */
    Color clockBoxColor = new Color(40, 40, 45); // Dark gray

    /**
     * Color for player 1 button (white side).
     */
    Color button1Color = new Color(220, 220, 230); // Light gray/white

    /**
     * Color for player 2 button (black side).
     */
    Color button2Color = new Color(50, 50, 60); // Dark gray/black

    /**
     * Color for clock faces.
     */
    Color clockFaceColor = new Color(250, 250, 250); // White

    /**
     * Color for clock hands.
     */
    Color clockHandColor = new Color(10, 10, 10); // Black

    /**
     * Test method for Chess Board with Regular Grid - Feature Deactivated; MT Deactivated
     */
    @Test
    void testChessBoard_NoGrid_NoMT() {
        createChessBoardScene();

        cameraBuilder
                .setRayTracer(scene, RayTracerType.EXTENDED)
                .setMultithreading(0) // No multithreading
                .setDebugPrint(0.1)
                .build()
                .renderImage()
                .writeToImage("ChessBoard With Without Grid & MT");
    }

    /**
     * Test method for Chess Board with Regular Grid - Feature Deactivated; MT Activated
     */
    @Test
    void testChessBoard_NoGrid_WithMT() {
        createChessBoardScene();

        cameraBuilder
                .setRayTracer(scene, RayTracerType.EXTENDED)
                .setMultithreading(-2) // Multithreading activated
                .setDebugPrint(0.1)
                .build()
                .renderImage()
                .writeToImage("ChessBoard Without Grid With MT");
    }

    /**
     * Test method for Chess Board with Regular Grid - Feature Activated; MT Deactivated
     */
    @Test
    void testChessBoard_WithGrid_NoMT() {
        createChessBoardScene();

        cameraBuilder
                .setRegularGrid(new RegularGrid(scene))
                .setRayTracer(scene, RayTracerType.GRID_EXTENDED)
                .setMultithreading(0) // No multithreading
                .setDebugPrint(0.1)
                .build()
                .renderImage()
                .writeToImage("ChessBoard With Grid Without MT");
    }

    /**
     * Test method for Chess Board with Regular Grid - Feature Activated; MT Activated
     */
    @Test
    void testChessBoard_WithGrid_WithMT() {
        createChessBoardScene();

        cameraBuilder
                .setRegularGrid(new RegularGrid(scene))
                .setRayTracer(scene, RayTracerType.GRID_EXTENDED)
                .setMultithreading(-2)
                .setDebugPrint(0.1)
                .build()
                .renderImage()
                .writeToImage("ChessBoard With Grid With MT");
    }

    /**
     * Creates the complete chess board scene with mirror and lighting.
     */
    private void createChessBoardScene() {
        // Clear any existing geometries
        scene.geometries = new Geometries();
        // Create chess board
        createChessBoard();

        // Place chess pieces on the board
        placeChessPieces();

        // Place trophy next to the board
        placeTrophy();

        // Create floor/base
        createFloor();

        placeChessClock();

        // Set up lighting
        setupLighting();
    }

    /**
     * Creates a pawn chess piece using triangles.
     *
     * @param centerX X coordinate of the piece center
     * @param centerY Y coordinate of the piece center
     * @param isWhite true for white piece, false for black piece
     * @return array of triangles representing the pawn
     */
    private Triangle[] createPawn(double centerX, double centerY, boolean isWhite) {
        double baseRadius = SQUARE_SIZE * 0.2;
        double topRadius = SQUARE_SIZE * 0.15;
        double height = SQUARE_SIZE * 0.8; // Doubled from 0.4
        double sphereRadius = SQUARE_SIZE * 0.12;

        Point base = new Point(centerX, centerY, PIECE_HEIGHT_OFFSET);
        Point top = new Point(centerX, centerY, PIECE_HEIGHT_OFFSET + height);
        Point sphereTop = new Point(centerX, centerY, PIECE_HEIGHT_OFFSET + height + sphereRadius);

        // Create base circle points (8-sided)
        Point[] basePoints = new Point[8];
        Point[] topPoints = new Point[8];
        Point[] spherePoints = new Point[8];

        for (int i = 0; i < 8; i++) {
            double angle = i * 2 * Math.PI / 8;
            basePoints[i] = new Point(
                    centerX + baseRadius * Math.cos(angle),
                    centerY + baseRadius * Math.sin(angle),
                    PIECE_HEIGHT_OFFSET
            );
            topPoints[i] = new Point(
                    centerX + topRadius * Math.cos(angle),
                    centerY + topRadius * Math.sin(angle),
                    PIECE_HEIGHT_OFFSET + height
            );
            spherePoints[i] = new Point(
                    centerX + sphereRadius * Math.cos(angle),
                    centerY + sphereRadius * Math.sin(angle),
                    PIECE_HEIGHT_OFFSET + height + sphereRadius * 0.7
            );
        }

        Triangle[] triangles = new Triangle[32]; // 8 base + 8 sides + 8 top + 8 sphere
        int index = 0;

        Material material = isWhite ? whitePieceMaterial : blackPieceMaterial;
        Color color = isWhite ? whitePieceColor : blackPieceColor;

        // Base triangles
        for (int i = 0; i < 8; i++) {
            triangles[index++] = (Triangle) new Triangle(base, basePoints[i], basePoints[(i + 1) % 8])
                    .setEmission(color).setMaterial(material);
        }

        // Side triangles
        for (int i = 0; i < 8; i++) {
            triangles[index++] = (Triangle) new Triangle(basePoints[i], topPoints[i], basePoints[(i + 1) % 8])
                    .setEmission(color).setMaterial(material);
            triangles[index++] = (Triangle) new Triangle(topPoints[i], topPoints[(i + 1) % 8], basePoints[(i + 1) % 8])
                    .setEmission(color).setMaterial(material);
        }

        // Sphere approximation (top of pawn)
        for (int i = 0; i < 8; i++) {
            triangles[index++] = (Triangle) new Triangle(sphereTop, spherePoints[i], spherePoints[(i + 1) % 8])
                    .setEmission(color).setMaterial(material);
        }

        return java.util.Arrays.copyOf(triangles, index);
    }

    /**
     * Creates a queen chess piece using triangles.
     *
     * @param centerX X coordinate of the piece center
     * @param centerY Y coordinate of the piece center
     * @param isWhite true for white piece, false for black piece
     * @return array of triangles representing the queen
     */
    private Triangle[] createQueen(double centerX, double centerY, boolean isWhite) {
        double baseRadius = SQUARE_SIZE * 0.25;
        double middleRadius = SQUARE_SIZE * 0.18;
        double height = SQUARE_SIZE * 1.2; // Doubled from 0.6
        double crownHeight = SQUARE_SIZE * 0.4; // Doubled from 0.2

        Point base = new Point(centerX, centerY, PIECE_HEIGHT_OFFSET);
        Point middle = new Point(centerX, centerY, PIECE_HEIGHT_OFFSET + height * 0.7);
        Point crownBase = new Point(centerX, centerY, PIECE_HEIGHT_OFFSET + height);

        // Create base circle points (12-sided for more detail)
        Point[] basePoints = new Point[12];
        Point[] middlePoints = new Point[12];
        Point[] crownPoints = new Point[12];

        for (int i = 0; i < 12; i++) {
            double angle = i * 2 * Math.PI / 12;
            basePoints[i] = new Point(
                    centerX + baseRadius * Math.cos(angle),
                    centerY + baseRadius * Math.sin(angle),
                    PIECE_HEIGHT_OFFSET
            );
            middlePoints[i] = new Point(
                    centerX + middleRadius * Math.cos(angle),
                    centerY + middleRadius * Math.sin(angle),
                    PIECE_HEIGHT_OFFSET + height * 0.7
            );
            // Crown spikes (alternating high and low)
            double crownR = middleRadius * 0.8;
            double spikeHeight = (i % 2 == 0) ? crownHeight : crownHeight * 0.6;
            crownPoints[i] = new Point(
                    centerX + crownR * Math.cos(angle),
                    centerY + crownR * Math.sin(angle),
                    PIECE_HEIGHT_OFFSET + height + spikeHeight
            );
        }

        Triangle[] triangles = new Triangle[60]; // Base + sides + crown
        int index = 0;

        Material material = isWhite ? whitePieceMaterial : blackPieceMaterial;
        Color color = isWhite ? whitePieceColor : blackPieceColor;

        // Base triangles
        for (int i = 0; i < 12; i++) {
            triangles[index++] = (Triangle) new Triangle(base, basePoints[i], basePoints[(i + 1) % 12])
                    .setEmission(color).setMaterial(material);
        }

        // Lower body triangles
        for (int i = 0; i < 12; i++) {
            triangles[index++] = (Triangle) new Triangle(basePoints[i], middlePoints[i], basePoints[(i + 1) % 12])
                    .setEmission(color).setMaterial(material);
            triangles[index++] = (Triangle) new Triangle(middlePoints[i], middlePoints[(i + 1) % 12], basePoints[(i + 1) % 12])
                    .setEmission(color).setMaterial(material);
        }

        // Crown triangles
        for (int i = 0; i < 12; i++) {
            triangles[index++] = (Triangle) new Triangle(middlePoints[i], crownPoints[i], middlePoints[(i + 1) % 12])
                    .setEmission(color).setMaterial(material);
            triangles[index++] = (Triangle) new Triangle(crownPoints[i], crownPoints[(i + 1) % 12], middlePoints[(i + 1) % 12])
                    .setEmission(color).setMaterial(material);
        }

        return java.util.Arrays.copyOf(triangles, index);
    }

    /**
     * Creates a king chess piece using triangles.
     *
     * @param centerX X coordinate of the piece center
     * @param centerY Y coordinate of the piece center
     * @param isWhite true for white piece, false for black piece
     * @return array of triangles representing the king
     */
    private Triangle[] createKing(double centerX, double centerY, boolean isWhite) {
        double baseRadius = SQUARE_SIZE * 0.28;
        double middleRadius = SQUARE_SIZE * 0.2;
        double height = SQUARE_SIZE * 1.4; // Doubled from 0.7
        double crossHeight = SQUARE_SIZE * 0.3; // Doubled from 0.15
        double crossWidth = SQUARE_SIZE * 0.08;

        Point base = new Point(centerX, centerY, PIECE_HEIGHT_OFFSET);
        Point middle = new Point(centerX, centerY, PIECE_HEIGHT_OFFSET + height * 0.8);
        Point top = new Point(centerX, centerY, PIECE_HEIGHT_OFFSET + height);

        // Create base circle points (10-sided)
        Point[] basePoints = new Point[10];
        Point[] middlePoints = new Point[10];
        Point[] topPoints = new Point[8];

        for (int i = 0; i < 10; i++) {
            double angle = i * 2 * Math.PI / 10;
            basePoints[i] = new Point(
                    centerX + baseRadius * Math.cos(angle),
                    centerY + baseRadius * Math.sin(angle),
                    PIECE_HEIGHT_OFFSET
            );
            middlePoints[i] = new Point(
                    centerX + middleRadius * Math.cos(angle),
                    centerY + middleRadius * Math.sin(angle),
                    PIECE_HEIGHT_OFFSET + height * 0.8
            );
        }

        for (int i = 0; i < 8; i++) {
            double angle = i * 2 * Math.PI / 8;
            topPoints[i] = new Point(
                    centerX + middleRadius * 0.6 * Math.cos(angle),
                    centerY + middleRadius * 0.6 * Math.sin(angle),
                    PIECE_HEIGHT_OFFSET + height
            );
        }

        // Cross points
        Point crossTop = new Point(centerX, centerY, PIECE_HEIGHT_OFFSET + height + crossHeight);
        Point crossLeft = new Point(centerX - crossWidth, centerY, PIECE_HEIGHT_OFFSET + height + crossHeight * 0.7);
        Point crossRight = new Point(centerX + crossWidth, centerY, PIECE_HEIGHT_OFFSET + height + crossHeight * 0.7);
        Point crossFront = new Point(centerX, centerY - crossWidth, PIECE_HEIGHT_OFFSET + height + crossHeight * 0.7);
        Point crossBack = new Point(centerX, centerY + crossWidth, PIECE_HEIGHT_OFFSET + height + crossHeight * 0.7);

        Triangle[] triangles = new Triangle[70]; // Base + body + crown + cross
        int index = 0;

        Material material = isWhite ? whitePieceMaterial : blackPieceMaterial;
        Color color = isWhite ? whitePieceColor : blackPieceColor;

        // Base triangles
        for (int i = 0; i < 10; i++) {
            triangles[index++] = (Triangle) new Triangle(base, basePoints[i], basePoints[(i + 1) % 10])
                    .setEmission(color).setMaterial(material);
        }

        // Body triangles
        for (int i = 0; i < 10; i++) {
            triangles[index++] = (Triangle) new Triangle(basePoints[i], middlePoints[i], basePoints[(i + 1) % 10])
                    .setEmission(color).setMaterial(material);
            triangles[index++] = (Triangle) new Triangle(middlePoints[i], middlePoints[(i + 1) % 10], basePoints[(i + 1) % 10])
                    .setEmission(color).setMaterial(material);
        }

        // Top crown
        for (int i = 0; i < 8; i++) {
            triangles[index++] = (Triangle) new Triangle(top, topPoints[i], topPoints[(i + 1) % 8])
                    .setEmission(color).setMaterial(material);
        }

        // Cross
        triangles[index++] = (Triangle) new Triangle(top, crossTop, crossLeft).setEmission(color).setMaterial(material);
        triangles[index++] = (Triangle) new Triangle(top, crossTop, crossRight).setEmission(color).setMaterial(material);
        triangles[index++] = (Triangle) new Triangle(top, crossTop, crossFront).setEmission(color).setMaterial(material);
        triangles[index++] = (Triangle) new Triangle(top, crossTop, crossBack).setEmission(color).setMaterial(material);
        triangles[index++] = (Triangle) new Triangle(crossLeft, crossRight, crossFront).setEmission(color).setMaterial(material);
        triangles[index++] = (Triangle) new Triangle(crossRight, crossBack, crossFront).setEmission(color).setMaterial(material);

        return java.util.Arrays.copyOf(triangles, index);
    }

    /**
     * Creates a rook chess piece using triangles.
     *
     * @param centerX X coordinate of the piece center
     * @param centerY Y coordinate of the piece center
     * @param isWhite true for white piece, false for black piece
     * @return array of triangles representing the rook
     */
    private Triangle[] createRook(double centerX, double centerY, boolean isWhite) {
        double baseRadius = SQUARE_SIZE * 0.22;
        double topRadius = SQUARE_SIZE * 0.20;
        double height = SQUARE_SIZE * 1.1; // Doubled from 0.55
        double battlementHeight = SQUARE_SIZE * 0.2; // Doubled from 0.1

        Point base = new Point(centerX, centerY, PIECE_HEIGHT_OFFSET);
        Point top = new Point(centerX, centerY, PIECE_HEIGHT_OFFSET + height);

        // Create base and top circle points (8-sided)
        Point[] basePoints = new Point[8];
        Point[] topPoints = new Point[8];
        Point[] battlementPoints = new Point[16]; // Alternating high and low for crenellations

        for (int i = 0; i < 8; i++) {
            double angle = i * 2 * Math.PI / 8;
            basePoints[i] = new Point(
                    centerX + baseRadius * Math.cos(angle),
                    centerY + baseRadius * Math.sin(angle),
                    PIECE_HEIGHT_OFFSET
            );
            topPoints[i] = new Point(
                    centerX + topRadius * Math.cos(angle),
                    centerY + topRadius * Math.sin(angle),
                    PIECE_HEIGHT_OFFSET + height
            );
        }

        // Create battlements (castle-like top)
        for (int i = 0; i < 16; i++) {
            double angle = i * 2 * Math.PI / 16;
            double radius = topRadius * 0.9;
            double battlementZ = (i % 2 == 0) ?
                    PIECE_HEIGHT_OFFSET + height + battlementHeight :
                    PIECE_HEIGHT_OFFSET + height + battlementHeight * 0.5;

            battlementPoints[i] = new Point(
                    centerX + radius * Math.cos(angle),
                    centerY + radius * Math.sin(angle),
                    battlementZ
            );
        }

        Triangle[] triangles = new Triangle[48]; // Base + sides + battlements
        int index = 0;

        Material material = isWhite ? whitePieceMaterial : blackPieceMaterial;
        Color color = isWhite ? whitePieceColor : blackPieceColor;

        // Base triangles
        for (int i = 0; i < 8; i++) {
            triangles[index++] = (Triangle) new Triangle(base, basePoints[i], basePoints[(i + 1) % 8])
                    .setEmission(color).setMaterial(material);
        }

        // Side triangles
        for (int i = 0; i < 8; i++) {
            triangles[index++] = (Triangle) new Triangle(basePoints[i], topPoints[i], basePoints[(i + 1) % 8])
                    .setEmission(color).setMaterial(material);
            triangles[index++] = (Triangle) new Triangle(topPoints[i], topPoints[(i + 1) % 8], basePoints[(i + 1) % 8])
                    .setEmission(color).setMaterial(material);
        }

        // Battlements
        for (int i = 0; i < 16; i++) {
            triangles[index++] = (Triangle) new Triangle(top, battlementPoints[i], battlementPoints[(i + 1) % 16])
                    .setEmission(color).setMaterial(material);
        }

        return java.util.Arrays.copyOf(triangles, index);
    }

    /**
     * Creates a knight chess piece using triangles.
     *
     * @param centerX X coordinate of the piece center
     * @param centerY Y coordinate of the piece center
     * @param isWhite true for white piece, false for black piece
     * @return array of triangles representing the knight
     */
    private Triangle[] createKnight(double centerX, double centerY, boolean isWhite) {
        double baseRadius = SQUARE_SIZE * 0.20;
        double height = SQUARE_SIZE * 1.0; // Doubled from 0.5
        double neckLength = SQUARE_SIZE * 0.15;
        double headSize = SQUARE_SIZE * 0.12;

        Point base = new Point(centerX, centerY, PIECE_HEIGHT_OFFSET);
        Point bodyTop = new Point(centerX, centerY, PIECE_HEIGHT_OFFSET + height * 0.6);
        Point neckStart = new Point(centerX, centerY - neckLength * 0.3, PIECE_HEIGHT_OFFSET + height * 0.7);
        Point headBase = new Point(centerX, centerY - neckLength, PIECE_HEIGHT_OFFSET + height * 0.8);
        Point headTop = new Point(centerX, centerY - neckLength, PIECE_HEIGHT_OFFSET + height);
        Point muzzle = new Point(centerX, centerY - neckLength - headSize, PIECE_HEIGHT_OFFSET + height * 0.9);
        Point ear1 = new Point(centerX - headSize * 0.3, centerY - neckLength + headSize * 0.3, PIECE_HEIGHT_OFFSET + height + headSize * 0.5);
        Point ear2 = new Point(centerX + headSize * 0.3, centerY - neckLength + headSize * 0.3, PIECE_HEIGHT_OFFSET + height + headSize * 0.5);

        // Create base circle points (8-sided)
        Point[] basePoints = new Point[8];
        Point[] bodyPoints = new Point[8];

        for (int i = 0; i < 8; i++) {
            double angle = i * 2 * Math.PI / 8;
            basePoints[i] = new Point(
                    centerX + baseRadius * Math.cos(angle),
                    centerY + baseRadius * Math.sin(angle),
                    PIECE_HEIGHT_OFFSET
            );
            bodyPoints[i] = new Point(
                    centerX + baseRadius * 0.7 * Math.cos(angle),
                    centerY + baseRadius * 0.7 * Math.sin(angle),
                    PIECE_HEIGHT_OFFSET + height * 0.6
            );
        }

        Triangle[] triangles = new Triangle[40]; // Base + body + neck + head
        int index = 0;

        Material material = isWhite ? whitePieceMaterial : blackPieceMaterial;
        Color color = isWhite ? whitePieceColor : blackPieceColor;

        // Base triangles
        for (int i = 0; i < 8; i++) {
            triangles[index++] = (Triangle) new Triangle(base, basePoints[i], basePoints[(i + 1) % 8])
                    .setEmission(color).setMaterial(material);
        }

        // Body triangles
        for (int i = 0; i < 8; i++) {
            triangles[index++] = (Triangle) new Triangle(basePoints[i], bodyPoints[i], basePoints[(i + 1) % 8])
                    .setEmission(color).setMaterial(material);
            triangles[index++] = (Triangle) new Triangle(bodyPoints[i], bodyPoints[(i + 1) % 8], basePoints[(i + 1) % 8])
                    .setEmission(color).setMaterial(material);
        }

        // Neck triangles
        triangles[index++] = (Triangle) new Triangle(bodyTop, neckStart, headBase).setEmission(color).setMaterial(material);

        // Head triangles
        triangles[index++] = (Triangle) new Triangle(headBase, headTop, muzzle).setEmission(color).setMaterial(material);
        triangles[index++] = (Triangle) new Triangle(headTop, ear1, ear2).setEmission(color).setMaterial(material);
        triangles[index++] = (Triangle) new Triangle(headBase, ear1, headTop).setEmission(color).setMaterial(material);
        triangles[index++] = (Triangle) new Triangle(headBase, headTop, ear2).setEmission(color).setMaterial(material);
        triangles[index++] = (Triangle) new Triangle(muzzle, ear1, ear2).setEmission(color).setMaterial(material);

        return java.util.Arrays.copyOf(triangles, index);
    }

    /**
     * Creates a bishop chess piece using triangles.
     *
     * @param centerX X coordinate of the piece center
     * @param centerY Y coordinate of the piece center
     * @param isWhite true for white piece, false for black piece
     * @return array of triangles representing the bishop
     */
    private Triangle[] createBishop(double centerX, double centerY, boolean isWhite) {
        double baseRadius = SQUARE_SIZE * 0.20;
        double middleRadius = SQUARE_SIZE * 0.15;
        double topRadius = SQUARE_SIZE * 0.08;
        double height = SQUARE_SIZE * 1.3; // Doubled from 0.65
        double mitreHeight = SQUARE_SIZE * 0.3; // Doubled from 0.15

        Point base = new Point(centerX, centerY, PIECE_HEIGHT_OFFSET);
        Point middle = new Point(centerX, centerY, PIECE_HEIGHT_OFFSET + height * 0.7);
        Point top = new Point(centerX, centerY, PIECE_HEIGHT_OFFSET + height);
        Point mitreTop = new Point(centerX, centerY, PIECE_HEIGHT_OFFSET + height + mitreHeight);
        Point mitreSlit = new Point(centerX, centerY, PIECE_HEIGHT_OFFSET + height + mitreHeight * 0.8);

        // Create base circle points (10-sided for smoother curves)
        Point[] basePoints = new Point[10];
        Point[] middlePoints = new Point[10];
        Point[] topPoints = new Point[8];
        Point[] mitrePoints = new Point[8];

        for (int i = 0; i < 10; i++) {
            double angle = i * 2 * Math.PI / 10;
            basePoints[i] = new Point(
                    centerX + baseRadius * Math.cos(angle),
                    centerY + baseRadius * Math.sin(angle),
                    PIECE_HEIGHT_OFFSET
            );
            middlePoints[i] = new Point(
                    centerX + middleRadius * Math.cos(angle),
                    centerY + middleRadius * Math.sin(angle),
                    PIECE_HEIGHT_OFFSET + height * 0.7
            );
        }

        for (int i = 0; i < 8; i++) {
            double angle = i * 2 * Math.PI / 8;
            topPoints[i] = new Point(
                    centerX + topRadius * Math.cos(angle),
                    centerY + topRadius * Math.sin(angle),
                    PIECE_HEIGHT_OFFSET + height
            );
            mitrePoints[i] = new Point(
                    centerX + topRadius * 0.6 * Math.cos(angle),
                    centerY + topRadius * 0.6 * Math.sin(angle),
                    PIECE_HEIGHT_OFFSET + height + mitreHeight * 0.5
            );
        }

        Triangle[] triangles = new Triangle[60]; // Base + body + mitre
        int index = 0;

        Material material = isWhite ? whitePieceMaterial : blackPieceMaterial;
        Color color = isWhite ? whitePieceColor : blackPieceColor;

        // Base triangles
        for (int i = 0; i < 10; i++) {
            triangles[index++] = (Triangle) new Triangle(base, basePoints[i], basePoints[(i + 1) % 10])
                    .setEmission(color).setMaterial(material);
        }

        // Lower body triangles
        for (int i = 0; i < 10; i++) {
            triangles[index++] = (Triangle) new Triangle(basePoints[i], middlePoints[i], basePoints[(i + 1) % 10])
                    .setEmission(color).setMaterial(material);
            triangles[index++] = (Triangle) new Triangle(middlePoints[i], middlePoints[(i + 1) % 10], basePoints[(i + 1) % 10])
                    .setEmission(color).setMaterial(material);
        }

        // Upper body triangles
        for (int i = 0; i < 8; i++) {
            triangles[index++] = (Triangle) new Triangle(middle, topPoints[i], topPoints[(i + 1) % 8])
                    .setEmission(color).setMaterial(material);
        }

        // Mitre (bishop's hat) triangles
        for (int i = 0; i < 8; i++) {
            triangles[index++] = (Triangle) new Triangle(top, mitrePoints[i], mitrePoints[(i + 1) % 8])
                    .setEmission(color).setMaterial(material);
        }

        // Mitre top
        triangles[index++] = (Triangle) new Triangle(mitreTop, mitreSlit, mitrePoints[0]).setEmission(color).setMaterial(material);
        triangles[index++] = (Triangle) new Triangle(mitreTop, mitreSlit, mitrePoints[4]).setEmission(color).setMaterial(material);

        return java.util.Arrays.copyOf(triangles, index);
    }

    /**
     * Creates a chess clock with rectangular box, buttons, and clock faces.
     *
     * @param centerX X coordinate of the clock center
     * @param centerY Y coordinate of the clock center
     * @return array of geometries representing the chess clock
     */
    private Geometry[] createChessClock(double centerX, double centerY) {
        // Scale factor to make clock bigger
        double scaleFactor = 2.5;

        double boxWidth = SQUARE_SIZE * 1.5 * scaleFactor;
        double boxDepth = SQUARE_SIZE * 0.8 * scaleFactor;
        double boxHeight = SQUARE_SIZE * 0.6 * scaleFactor;
        double buttonRadius = SQUARE_SIZE * 0.15 * scaleFactor;
        double clockFaceRadius = SQUARE_SIZE * 0.25 * scaleFactor;
        double handLength = SQUARE_SIZE * 0.18 * scaleFactor;
        double handWidth = SQUARE_SIZE * 0.02 * scaleFactor;

        double clockZ = PIECE_HEIGHT_OFFSET;

        // Rotate the clock to face toward the board (90-degree rotation around Z-axis)
        // Original orientation: clock faces on front (Y-negative side)
        // New orientation: clock faces on left side (X-negative side, facing the board)

        // Box corner points - rotated 90 degrees to face the board
        Point boxBottomFrontLeft = new Point(centerX - boxDepth / 2, centerY - boxWidth / 2, clockZ);
        Point boxBottomFrontRight = new Point(centerX - boxDepth / 2, centerY + boxWidth / 2, clockZ);
        Point boxBottomBackLeft = new Point(centerX + boxDepth / 2, centerY - boxWidth / 2, clockZ);
        Point boxBottomBackRight = new Point(centerX + boxDepth / 2, centerY + boxWidth / 2, clockZ);

        Point boxTopFrontLeft = new Point(centerX - boxDepth / 2, centerY - boxWidth / 2, clockZ + boxHeight);
        Point boxTopFrontRight = new Point(centerX - boxDepth / 2, centerY + boxWidth / 2, clockZ + boxHeight);
        Point boxTopBackLeft = new Point(centerX + boxDepth / 2, centerY - boxWidth / 2, clockZ + boxHeight);
        Point boxTopBackRight = new Point(centerX + boxDepth / 2, centerY + boxWidth / 2, clockZ + boxHeight);

        // Button positions on top of the box (rotated coordinates)
        Point button1Center = new Point(centerX, centerY - boxWidth / 4, clockZ + boxHeight);
        Point button2Center = new Point(centerX, centerY + boxWidth / 4, clockZ + boxHeight);

        // Clock face positions on the front face (now facing toward the board)
        Point clockFace1Center = new Point(centerX - boxDepth / 2 - 2, centerY - boxWidth / 4, clockZ + boxHeight / 2);
        Point clockFace2Center = new Point(centerX - boxDepth / 2 - 2, centerY + boxWidth / 4, clockZ + boxHeight / 2);

        // Create geometry array
        Geometry[] geometries = new Geometry[100]; // Estimated size
        int index = 0;

        // === CREATE BOX (6 faces, each made of 2 triangles) ===

        // Bottom face
        geometries[index++] = new Triangle(boxBottomFrontLeft, boxBottomFrontRight, boxBottomBackRight)
                .setEmission(clockBoxColor).setMaterial(clockBoxMaterial);
        geometries[index++] = new Triangle(boxBottomFrontLeft, boxBottomBackRight, boxBottomBackLeft)
                .setEmission(clockBoxColor).setMaterial(clockBoxMaterial);

        // Top face
        geometries[index++] = new Triangle(boxTopFrontLeft, boxTopBackLeft, boxTopBackRight)
                .setEmission(clockBoxColor).setMaterial(clockBoxMaterial);
        geometries[index++] = new Triangle(boxTopFrontLeft, boxTopBackRight, boxTopFrontRight)
                .setEmission(clockBoxColor).setMaterial(clockBoxMaterial);

        // Front face (now facing toward the board)
        geometries[index++] = new Triangle(boxBottomFrontLeft, boxTopFrontLeft, boxTopFrontRight)
                .setEmission(clockBoxColor).setMaterial(clockBoxMaterial);
        geometries[index++] = new Triangle(boxBottomFrontLeft, boxTopFrontRight, boxBottomFrontRight)
                .setEmission(clockBoxColor).setMaterial(clockBoxMaterial);

        // Back face
        geometries[index++] = new Triangle(boxBottomBackLeft, boxBottomBackRight, boxTopBackRight)
                .setEmission(clockBoxColor).setMaterial(clockBoxMaterial);
        geometries[index++] = new Triangle(boxBottomBackLeft, boxTopBackRight, boxTopBackLeft)
                .setEmission(clockBoxColor).setMaterial(clockBoxMaterial);

        // Left face
        geometries[index++] = new Triangle(boxBottomFrontLeft, boxBottomBackLeft, boxTopBackLeft)
                .setEmission(clockBoxColor).setMaterial(clockBoxMaterial);
        geometries[index++] = new Triangle(boxBottomFrontLeft, boxTopBackLeft, boxTopFrontLeft)
                .setEmission(clockBoxColor).setMaterial(clockBoxMaterial);

        // Right face
        geometries[index++] = new Triangle(boxBottomFrontRight, boxTopFrontRight, boxTopBackRight)
                .setEmission(clockBoxColor).setMaterial(clockBoxMaterial);
        geometries[index++] = new Triangle(boxBottomFrontRight, boxTopBackRight, boxBottomBackRight)
                .setEmission(clockBoxColor).setMaterial(clockBoxMaterial);

        // === CREATE BUTTONS (2 spheres) ===
        geometries[index++] = new Sphere(button1Center, buttonRadius)
                .setEmission(button1Color).setMaterial(clockButtonMaterial);
        geometries[index++] = new Sphere(button2Center, buttonRadius)
                .setEmission(button2Color).setMaterial(clockButtonMaterial);

        // === CREATE CLOCK FACES (2 circular faces made of triangles) ===
        index = createClockFaceRotated(geometries, index, clockFace1Center, clockFaceRadius, true);
        index = createClockFaceRotated(geometries, index, clockFace2Center, clockFaceRadius, true);

        // === CREATE CLOCK HANDS (4 small triangles) ===
        // Clock 1 hands (showing 10:15)
        index = createClockHandsRotated(geometries, index, clockFace1Center, handLength, handWidth, 10, 15);

        // Clock 2 hands (showing 2:45)
        index = createClockHandsRotated(geometries, index, clockFace2Center, handLength, handWidth, 2, 45);

        return java.util.Arrays.copyOf(geometries, index);
    }

    /**
     * Creates a circular clock face using triangular segments (rotated to face the board).
     *
     * @param geometries array to add triangles to
     * @param startIndex starting index in the array
     * @param center     center point of the clock face
     * @param radius     radius of the clock face
     * @param faceBoard  true if facing toward the board
     * @return updated index after adding triangles
     */
    private int createClockFaceRotated(Geometry[] geometries, int startIndex, Point center, double radius, boolean faceBoard) {
        int segments = 12; // 12-sided circle for smooth appearance
        int index = startIndex;

        // Create circle edge points (rotated 90 degrees to face the board)
        Point[] edgePoints = new Point[segments];
        for (int i = 0; i < segments; i++) {
            double angle = i * 2 * Math.PI / segments;
            if (faceBoard) {
                // Clock face oriented in the YZ plane (facing toward board in X direction)
                edgePoints[i] = new Point(
                        center.getX(),
                        center.getY() + radius * Math.cos(angle),
                        center.getZ() + radius * Math.sin(angle)
                );
            } else {
                // Original orientation (XZ plane)
                edgePoints[i] = new Point(
                        center.getX() + radius * Math.cos(angle),
                        center.getY(),
                        center.getZ() + radius * Math.sin(angle)
                );
            }
        }

        // Create triangular fan from center to edges
        for (int i = 0; i < segments; i++) {
            geometries[index++] = new Triangle(center, edgePoints[i], edgePoints[(i + 1) % segments])
                    .setEmission(clockFaceColor).setMaterial(clockFaceMaterial);
        }

        return index;
    }

    /**
     * Creates clock hands (hour and minute hands) as small triangles (rotated to face the board).
     *
     * @param geometries array to add triangles to
     * @param startIndex starting index in the array
     * @param center     center point of the clock face
     * @param handLength length of the hands
     * @param handWidth  width of the hands
     * @param hour       hour to display (0-12)
     * @param minute     minute to display (0-59)
     * @return updated index after adding triangles
     */
    private int createClockHandsRotated(Geometry[] geometries, int startIndex, Point center, double handLength, double handWidth, int hour, int minute) {
        int index = startIndex;

        // Calculate angles for hands
        double hourAngle = ((hour % 12) * 30 + minute * 0.5) * Math.PI / 180; // Hour hand
        double minuteAngle = (minute * 6) * Math.PI / 180; // Minute hand

        // Hour hand (shorter, thicker) - rotated to face board
        double hourHandLength = handLength * 0.6;
        Point hourHandTip = new Point(
                center.getX() - 1, // Slightly in front of clock face (toward board)
                center.getY(),
                center.getZ() + hourHandLength * Math.sin(hourAngle)
        );
        Point hourHandBase1 = new Point(
                center.getX() - 1,
                center.getY() - handWidth,
                center.getZ() - handWidth
        );
        Point hourHandBase2 = new Point(
                center.getX() - 1,
                center.getY() + handWidth,
                center.getZ() - handWidth
        );

        geometries[index++] = new Triangle(center, hourHandTip, hourHandBase1)
                .setEmission(clockHandColor).setMaterial(clockHandMaterial);
        geometries[index++] = new Triangle(center, hourHandBase2, hourHandTip)
                .setEmission(clockHandColor).setMaterial(clockHandMaterial);

        // Minute hand (longer, thinner) - rotated to face board
        Point minuteHandTip = new Point(
                center.getX() - 1, // Slightly in front of clock face (toward board)
                center.getY(),
                center.getZ() + handLength * Math.sin(minuteAngle)
        );
        Point minuteHandBase1 = new Point(
                center.getX() - 1,
                center.getY() - handWidth * 0.5,
                center.getZ() - handWidth * 0.5
        );
        Point minuteHandBase2 = new Point(
                center.getX() - 1,
                center.getY() + handWidth * 0.5,
                center.getZ() - handWidth * 0.5
        );

        geometries[index++] = new Triangle(center, minuteHandTip, minuteHandBase1)
                .setEmission(clockHandColor).setMaterial(clockHandMaterial);
        geometries[index++] = new Triangle(center, minuteHandBase2, minuteHandTip)
                .setEmission(clockHandColor).setMaterial(clockHandMaterial);

        return index;
    }

    /**
     * Places the chess clock next to the chess board on the right side.
     */
    private void placeChessClock() {
        // Position clock to the right of the chess board
        double clockX = BOARD_SIZE / 2 + SQUARE_SIZE * 2; // Right of the board
        double clockY = 0; // Center aligned with board

        // Create and place the chess clock
        Geometry[] clockGeometries = createChessClock(clockX, clockY);
        for (Geometry geometry : clockGeometries) {
            if (geometry != null) {
                scene.geometries.add(geometry);
            }
        }
    }

    /**
     * Places chess pieces on the board in starting positions.
     */
    private void placeChessPieces() {
        double startX = -BOARD_SIZE / 2;
        double startY = -BOARD_SIZE / 2;

        // Calculate center positions for each square
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                double centerX = startX + (col + 0.5) * SQUARE_SIZE;
                double centerY = startY + (row + 0.5) * SQUARE_SIZE;

                Triangle[] pieceTriangles = null;

                // Place pieces according to chess starting positions
                if (row == 0) { // Black back row
                    if (col == 0 || col == 7) {
                        // Black Rooks
                        pieceTriangles = createRook(centerX, centerY, false);
                    } else if (col == 1 || col == 6) {
                        // Black Knights
                        pieceTriangles = createKnight(centerX, centerY, false);
                    } else if (col == 2 || col == 5) {
                        // Black Bishops
                        pieceTriangles = createBishop(centerX, centerY, false);
                    } else if (col == 3) {
                        // Black Queen
                        pieceTriangles = createQueen(centerX, centerY, false);
                    } else {
                        // Black King
                        pieceTriangles = createKing(centerX, centerY, false);
                    }
                } else if (row == 1) { // Black pawns
                    pieceTriangles = createPawn(centerX, centerY, false);
                } else if (row == 6) { // White pawns
                    pieceTriangles = createPawn(centerX, centerY, true);
                } else if (row == 7) { // White back row
                    if (col == 0 || col == 7) {
                        // White Rooks
                        pieceTriangles = createRook(centerX, centerY, true);
                    } else if (col == 1 || col == 6) {
                        // White Knights
                        pieceTriangles = createKnight(centerX, centerY, true);
                    } else if (col == 2 || col == 5) {
                        // White Bishops
                        pieceTriangles = createBishop(centerX, centerY, true);
                    } else if (col == 3) {
                        // White Queen
                        pieceTriangles = createQueen(centerX, centerY, true);
                    } else if (col == 4) {
                        // White King
                        pieceTriangles = createKing(centerX, centerY, true);
                    }
                }

                // Add piece triangles to scene
                if (pieceTriangles != null) {
                    for (Triangle triangle : pieceTriangles) {
                        if (triangle != null) {
                            scene.geometries.add(triangle);
                        }
                    }
                }
            }
        }
    }

    /**
     * Creates a football trophy with triangular base and sparkling ball on top.
     *
     * @param centerX X coordinate of the trophy center
     * @param centerY Y coordinate of the trophy center
     * @return array of geometries representing the trophy
     */
    private Geometry[] createTrophy(double centerX, double centerY) {
        double baseRadius = SQUARE_SIZE * 0.8; // Wide base
        double baseHeight = SQUARE_SIZE * 1.5; // Tall base
        double cupRadius = SQUARE_SIZE * 0.6;
        double cupHeight = SQUARE_SIZE * 0.8;
        double ballRadius = SQUARE_SIZE * 0.4;
        double totalHeight = SQUARE_SIZE * 3.5; // Real trophy height relative to chess board

        double trophyZ = PIECE_HEIGHT_OFFSET;

        // Base platform (circular base made of triangles)
        Point baseCenter = new Point(centerX, centerY, trophyZ);
        Point baseTop = new Point(centerX, centerY, trophyZ + baseHeight);

        // Create base circle points (12-sided for smooth appearance)
        Point[] baseBottomPoints = new Point[12];
        Point[] baseTopPoints = new Point[12];

        for (int i = 0; i < 12; i++) {
            double angle = i * 2 * Math.PI / 12;
            baseBottomPoints[i] = new Point(
                    centerX + baseRadius * Math.cos(angle),
                    centerY + baseRadius * Math.sin(angle),
                    trophyZ
            );
            baseTopPoints[i] = new Point(
                    centerX + (baseRadius * 0.8) * Math.cos(angle),
                    centerY + (baseRadius * 0.8) * Math.sin(angle),
                    trophyZ + baseHeight
            );
        }

        // Cup/chalice part (narrower than base)
        Point cupBase = new Point(centerX, centerY, trophyZ + baseHeight);
        Point cupTop = new Point(centerX, centerY, trophyZ + baseHeight + cupHeight);

        Point[] cupBottomPoints = new Point[10];
        Point[] cupTopPoints = new Point[10];

        for (int i = 0; i < 10; i++) {
            double angle = i * 2 * Math.PI / 10;
            cupBottomPoints[i] = new Point(
                    centerX + cupRadius * 0.6 * Math.cos(angle),
                    centerY + cupRadius * 0.6 * Math.sin(angle),
                    trophyZ + baseHeight
            );
            cupTopPoints[i] = new Point(
                    centerX + cupRadius * Math.cos(angle),
                    centerY + cupRadius * Math.sin(angle),
                    trophyZ + baseHeight + cupHeight
            );
        }

        // Trophy handles (decorative triangular elements)
        Point handleLeft1 = new Point(centerX - cupRadius * 1.2, centerY, trophyZ + baseHeight + cupHeight * 0.3);
        Point handleLeft2 = new Point(centerX - cupRadius * 1.4, centerY, trophyZ + baseHeight + cupHeight * 0.5);
        Point handleLeft3 = new Point(centerX - cupRadius * 1.2, centerY, trophyZ + baseHeight + cupHeight * 0.7);
        Point handleLeftConnect1 = new Point(centerX - cupRadius * 0.9, centerY, trophyZ + baseHeight + cupHeight * 0.4);
        Point handleLeftConnect2 = new Point(centerX - cupRadius * 0.9, centerY, trophyZ + baseHeight + cupHeight * 0.6);

        Point handleRight1 = new Point(centerX + cupRadius * 1.2, centerY, trophyZ + baseHeight + cupHeight * 0.3);
        Point handleRight2 = new Point(centerX + cupRadius * 1.4, centerY, trophyZ + baseHeight + cupHeight * 0.5);
        Point handleRight3 = new Point(centerX + cupRadius * 1.2, centerY, trophyZ + baseHeight + cupHeight * 0.7);
        Point handleRightConnect1 = new Point(centerX + cupRadius * 0.9, centerY, trophyZ + baseHeight + cupHeight * 0.4);
        Point handleRightConnect2 = new Point(centerX + cupRadius * 0.9, centerY, trophyZ + baseHeight + cupHeight * 0.6);

        // Ball position
        Point ballCenter = new Point(centerX, centerY, trophyZ + baseHeight + cupHeight + ballRadius);

        // Count total geometries needed
        int totalGeometries = 1 + (12 * 3) + (10 * 3) + 6; // ball + base triangles + cup triangles + handles
        Geometry[] geometries = new Geometry[totalGeometries];
        int index = 0;

        // Add sparkling ball on top
        geometries[index++] = new Sphere(ballCenter, ballRadius)
                .setEmission(trophySparkleColor)
                .setMaterial(trophyBallMaterial);

        // Base triangles (bottom circle)
        for (int i = 0; i < 12; i++) {
            geometries[index++] = new Triangle(baseCenter, baseBottomPoints[i], baseBottomPoints[(i + 1) % 12])
                    .setEmission(trophyDarkColor).setMaterial(trophyBaseMaterial);
        }

        // Base side triangles
        for (int i = 0; i < 12; i++) {
            geometries[index++] = new Triangle(baseBottomPoints[i], baseTopPoints[i], baseBottomPoints[(i + 1) % 12])
                    .setEmission(trophyGoldColor).setMaterial(trophyBaseMaterial);
            geometries[index++] = new Triangle(baseTopPoints[i], baseTopPoints[(i + 1) % 12], baseBottomPoints[(i + 1) % 12])
                    .setEmission(trophyGoldColor).setMaterial(trophyBaseMaterial);
        }

        // Cup triangles
        for (int i = 0; i < 10; i++) {
            geometries[index++] = new Triangle(cupBase, cupBottomPoints[i], cupBottomPoints[(i + 1) % 10])
                    .setEmission(trophyDarkColor).setMaterial(trophyBaseMaterial);
        }

        // Cup side triangles
        for (int i = 0; i < 10; i++) {
            geometries[index++] = new Triangle(cupBottomPoints[i], cupTopPoints[i], cupBottomPoints[(i + 1) % 10])
                    .setEmission(trophyGoldColor).setMaterial(trophyBaseMaterial);
            geometries[index++] = new Triangle(cupTopPoints[i], cupTopPoints[(i + 1) % 10], cupBottomPoints[(i + 1) % 10])
                    .setEmission(trophyGoldColor).setMaterial(trophyBaseMaterial);
        }

        // Left handle triangles
        geometries[index++] = new Triangle(handleLeft1, handleLeft2, handleLeft3)
                .setEmission(trophyDarkColor).setMaterial(trophyBaseMaterial);
        geometries[index++] = new Triangle(handleLeft1, handleLeftConnect1, handleLeft2)
                .setEmission(trophyGoldColor).setMaterial(trophyBaseMaterial);
        geometries[index++] = new Triangle(handleLeft2, handleLeftConnect2, handleLeft3)
                .setEmission(trophyGoldColor).setMaterial(trophyBaseMaterial);

        // Right handle triangles
        geometries[index++] = new Triangle(handleRight1, handleRight2, handleRight3)
                .setEmission(trophyDarkColor).setMaterial(trophyBaseMaterial);
        geometries[index++] = new Triangle(handleRight1, handleRightConnect1, handleRight2)
                .setEmission(trophyGoldColor).setMaterial(trophyBaseMaterial);
        geometries[index++] = new Triangle(handleRight2, handleRightConnect2, handleRight3)
                .setEmission(trophyGoldColor).setMaterial(trophyBaseMaterial);

        return java.util.Arrays.copyOf(geometries, index);
    }

    /**
     * Creates a circular glass display case around the trophy using triangles.
     *
     * @param centerX X coordinate of the trophy center
     * @param centerY Y coordinate of the trophy center
     * @return array of triangles representing the display case
     */
    private Triangle[] createTrophyDisplayCase(double centerX, double centerY) {
        double caseRadius = SQUARE_SIZE * 1.0; // Slightly larger than trophy base
        double caseHeight = SQUARE_SIZE * 4.2; // Taller than trophy to provide clearance
        double wallThickness = 3.0; // Glass thickness
        int segments = 16; // Number of triangular segments for smooth circular appearance

        double caseZ = PIECE_HEIGHT_OFFSET;

        // Calculate inner and outer radii
        double innerRadius = caseRadius - wallThickness;
        double outerRadius = caseRadius;

        // Create points for bottom circle (inner and outer)
        Point[] bottomInnerPoints = new Point[segments];
        Point[] bottomOuterPoints = new Point[segments];

        // Create points for top circle (inner and outer)
        Point[] topInnerPoints = new Point[segments];
        Point[] topOuterPoints = new Point[segments];

        // Center points for bottom and top
        Point bottomInnerCenter = new Point(centerX, centerY, caseZ);
        Point bottomOuterCenter = new Point(centerX, centerY, caseZ - wallThickness);
        Point topInnerCenter = new Point(centerX, centerY, caseZ + caseHeight);
        Point topOuterCenter = new Point(centerX, centerY, caseZ + caseHeight + wallThickness);

        // Generate circle points
        for (int i = 0; i < segments; i++) {
            double angle = i * 2 * Math.PI / segments;
            double cosAngle = Math.cos(angle);
            double sinAngle = Math.sin(angle);

            // Bottom points
            bottomInnerPoints[i] = new Point(
                    centerX + innerRadius * cosAngle,
                    centerY + innerRadius * sinAngle,
                    caseZ
            );
            bottomOuterPoints[i] = new Point(
                    centerX + outerRadius * cosAngle,
                    centerY + outerRadius * sinAngle,
                    caseZ
            );

            // Top points
            topInnerPoints[i] = new Point(
                    centerX + innerRadius * cosAngle,
                    centerY + innerRadius * sinAngle,
                    caseZ + caseHeight
            );
            topOuterPoints[i] = new Point(
                    centerX + outerRadius * cosAngle,
                    centerY + outerRadius * sinAngle,
                    caseZ + caseHeight
            );
        }

        // Calculate total number of triangles needed
        int totalTriangles = segments * 8; // 2 for bottom + 2 for top + 4 for walls per segment
        Triangle[] triangles = new Triangle[totalTriangles];
        int index = 0;

        // Create bottom surface (glass base)
        for (int i = 0; i < segments; i++) {
            int nextI = (i + 1) % segments;

            // Bottom inner surface (inside the case)
            triangles[index++] = (Triangle) new Triangle(bottomInnerCenter, bottomInnerPoints[nextI], bottomInnerPoints[i])
                    .setEmission(trophyCaseColor).setMaterial(trophyCaseMaterial);

            // Bottom outer surface (bottom of glass)
            triangles[index++] = (Triangle) new Triangle(bottomOuterCenter, bottomOuterPoints[i], bottomOuterPoints[nextI])
                    .setEmission(trophyCaseColor).setMaterial(trophyCaseMaterial);
        }

        // Create top surface (glass lid)
        for (int i = 0; i < segments; i++) {
            int nextI = (i + 1) % segments;

            // Top inner surface (inside the case)
            triangles[index++] = (Triangle) new Triangle(topInnerCenter, topInnerPoints[i], topInnerPoints[nextI])
                    .setEmission(trophyCaseColor).setMaterial(trophyCaseMaterial);

            // Top outer surface (top of glass)
            triangles[index++] = (Triangle) new Triangle(topOuterCenter, topOuterPoints[nextI], topOuterPoints[i])
                    .setEmission(trophyCaseColor).setMaterial(trophyCaseMaterial);
        }

        // Create cylindrical walls
        for (int i = 0; i < segments; i++) {
            int nextI = (i + 1) % segments;

            // Outer wall (visible from outside)
            triangles[index++] = (Triangle) new Triangle(bottomOuterPoints[i], topOuterPoints[i], bottomOuterPoints[nextI])
                    .setEmission(trophyCaseColor).setMaterial(trophyCaseMaterial);
            triangles[index++] = (Triangle) new Triangle(topOuterPoints[i], topOuterPoints[nextI], bottomOuterPoints[nextI])
                    .setEmission(trophyCaseColor).setMaterial(trophyCaseMaterial);

            // Inner wall (visible from inside)
            triangles[index++] = (Triangle) new Triangle(bottomInnerPoints[i], bottomInnerPoints[nextI], topInnerPoints[i])
                    .setEmission(trophyCaseColor).setMaterial(trophyCaseMaterial);
            triangles[index++] = (Triangle) new Triangle(topInnerPoints[i], bottomInnerPoints[nextI], topInnerPoints[nextI])
                    .setEmission(trophyCaseColor).setMaterial(trophyCaseMaterial);
        }

        return java.util.Arrays.copyOf(triangles, index);
    }

    /**
     * Places the trophy with its display case next to the chess board on the left side.
     */
    private void placeTrophy() {
        // Position trophy to the left of the chess board
        double trophyX = -BOARD_SIZE / 2 - SQUARE_SIZE * 2; // Left of the board
        double trophyY = 0; // Center aligned with board

        // Create and place the trophy
        Geometry[] trophyGeometries = createTrophy(trophyX, trophyY);
        for (Geometry geometry : trophyGeometries) {
            if (geometry != null) {
                scene.geometries.add(geometry);
            }
        }

        // Create and place the display case around the trophy
        Triangle[] displayCaseTriangles = createTrophyDisplayCase(trophyX, trophyY);
        for (Triangle triangle : displayCaseTriangles) {
            if (triangle != null) {
                scene.geometries.add(triangle);
            }
        }
    }

    /**
     * Creates the chess board using triangles for each square.
     */
    private void createChessBoard() {
        // Starting position (center the board)
        double startX = -BOARD_SIZE / 2;
        double startY = -BOARD_SIZE / 2;
        double boardZ = 0;

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                // Determine if square should be white or black
                boolean isWhite = (row + col) % 2 == 0;

                // Calculate square position
                double x1 = startX + col * SQUARE_SIZE;
                double y1 = startY + row * SQUARE_SIZE;
                double x2 = x1 + SQUARE_SIZE;
                double y2 = y1 + SQUARE_SIZE;

                // Create the four corners of the square
                Point p1 = new Point(x1, y1, boardZ);
                Point p2 = new Point(x2, y1, boardZ);
                Point p3 = new Point(x2, y2, boardZ);
                Point p4 = new Point(x1, y2, boardZ);

                // Create the top surface of the square (2 triangles)
                Triangle triangle1 = new Triangle(p1, p2, p3);
                Triangle triangle2 = new Triangle(p1, p3, p4);

                // Create the sides of the square for height
                Point p1Bottom = new Point(x1, y1, boardZ - BOARD_HEIGHT);
                Point p2Bottom = new Point(x2, y1, boardZ - BOARD_HEIGHT);
                Point p3Bottom = new Point(x2, y2, boardZ - BOARD_HEIGHT);
                Point p4Bottom = new Point(x1, y2, boardZ - BOARD_HEIGHT);

                // Front side
                Triangle frontSide1 = new Triangle(p1, p2, p2Bottom);
                Triangle frontSide2 = new Triangle(p1, p2Bottom, p1Bottom);

                // Right side
                Triangle rightSide1 = new Triangle(p2, p3, p3Bottom);
                Triangle rightSide2 = new Triangle(p2, p3Bottom, p2Bottom);

                // Back side
                Triangle backSide1 = new Triangle(p3, p4, p4Bottom);
                Triangle backSide2 = new Triangle(p3, p4Bottom, p3Bottom);

                // Left side
                Triangle leftSide1 = new Triangle(p4, p1, p1Bottom);
                Triangle leftSide2 = new Triangle(p4, p1Bottom, p4Bottom);

                // Set material and color based on square color
                Material material = isWhite ? whiteMaterial : blackMaterial;
                Color color = isWhite ? whiteColor : blackColor;

                // Add all triangles to scene
                scene.geometries.add(
                        triangle1.setEmission(color).setMaterial(material),
                        triangle2.setEmission(color).setMaterial(material),
                        frontSide1.setEmission(color).setMaterial(material),
                        frontSide2.setEmission(color).setMaterial(material),
                        rightSide1.setEmission(color).setMaterial(material),
                        rightSide2.setEmission(color).setMaterial(material),
                        backSide1.setEmission(color).setMaterial(material),
                        backSide2.setEmission(color).setMaterial(material),
                        leftSide1.setEmission(color).setMaterial(material),
                        leftSide2.setEmission(color).setMaterial(material)
                );
            }
        }
    }

    /**
     * Creates a floor/base plane.
     */
    private void createFloor() {
        // Large floor plane
        scene.geometries.add(
                new Plane(new Vector(0, 0, 1), new Point(0, 0, -BOARD_HEIGHT - 1))
                        .setEmission(floorColor)
                        .setMaterial(floorMaterial)
        );
    }

    /**
     * Sets up the lighting for the scene.
     */
    private void setupLighting() {
        // Set background and ambient light
        scene.setBackground(backgroundColor);
        scene.setAmbientLight(new AmbientLight(new Color(20, 25, 30).scale(0.05))); // Very subtle ambient light

        // LIGHT 1: Center Board Spotlight (SpotLight) - BRIGHT YELLOW circular illumination
        scene.lights.add(
                new SpotLight(new Color(255, 255, 0), new Point(0, 0, 1500), new Vector(0, 0, -1)) // Pure bright yellow
                        .setKl(0.00001)
                        .setKq(0.0000001)
                        .setNarrowBeam(250) // Creates circular spotlight on center of board
        );

        // Calculate queen positions for targeting
        double startX = -BOARD_SIZE / 2;
        double startY = -BOARD_SIZE / 2;

        // Black Queen position: row=0, col=3
        Point blackQueenPos = new Point(
                startX + (3 + 0.5) * SQUARE_SIZE,  // col=3, center of square
                startY + (0 + 0.5) * SQUARE_SIZE,  // row=0, center of square
                PIECE_HEIGHT_OFFSET + SQUARE_SIZE * 0.6  // Approximate queen height
        );

        // White Queen position: row=7, col=3
        Point whiteQueenPos = new Point(
                startX + (3 + 0.5) * SQUARE_SIZE,  // col=3, center of square
                startY + (7 + 0.5) * SQUARE_SIZE,  // row=7, center of square
                PIECE_HEIGHT_OFFSET + SQUARE_SIZE * 0.6  // Approximate queen height
        );

        // LIGHT 2: Black Queen Shadow Creator (SpotLight) - CYAN targeting black queen
        // Position: From White's side, far to the left and high up for DIAGONAL lighting
        Point blackQueenLightPos = new Point(
                blackQueenPos.getX() - 600,  // Much further left from black queen
                blackQueenPos.getY() - 800,  // Much further from white's side
                1200  // High above for diagonal angle
        );
        Vector blackQueenDirection = blackQueenPos.subtract(blackQueenLightPos).normalize();

        scene.lights.add(
                new SpotLight(new Color(0, 255, 255), blackQueenLightPos, blackQueenDirection) // Bright cyan
                        .setKl(0.00002)
                        .setKq(0.0000002)
                        .setNarrowBeam(200) // Very focused beam on black queen
        );

        // LIGHT 3: White Queen Shadow Creator (SpotLight) - MAGENTA targeting white queen
        // Position: From Black's side, far to the left and high up for DIAGONAL lighting
        Point whiteQueenLightPos = new Point(
                whiteQueenPos.getX() - 600,  // Much further left from white queen
                whiteQueenPos.getY() + 800,  // Much further from black's side
                1200  // High above for diagonal angle
        );
        Vector whiteQueenDirection = whiteQueenPos.subtract(whiteQueenLightPos).normalize();

        scene.lights.add(
                new SpotLight(new Color(255, 0, 255), whiteQueenLightPos, whiteQueenDirection) // Bright magenta
                        .setKl(0.00002)
                        .setKq(0.0000002)
                        .setNarrowBeam(200) // Very focused beam on white queen
        );

        // LIGHT 4: Trophy Shadow Light - Right-Black Side (SpotLight) - ORANGE
        double trophyX = -BOARD_SIZE / 2 - SQUARE_SIZE * 2;
        double trophyY = 0;
        Point trophyPos = new Point(trophyX, trophyY, PIECE_HEIGHT_OFFSET + SQUARE_SIZE * 2);

        Point trophy1LightPos = new Point(600, 500, 1400); // Further away for more diagonal angle
        Vector trophy1Direction = trophyPos.subtract(trophy1LightPos).normalize();

        scene.lights.add(
                new SpotLight(new Color(255, 165, 0), trophy1LightPos, trophy1Direction) // Bright orange
                        .setKl(0.00001)
                        .setKq(0.0000001)
                        .setNarrowBeam(300) // Focused on trophy display case
        );

        // LIGHT 5: Trophy Shadow Light - Left-Back Black Side (SpotLight) - LIME GREEN
        Point trophy2LightPos = new Point(-1000, 800, 1200); // Further away for more diagonal angle
        Vector trophy2Direction = trophyPos.subtract(trophy2LightPos).normalize();

        scene.lights.add(
                new SpotLight(new Color(0, 255, 0), trophy2LightPos, trophy2Direction) // Bright lime green
                        .setKl(0.00002)
                        .setKq(0.0000002)
                        .setNarrowBeam(350) // Creates cross-lighting on trophy
        );

        // LIGHT 6: Trophy Case Interior Glow (PointLight) - DEEP RED
        double interiorHeight = PIECE_HEIGHT_OFFSET + SQUARE_SIZE * 1.5; // Inside the case, mid-height

        scene.lights.add(
                new PointLight(new Color(255, 0, 0), new Point(trophyX, trophyY, interiorHeight)) // Bright red glow
                        .setKl(0.001) // Higher attenuation since it's close-range interior lighting
                        .setKq(0.0001)
        );
    }
}