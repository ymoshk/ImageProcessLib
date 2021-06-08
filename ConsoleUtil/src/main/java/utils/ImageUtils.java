package utils;

import image.ImageWrapper;
import image.Pixel;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import java.util.ArrayList;
import java.util.List;

public class ImageUtils {

    static {
        //Load OpenCV library
        nu.pattern.OpenCV.loadShared();
    }

    /**
     * @param imagePath path to an RGB image.
     * @param maskPath  path to an RGB image that contains a mask.
     * @return Mat object which contains a normalized grayscale image of the fusion of the image and the mask.
     * @throws Exception will be thrown in any case the image or the mask couldn't be loaded.
     */
    public static Mat mergeImageWithMask(String imagePath, String maskPath) throws Exception {
        Mat image = Imgcodecs.imread(imagePath, Imgcodecs.IMREAD_COLOR);
        Mat mask = Imgcodecs.imread(maskPath, Imgcodecs.IMREAD_COLOR);

        if (!image.empty() && !mask.empty()) {
            Mat normImage = convertToNormalizedGrayscale(image);
            Mat normMask = convertToNormalizedGrayscale(mask);
            merge(normImage, normMask);

            return normImage;
        } else {
            if (image.empty()) {
                throw new Exception("Image couldn't be loaded");
            } else {
                throw new Exception("Mask couldn't be loaded");
            }
        }
    }

    /**
     * @param path  the location where the image should be saved into.
     * @param image ImageWrapper object that contains the image to save.
     */
    public static void saveImage(String path, ImageWrapper image) {
        Imgcodecs.imwrite(path, ImageUtils.denormalizeImage(image.getImage()));
    }

    /**
     * @param image Mat object which contains an RGB image.
     * @return a grayscale normalized version of the given image.
     * normalized - each pixel is a scalar in the range [0,1]
     */
    private static Mat convertToNormalizedGrayscale(Mat image) {
        int height = image.height();
        int width = image.width();
        float red, green, blue, newVal;
        double[] rgb;

        Mat grayImageMatrix = new Mat(height, width, CvType.CV_32FC1);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                rgb = image.get(y, x);

                // The Weighted Method - result appears to be less accurate.
                // red = (float) (rgb[0] * 0.299) / 255f;
                // green = (float) (rgb[1] * 0.587) / 255f;
                // blue = (float) (rgb[2] * 0.114) / 255f;
                // newVal = (red + green + blue);

                // Average Method
                red = (float) rgb[0];
                green = (float) rgb[1];
                blue = (float) rgb[2];
                newVal = (red + green + blue) / (3 * 255f);

                grayImageMatrix.put(y, x, newVal);
            }
        }

        return grayImageMatrix;
    }

    /**
     * @param mask Mat object that contains a grayscale normalized image.
     * @return a list of pixels that each one is a part of a hole in the image.
     */
    private static List<Pixel> extractHoleCoords(Mat mask) {
        List<Pixel> holesCoordsList = new ArrayList<>();

        for (int y = 0; y < mask.height(); y++) {
            for (int x = 0; x < mask.width(); x++) {
                double[] pixel = mask.get(y, x);
                if (pixel[0] < 0.5) {
                    holesCoordsList.add(new Pixel(x, y));
                }
            }
        }

        return holesCoordsList;
    }

    /**
     * @param image Mat object that contains a image to merge with a mask.
     * @param mask  Mat object that contains a mask that represent a hole. <br>
     *              The function merge the image with the mask - basically it creates a hole in the image.
     */
    private static void merge(Mat image, Mat mask) {
        List<Pixel> holeCoordsList = extractHoleCoords(mask);
        holeCoordsList.forEach(pixel -> image.put(pixel.getY(), pixel.getX(), -1f));
    }

    /**
     * @param image Mat object that contains a grayscale normalized image.
     * @return A Mat object that contains the grayscale image where each pixel is in the range [0, 255].
     */
    public static Mat denormalizeImage(Mat image) {
        int height = image.height();
        int width = image.width();
        Mat denormalizedImage = new Mat(height, width, CvType.CV_8UC1);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                double[] pixel = image.get(y, x);
                denormalizedImage.put(y, x, Math.round(pixel[0] * 255f));
            }
        }

        return denormalizedImage;
    }
}