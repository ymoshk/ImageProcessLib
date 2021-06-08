package algorithm;

import utils.PixelConnectivity;
import weighting.function.WeightingFunction;

public class AlgorithmFactory {

    public static ImageProcessAlgorithm createHoleFillingAlgorithm(
            WeightingFunction weightingFunction,
            PixelConnectivity pixelConnectivity) {

        return new HoleFillingAlgorithm(weightingFunction, pixelConnectivity);
    }

    public static ImageProcessAlgorithm createQuickHoleFillingAlgorithm(
            WeightingFunction weightingFunction,
            PixelConnectivity pixelConnectivity,
            int boundarySize) {

        return new QuickHoleFillingAlgorithm(weightingFunction, pixelConnectivity, boundarySize);
    }
}