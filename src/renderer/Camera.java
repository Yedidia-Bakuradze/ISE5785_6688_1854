package renderer;

import primitives.*;
import primitives.Vector;
import sampling.*;
import scene.Scene;

import java.util.*;
import java.util.stream.IntStream;

import static primitives.Util.*;

/**
 * Represents a camera in 3D space for rendering scenes
 * The camera defines the view point and direction for rendering images
 */
public class Camera implements Cloneable {

    /**
     * Creates a deep copy of the camera object
     *
     * @return A new Camera instance with copied properties
     */
    @Override
    public Camera clone() {
        try {
            return (Camera) super.clone();
        } catch (CloneNotSupportedException ignored) {
            throw new AssertionError("It shall not happen!!!");
        }
    }

    /**
     * Builder class for constructing Camera objects using the builder pattern
     */
    public static class Builder {

        /**
         * Default constructor that initializes the camera with default target areas
         */
        public Builder() {
            camera.targetAreas.put(EffectType.DIFFUSIVE_GLASS, null);
            camera.targetAreas.put(EffectType.DEPTH_OF_FIELD, null);
            camera.targetAreas.put(EffectType.ANTI_ALIASING, null);
            camera.targetAreas.put(EffectType.SOFT_SHADOW, null);
            camera.targetAreas.put(EffectType.GLOSSY_REFLECTION, null);
        }

        /**
         * The camera instance being built
         */
        private final Camera camera = new Camera();

        /**
         * Sets the location of the camera
         *
         * @param p The position point for the camera
         * @return The builder instance for method chaining
         */
        public Builder setLocation(Point p) {
            camera.position = p;
            return this;
        }

        /**
         * Sets the direction vectors of the camera
         *
         * @param vTo The direction vector the camera points to
         * @param vUp The up vector for the camera orientation
         * @return The builder instance for method chaining
         * @throws IllegalArgumentException if vectors are not orthogonal
         */
        public Builder setDirection(Vector vTo, Vector vUp) {
            if (!isZero(vUp.dotProduct(vTo)))
                throw new IllegalArgumentException("Error: Provided vectors are not orthogonal");

            camera.vUp = vUp.normalize();
            camera.vTo = vTo.normalize();
            camera.vRight = camera.vTo.crossProduct(camera.vUp).normalize();
            return this;
        }

        /**
         * Sets the direction vectors of the camera using a point and an up vector
         *
         * @param p   The point to look at
         * @param vUp The up vector for the camera orientation
         * @return The builder instance for method chaining
         */
        public Builder setDirection(Point p, Vector vUp) {
            camera.vTo = p.subtract(camera.position).normalize();
            camera.vRight = camera.vTo.crossProduct(vUp).normalize();
            camera.vUp = camera.vRight.crossProduct(camera.vTo).normalize();
            return this;
        }

        /**
         * Sets the direction vectors of the camera using a point
         *
         * @param p The point to look at
         * @return The builder instance for method chaining
         */
        public Builder setDirection(Point p) {
            camera.vTo = p.subtract(camera.position).normalize();
            camera.vUp = Vector.AXIS_Y;
            camera.vRight = camera.vTo.crossProduct(camera.vUp).normalize();
            camera.vUp = camera.vRight.crossProduct(camera.vTo).normalize();
            return this;
        }

        /**
         * Sets the size of the view plane
         *
         * @param width  The width of the view plane
         * @param height The height of the view plane
         * @return The builder instance for method chaining
         * @throws IllegalArgumentException if width or height are not positive
         */
        public Builder setVpSize(double width, double height) {
            if (width <= 0 || height <= 0) throw new IllegalArgumentException("Width and height must be positive");
            camera.width = width;
            camera.height = height;
            return this;
        }

        /**
         * Sets the distance from the camera to the view plane
         *
         * @param d The distance value
         * @return The builder instance for method chaining
         * @throws IllegalArgumentException if distance is not positive
         */
        public Builder setVpDistance(double d) {
            if (d <= 0) throw new IllegalArgumentException("Distance must be positive");
            camera.distance = d;
            return this;
        }

        /**
         * Sets the resolution of the view plane
         *
         * @param nX The number of columns in the view plane
         * @param nY The number of rows in the view plane
         * @return The builder instance for method chaining
         * @throws IllegalArgumentException if nX or nY are not positive
         */
        public Builder setResolution(int nX, int nY) {
            camera.nX = nX;
            camera.nY = nY;
            return this;
        }

        /**
         * Sets the effect type and its configuration for the camera
         *
         * @param type   The type of effect to apply
         * @param config The sampling configuration for the effect
         * @return The builder instance for method chaining
         */
        public Builder setEffect(EffectType type, SamplingConfiguration config) {
            switch (type) {
                case DIFFUSIVE_GLASS -> camera.targetAreas.put(type, new DiffusiveTargetArea(config));
//                case DEPTH_OF_FIELD -> camera.featureTargetAreas.put(type, new DepthOfFieldTargetArea(config));
//                case ANTI_ALIASING -> camera.featureTargetAreas.put(type, new AntiAliasingTargetArea(config));
//                case SOFT_SHADOW -> camera.featureTargetAreas.put(type, new SoftShadowTargetArea(config));
//                case GLOSSY_REFLECTION -> camera.featureTargetAreas.put(type, new GlossyReflectionTargetArea(config));
            }
            return this;
        }

