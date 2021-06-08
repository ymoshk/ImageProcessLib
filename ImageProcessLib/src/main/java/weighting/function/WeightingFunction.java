package weighting.function;

import image.Pixel;

public interface WeightingFunction {
    /**
     * weighting function prototype which assigns a non-negative float weight to a pair of two
     * pixel coordinates in the image.
     *
     * @param firstPixelCoord  the first pixel from an image.
     * @param secondPixelCoord the second pixel from an image.
     * @return A float that represents the result of the invocation of the weighting function over the two pixels.
     */
    float calculate(Pixel firstPixelCoord, Pixel secondPixelCoord);
}
