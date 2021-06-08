package utils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class argsParser {
    private final PixelConnectivity connectivity;
    private int zFactor;
    private float epsilon;
    private String maskPath;
    private String imagePath;
    private String outputPath;

    public argsParser(String[] args) throws IllegalArgumentException {
        List<String> argsList = Arrays.stream(args)
                .map(String::trim)
                .collect(Collectors.toList());

        if (argsList.get(0).equals("-4")) {
            this.connectivity = PixelConnectivity.FourConnected;
        } else if (argsList.get(0).equals("-8")) {
            this.connectivity = PixelConnectivity.EightConnected;
        } else {
            throw new IllegalArgumentException("Pixel connectivity argument is invalid.");
        }

        argsList.remove(0);

        while (argsList.size() > 1) {
            setValues(argsList.get(0), argsList.get(1));
            argsList.remove(0);
            argsList.remove(0);
        }

        validate();
    }

    private void validate() throws IllegalArgumentException {
        boolean check = this.zFactor > 0 &&
                this.epsilon > 0 &&
                this.imagePath != null &&
                this.maskPath != null &&
                this.outputPath != null;

        if (!check) {
            throw new IllegalArgumentException("Error, Invalid arguments received.");
        }
    }

    private void setValues(String option, String value) throws IllegalArgumentException {
        switch (option) {
            case "-z":
                this.zFactor = Integer.parseInt(value);
                break;
            case "-e":
                this.epsilon = Float.parseFloat(value);
                break;
            case "-m":
                this.maskPath = value;
                break;
            case "-o":
                this.outputPath = value;
                break;
            case "-i":
                this.imagePath = value;
                break;
            default:
                throw new IllegalArgumentException("Invalid argument received " + option + " : " + value);
        }
    }

    public PixelConnectivity getConnectivity() {
        return this.connectivity;
    }

    public int getZFactor() {
        return this.zFactor;
    }

    public float getEpsilon() {
        return this.epsilon;
    }

    public String getMaskPath() {
        return this.maskPath;
    }

    public String getImagePath() {
        return this.imagePath;
    }

    public String getOutputPath() {
        return this.outputPath;
    }
}