        /**
         * Sets the ray that would identify and paint the intersected pixels
         *
         * @param scene the scene of objects
         * @param type  the type of requested ray
         * @return the builder instance
         */
        public Builder setRayTracer(Scene scene, RayTracerType type) {
            switch (type) {
                case SIMPLE:
                    camera.rayTracer = new SimpleRayTracer(scene);
                    break;
                case GRID:
                    camera.rayTracer = null;
                    break;
                case EXTENDED:
                    camera.rayTracer = new ExtendedRayTracer(scene, camera.targetAreas);
                    break;
                default:
                    throw new IllegalArgumentException("The type: " + type + " is invalid for the ray tracer");
            }
            return this;
        }

        /**
         * Set multi-threading <br>
         * Parameter value meaning:
         * <ul>
         * <li>-2 - number of threads is number of logical processors less 2</li>
         * <li>-1 - stream processing parallelization (implicit multi-threading) is used</li>
         * <li>0 - multi-threading is not activated</li>
         * <li>1 and more - literally number of threads</li>
         * </ul>
         *
         * @param threads number of threads
         * @return builder object itself
         */
        public Builder setMultithreading(int threads) {
            if (threads < -3)
                throw new IllegalArgumentException("Multithreading parameter must be -2 or higher");
            if (threads == -2) {
                int cores = Runtime.getRuntime().availableProcessors() - SPARE_THREADS;
                camera.threadsCount = cores <= 2 ? 1 : cores;
            } else
                camera.threadsCount = threads;
            return this;
        }

        /**
         * Set debug printing interval. If it's zero - there won't be printing at all
         *
         * @param interval printing interval in %
         * @return builder object itself
         */
        public Builder setDebugPrint(double interval) {
            if (interval < 0) throw new IllegalArgumentException("interval parameter must be non-negative");
            camera.printInterval = interval;
            return this;
        }

        /**
         * Builds and validates the camera instance
         *
         * @return A new validated Camera instance
         * @throws MissingResourceException if required parameters are missing
         * @throws IllegalArgumentException if vectors are not properly orthogonal
         */
        public Camera build() {
            // Size values check
            if (alignZero(camera.width) <= 0)
                throw new MissingResourceException("Width must be positive non zero values", "Camera", "width");
            if (alignZero(camera.height) <= 0)
                throw new MissingResourceException("Height must be positive non zero values", "Camera", "height");
            if (alignZero(camera.distance) <= 0)
                throw new MissingResourceException("Distance must be positive a non zero value", "Camera", "distance");

            //Geometry values check
            if (camera.position == null)
                throw new MissingResourceException("Camera position must be included", "Camera", "position");
            camera.viewPlaneCenter = camera.position.add(camera.vTo.scale(camera.distance));

            if (camera.vTo == null)
                throw new MissingResourceException("Camera to vector must be included", "Camera", "vTo");
            if (camera.vUp == null)
                throw new MissingResourceException("Camera up vector must be included", "Camera", "vUp");
            if (camera.vRight == null)
                throw new MissingResourceException("Camera right vector must be included", "Camera", "vRight");

            // Check if the vectors are orthogonal
            if (!isZero(camera.vTo.dotProduct(camera.vUp)))
                throw new IllegalArgumentException("Error: Provided to & up vectors are not orthogonal");
            if (!isZero(camera.vTo.dotProduct(camera.vRight)))
                throw new IllegalArgumentException("Error: Provided to & right vectors are not orthogonal");

            if (camera.nX <= 0)
                throw new IllegalArgumentException("The resolution (nX) should have a positive non zero value");
            if (camera.nY <= 0)
                throw new IllegalArgumentException("The resolution (nY) should have a positive non zero value");
            camera.imageWriter = new ImageWriter(camera.nX, camera.nY);
            camera.pixelWidth = camera.width / camera.nX;
            camera.pixelHeight = camera.height / camera.nY;

            if (camera.rayTracer == null) camera.rayTracer = new SimpleRayTracer(null);

            return camera.clone();
        }
    }

    // ========================== Multi-threading parameters ==========================
    /**
     * Amount of threads to use fore rendering image by the camera
     */
    private int threadsCount = -1;
    /**
     * Amount of threads to spare for Java VM threads:<br>
     * Spare threads if trying to use all the cores
     */
    private static final int SPARE_THREADS = 2;
    /**
     * Debug print interval in seconds (for progress percentage)<br>
     * if it is zero - there is no progress output
     */
    private double printInterval = 0;
    /**
     * Pixel manager for supporting:
     * <ul>
     * <li>multi-threading</li>
     * <li>debug print of progress percentage in Console window/tab</li>
     * </ul>
     */
    private PixelManager pixelManager;
    // =================================================================================

