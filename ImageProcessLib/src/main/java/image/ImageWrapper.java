package image;

import algorithm.ImageProcessAlgorithm;
import org.opencv.core.Mat;

public class ImageWrapper {
    private Mat image;

    private ImageWrapper(Mat image) {
        this.image = image;
    }

    public static ImageWrapper create(Mat image) {
        return new ImageWrapper(image);
    }

    public Mat getImage() {
        return image;
    }

    public void setImage(Mat image) {
        this.image = image;
    }

    /**
     * @param algorithm an implementation of the interface ImageProcessAlgorithm
     *                  to apply on the image in this wrapper.
     */
    public void applyAlgorithm(ImageProcessAlgorithm algorithm) {
        this.image = algorithm.invoke(this.image);
    }
}