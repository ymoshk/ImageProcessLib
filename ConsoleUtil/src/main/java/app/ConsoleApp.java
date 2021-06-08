package app;

import algorithm.AlgorithmFactory;
import algorithm.ImageProcessAlgorithm;
import image.ImageWrapper;
import utils.ImageUtils;
import utils.argsParser;
import weighting.function.WeightingFunctionFactory;

import java.util.Arrays;

public class ConsoleApp {

    public static void main(String[] args) {
        if (Arrays.asList(args).contains("--help")) {
            help();
        } else {
            try {
                argsParser parser = new argsParser(args);

                // Prepare the image
                ImageWrapper image = ImageWrapper.create(
                        ImageUtils.mergeImageWithMask(parser.getImagePath(), parser.getMaskPath()));

                // Prepare the algorithm
                ImageProcessAlgorithm holeFillingAlgorithm =
                        AlgorithmFactory.createHoleFillingAlgorithm(
                                WeightingFunctionFactory.CreateDefault(parser.getZFactor(), parser.getEpsilon()),
                                parser.getConnectivity());

                // Second question algorithm suggestion
                ImageProcessAlgorithm quickHoleFilling =
                        AlgorithmFactory.createQuickHoleFillingAlgorithm(
                                WeightingFunctionFactory.CreateDefault(parser.getZFactor(), parser.getEpsilon()),
                                parser.getConnectivity(), 150);


                image.applyAlgorithm(holeFillingAlgorithm);
                ImageUtils.saveImage(parser.getOutputPath(), image);

                System.out.println("Done");
            } catch (Exception e) {
                System.out.println(e.getMessage());
                System.out.println("type --help for instructions");
            }
        }
    }

    private static void help() {
        StringBuilder help = new StringBuilder();

        help.append("Usage: ConsoleApp [PIXELS_CONNECTIVITY] [OPTIONS] [FILES]\n");
        help.append("\n");
        help.append("Options:\n");
        help.append("\t-z, z factor value\n");
        help.append("\t-e, epsilon value\n");
        help.append("\n");
        help.append("FILES:\n");
        help.append("\t-i, input image path\n");
        help.append("\t-m, mask image path\n");
        help.append("\t-o, output image path\n");
        help.append("\n");
        help.append("Pixels connectivity:\n");
        help.append("\t-4 to apply 4-connected neighbors\n");
        help.append("\t-8 to apply 8-connected neighbors\n");

        System.out.println(help);
    }
}
