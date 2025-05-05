package renderer;

import primitives.Color;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;
import scene.Scene;

import java.util.MissingResourceException;

import static primitives.Util.isZero;

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
        Camera clone = new Camera();
        clone.vRight = this.vRight;
        clone.vUp = this.vUp;
        clone.vTo = this.vTo;
        clone.position = this.position;
        clone.distance = this.distance;
        clone.width = this.width;
        clone.height = this.height;

        return clone;
    }

    /**
     * Builder class for constructing Camera objects using the builder pattern
     */
    public static class Builder {

        /**
         * Default constructor for the builder to make the JavaDoc error to be gone
         */
        public Builder() {
        }

        /**
         * The camera instance being built
         */
        private Camera camera = new Camera();

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
         * NOT IMPLEMENTED YET
         *
         * @param _nX The number of columns in the view plane
         * @param _nY The number of rows in the view plane
         * @return The builder instance for method chaining
         * @throws IllegalArgumentException if nX or nY are not positive
         */
        public Builder setResolution(int _nX, int _nY) {
            camera.nX = _nX;
            camera.nY = _nY;
            return this;
        }

        public Builder setRayTracer(Scene scene, RayTracerType type) {
            switch (type) {
                case SIMPLE:
                    camera.rayTracer = new SimpleRayTracer(scene);
                    break;
                case GRID:
                    //TODO: Implement the grid ray tracer
                    camera.rayTracer = null;
                    break;
                default:
                    throw new IllegalArgumentException("The type: " + type.toString() +" is invalid for the ray tracer");
            }
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
            if (camera.width == 0) throw new MissingResourceException("Width must be positive non zero values", "Camera", "width");
            if (camera.height == 0) throw new MissingResourceException("Height must be positive non zero values", "Camera", "height");
            if (camera.distance == 0) throw new MissingResourceException("Distance must be positive a non zero value", "Camera", "distance");

            //Geometry values check
            if (camera.position == null) throw new MissingResourceException("Camera position must be included", "Camera", "position");
            if (camera.vTo == null) throw new MissingResourceException("Camera to vector must be included", "Camera", "vTo");
            if (camera.vUp == null) throw new MissingResourceException("Camera up vector must be included", "Camera", "vUp");
            if (camera.vRight == null) throw new MissingResourceException("Camera right vector must be included", "Camera", "vRight");

            // Check if the vectors are orthogonal
            if (!isZero(camera.vTo.dotProduct(camera.vUp)))
                throw new IllegalArgumentException("Error: Provided to & up vectors are not orthogonal");
            if (!isZero(camera.vTo.dotProduct(camera.vRight)))
                throw new IllegalArgumentException("Error: Provided to & right vectors are not orthogonal");

            if (camera.nX <= 0) throw new IllegalArgumentException("The resolution (nX) should have a positive non zero value");
            if (camera.nY <= 0) throw new IllegalArgumentException("The resolution (nY) should have a positive non zero value");

            camera.imageWriter = new ImageWriter(camera.nX,camera.nY);
            camera.rayTracer = camera.rayTracer == null ? new SimpleRayTracer(null) : camera.rayTracer;

            return camera.clone();
        }
    }

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

    private ImageWriter imageWriter;

    private RayTracerBase rayTracer = null;

    private int nX = 1;
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
        Point pIJ = position.add(vTo.scale(distance));

        double yI = -(i - (nY - 1) / 2.0) * height / nY;
        double xJ = (j - (nX - 1) / 2.0) * width / nX;

        //check if xJ or yI are not zero, so we will not add zero vector
        if (!isZero(xJ)) pIJ = pIJ.add(vRight.scale(xJ));
        if (!isZero(yI)) pIJ = pIJ.add(vUp.scale(yI));

        return new Ray(position, pIJ.subtract(position).normalize());
    }


    public Camera renderImage(){
        //Goes over the rows
        for (int i = 0; i < nY; i++) {
            //Goes over the columns
            for (int j = 0; j < nX; j++) {
                castRay(j,i);
            }
        }
        return this;
    }

    //TODO: The printGrid method is not implemented yet
    public Camera printGrid(int interval, Color borderColor){
        ImageWriter imageWriter = new ImageWriter(800, 500);
        for (int i = 0; i < 500; i++) {
            for (int j = 0; j < 800; j++) {
                if(i % interval == 0 || j % interval == 0 )
                    imageWriter.writePixel(j, i, borderColor);
            }
        }
        imageWriter.writeToImage("firstImage");
        return this;
    }

    public Camera writeToImage(String imageName){
        imageWriter.writeToImage(imageName);
        return this;
    }

    private void castRay(int j, int i){
        Ray ray = constructRay(nX, nY, j, i);
        Color color = rayTracer.traceRay(ray);
        imageWriter.writePixel(j,i,color);
    }
}
