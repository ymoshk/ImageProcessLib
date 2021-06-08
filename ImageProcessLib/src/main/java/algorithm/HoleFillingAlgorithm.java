package algorithm;

import image.Pixel;
import org.opencv.core.Mat;
import utils.PixelConnectivity;
import weighting.function.WeightingFunction;

import java.util.HashSet;
import java.util.Set;

public class HoleFillingAlgorithm implements ImageProcessAlgorithm {

    private final PixelConnectivity pixelConnectivity;
    private final WeightingFunction weightingFunction;
    private Set<Pixel> boundary;
    private Set<Pixel> holeCoords;
    private Mat image;

    HoleFillingAlgorithm(WeightingFunction weightingFunction,
                         PixelConnectivity pixelConnectivity) {
        this.holeCoords = new HashSet<>();
        this.boundary = new HashSet<>();
        this.weightingFunction = weightingFunction;
        this.pixelConnectivity = pixelConnectivity;
    }

    /**
     * Fill the hole pixels set and the boundary pixels set.
     * Then, foreach pixel that's a part of the hole, calculate it's new value and set the new value to the image itself.
     */
    private void fillHole() {
        this.holeCoords = CommonOp.findHolePixels(this.image);
        this.boundary = CommonOp.findBoundaryPixels(this.image, this.holeCoords, this.pixelConnectivity);
        this.holeCoords.forEach(holeCoord -> this.image.put(holeCoord.getY(), holeCoord.getX(), calcPixelNewVal(holeCoord)));
    }

    /**
     * @param pixel a pixel that is a part of the hole.
     * @return the new value of the pixel according to the algorithm calculation.
     */
    private float calcPixelNewVal(Pixel pixel) {
        float numerator = 0, denominator = 0;

        for (Pixel boundaryPixel : this.boundary) {
            float weightingFunctionResult = this.weightingFunction.calculate(pixel, boundaryPixel);
            numerator += weightingFunctionResult * this.image.get(boundaryPixel.getY(), boundaryPixel.getX())[0];
            denominator += weightingFunctionResult;
        }

        return numerator / denominator;
    }

    @Override
    public Mat invoke(Mat image) {
        this.image = image;
        fillHole();
        return image;
    }
}