    /**
     * The position of the camera in 3D space
     */
    private Point position;
    /**
     * The right vector of the camera - perpendicular to vTo and vUp
     */
    private Vector vRight;
    /**
     * The up vector of the camera
     */
    private Vector vUp;
    /**
     * The direction vector the camera is pointing at
     */
    private Vector vTo;
    /**
     * Distance from the camera to the view plane
     */
    private double distance = 0.0;
    /**
     * Width of the view plane
     */
    private double width = 0.0;
    /**
     * Height of the view plane
     */
    private double height = 0.0;

    /**
     * The center point of the view plane.
     */
    private Point viewPlaneCenter;

    /**
     * The width of a single pixel in the view plane.
     */
    private double pixelWidth;

    /**
     * The height of a single pixel in the view plane.
     */
    private double pixelHeight;

    /**
     * The image writer instance for rendering the image
     */
    private ImageWriter imageWriter;

    /**
     * The ray tracer instance for calculating the rays
     */
    private RayTracerBase rayTracer = null;

    private final Map<EffectType, TargetAreaBase> targetAreas = new HashMap<>();

    /**
     * The number of pixels in the view plane (Rows)
     */
    private int nX = 1;

    /**
     * The number of pixels in the view plane (columns)
     */
    private int nY = 1;

    /**
     * Private constructor for builder pattern
     */
    private Camera() {
    }

    /**
     * Gets a new builder instance for creating cameras
     *
     * @return A new Camera.Builder instance
     */
    public static Builder getBuilder() {
        return new Builder();
    }

    /**
     * Constructs a ray through a specific pixel in the view plane
     *
     * @param nX Column number of pixels in the view plane (Column Width)
     * @param nY Row number of pixels in the view plane (Row Height)
     * @param j  Pixel column index
     * @param i  Pixel row index
     * @return The ray through pixel (j,i)
     */
    public Ray constructRay(int nX, int nY, int j, int i) {
        double yI = -(i - (nY - 1) / 2.0) * pixelHeight;
        double xJ = (j - (nX - 1) / 2.0) * pixelWidth;

        Point pIJ = viewPlaneCenter;
        //check if xJ or yI are not zero, so we will not add zero vector
        if (!isZero(xJ)) pIJ = pIJ.add(vRight.scale(xJ));
        if (!isZero(yI)) pIJ = pIJ.add(vUp.scale(yI));

        return new Ray(position, pIJ.subtract(position).normalize());
    }

    /**
     * This function renders image's pixel color map from the scene
     * included in the ray tracer object
     *
     * @return the camera object itself
     */
    public Camera renderImage() {
        pixelManager = new PixelManager(nY, nX, printInterval);
        return switch (threadsCount) {
            case 0 -> renderImageNoThreads();
            case -1 -> renderImageStream();
            default -> renderImageRawThreads();
        };
    }

    /**
     * Provides a layout of borderlines with the provided color on the image.
     *
     * @param interval    the size of each square on the view place that should be sounded with the borderlines
     * @param borderColor the color of the borderline
     * @return the caller (camera)
     */
    public Camera printGrid(int interval, Color borderColor) {
        for (int i = 0; i < nX; i++) {
            for (int j = 0; j < nY; j++) {
                if (i % interval == 0 || j % interval == 0)
                    imageWriter.writePixel(j, i, borderColor);
            }
        }
        return this;
    }

    /**
     * Sends an instructions to the imageWriter to apply the data on a given named image
     *
     * @param imageName the new image's name
     * @return the caller (camera)
     */
    public Camera writeToImage(String imageName) {
        imageWriter.writeToImage(imageName);
        return this;
    }

    /**
     * Sends the ray and gets the intersection data (the point)
     *
     * @param j the row that the ray should be sent on the ViewPlane
     * @param i the column that the ray should be sent on the ViewPlane
     */
    private void castRay(int j, int i) {
        Ray ray = constructRay(nX, nY, j, i);
        Color color = rayTracer.traceRay(ray);
        imageWriter.writePixel(j, i, color);
        pixelManager.pixelDone();
    }

    /**
     * Render image using multi-threading by parallel streaming
     *
     * @return the camera object itself
     */
    private Camera renderImageStream() {
        IntStream.range(0, nY).parallel()
                .forEach(i -> IntStream.range(0, nX).parallel()
                        .forEach(j -> castRay(j, i)));
        return this;
    }

    /**
     * Render image without multi-threading
     *
     * @return the camera object itself
     */
    private Camera renderImageNoThreads() {
        for (int i = 0; i < nY; ++i)
            for (int j = 0; j < nX; ++j)
                castRay(j, i);
        return this;
    }

    /**
     * Render image using multi-threading by creating and running raw threads
     *
     * @return the camera object itself
     */
    private Camera renderImageRawThreads() {
        var threads = new LinkedList<Thread>();
        while (threadsCount-- > 0)
            threads.add(new Thread(() -> {
                PixelManager.Pixel pixel;
                while ((pixel = pixelManager.nextPixel()) != null)
                    castRay(pixel.col(), pixel.row());
            }));
        for (var thread : threads) thread.start();
        try {
            for (var thread : threads) thread.join();
        } catch (InterruptedException ignored) {
        }
        return this;
    }
}
