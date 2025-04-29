package renderer;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.MissingResourceException;

import static primitives.Util.isZero;

public class Camera implements Cloneable  {
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

    public static class Builder {
        private Camera camera = new Camera();

        public Builder setLocation(Point p) {
            camera.position = p;
            return this;
        }

        public Builder setDirection(Vector vTo,Vector vUp){
            if(!isZero(vUp.dotProduct(vTo))) throw new IllegalArgumentException("Error: Provided vectors are not orthogonal");

            camera.vUp = vUp.normalize();
            camera.vTo = vTo.normalize();
            camera.vRight = camera.vTo.crossProduct(camera.vUp).normalize();
            return this;
        }
        public Builder setDirection(Point p,Vector vUp){
            camera.vTo = p.subtract(camera.position).normalize();
            camera.vRight = camera.vTo.crossProduct(vUp).normalize();
            camera.vUp = camera.vRight.crossProduct(camera.vTo).normalize();
            return this;
        }
        public Builder setDirection(Point p) {
            camera.vTo = p.subtract(camera.position).normalize();
            camera.vUp = Vector.AXIS_Y;
            camera.vRight = camera.vTo.crossProduct(camera.vUp).normalize();
            camera.vUp = camera.vRight.crossProduct(camera.vTo).normalize();
            return this;
        }

        public Builder setSize(double width, double height) {
            if (width <= 0 || height <= 0) throw new IllegalArgumentException("Width and height must be positive");
            camera.width = width;
            camera.height = height;
            return this;
        }

        public Builder setDistance(double d) {
            if (d <= 0) throw new IllegalArgumentException("Distance must be positive");
            camera.distance = d;
            return this;
        }

        public Builder setResolution(double nX,double nY){
            //TODO: The implementation of this method is holt for now
            return null;
        }

        public Camera build() {
            // Size values check
            if(camera.width == 0) throw new MissingResourceException("Width must be positive non zero values", "Camera", "width");
            if(camera.height == 0) throw new MissingResourceException("Height must be positive non zero values", "Camera", "height");
            if(camera.distance == 0) throw new MissingResourceException("Distance must be positive a non zero value", "Camera", "distance");

            //Geometry values check
            if (camera.position == null) throw new MissingResourceException("Camera position must be included", "Camera", "position");
            if (camera.vTo == null) throw new MissingResourceException("Camera to vector must be included", "Camera", "vTo");
            if (camera.vUp == null) throw new MissingResourceException("Camera up vector must be included", "Camera", "vUp");
            if (camera.vRight == null) throw new MissingResourceException("Camera right vector must be included", "Camera", "vRight");

            // Check if the vectors are orthogonal
            if (!isZero(camera.vTo.dotProduct(camera.vUp))) throw new IllegalArgumentException("Error: Provided to & up vectors are not orthogonal");
            if (!isZero(camera.vTo.dotProduct(camera.vRight))) throw new IllegalArgumentException("Error: Provided to & right vectors are not orthogonal");


            return camera.clone();
        }
    }


    private Point position;
    private Vector vRight;
    private Vector vUp;
    private Vector vTo;

    private double distance = 0.0;
    private double width = 0.0;
    private double height = 0.0;

    private Camera(){}

    public static Builder getBuilder(){
        return new Builder();
    }

    /*
        * Constructs a ray through pixel (j,i) in the view plane
        * @param nX - Column number of pixels in the view plane (Column Width)
        * @param nY - Row number of pixels in the view plane (Row Height)
        * @param j - pixel column index
        * @param i - pixel row index
        * @return the ray through pixel (j,i)
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

}
