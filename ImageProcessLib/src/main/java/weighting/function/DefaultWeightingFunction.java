package weighting.function;

import image.Pixel;

public class DefaultWeightingFunction implements WeightingFunction {
    private final float epsilon;
    private final int zFactor;

    DefaultWeightingFunction(int zFactor, float epsilon) {
        this.epsilon = epsilon;
        this.zFactor = zFactor;
    }

    @Override
    public float calculate(Pixel firstPixelCoord, Pixel secondPixelCoord) {
        double distanceElOne = Math.pow(secondPixelCoord.getX() - firstPixelCoord.getX(), 2);
        double distanceElTwo = Math.pow(secondPixelCoord.getY() - firstPixelCoord.getY(), 2);
        double distance = Math.sqrt(distanceElOne + distanceElTwo);

        return (float) (1 / (Math.pow(distance, this.zFactor) + this.epsilon));
    }
}
