package weighting.function;

public class WeightingFunctionFactory {

    public static WeightingFunction CreateDefault(int zFactor, float epsilon) {
        return new DefaultWeightingFunction(zFactor, epsilon);
    }
}
