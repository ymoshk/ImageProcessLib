package algorithm;

import image.Pixel;
import org.opencv.core.Mat;
import utils.ConnectedPixels;
import utils.PixelConnectivity;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.stream.Collectors;

public class CommonOp {

    /**
     * For each pixel that is a part of the hole, find it's neighbors pixels that aren't
     * a part of the hole itself according to the given pixelConnectivity method and add them to a boundary pixels set.
     */
    public static Set<Pixel> findBoundaryPixels(Mat image, Set<Pixel> holePixels,
                                                PixelConnectivity pixelConnectivity) {
        Set<Pixel> boundarySet = new HashSet<>();

        holePixels.forEach(holePixel -> {
            List<Pixel> neighbors = ConnectedPixels.getPixelNeighbors(holePixel, pixelConnectivity);
            boundarySet.addAll(neighbors.stream()
                    .filter(neighbor -> !isHole(image, neighbor))
                    .collect(Collectors.toList()));
        });

        return boundarySet;
    }

    /**
     * @param image Mat object that contains an image to find a hole in it.
     * @return A set of pixels that represents a hole in the image.
     */
    public static Set<Pixel> findHolePixels(Mat image) {
        Set<Pixel> result = new HashSet<>();
        Pixel begin = findHoleRepresentative(image);

        if (begin != null) {
            Stack<Pixel> stack = new Stack<>();
            stack.add(begin);

            while (!stack.isEmpty()) {
                Pixel pixel = stack.pop();
                result.add(pixel);
                ConnectedPixels.getPixelNeighbors(pixel, PixelConnectivity.EightConnected).stream()
                        .filter(neighbor -> !result.contains(neighbor))
                        .filter(neighbor -> isHole(image, neighbor))
                        .forEach(stack::push);
            }
        }
        return result;
    }

    /**
     * @return A pixel which is the first hole pixel that was found in a linear scan.
     */
    private static Pixel findHoleRepresentative(Mat image) {
        for (int y = 0; y < image.height(); y++) {
            for (int x = 0; x < image.width(); x++) {
                if (isHole(image, x, y)) {
                    return new Pixel(x, y);
                }
            }
        }
        return null;
    }

    public static boolean isHole(Mat image, Pixel pixel) {
        return isHole(image, pixel.getX(), pixel.getY());
    }

    private static boolean isHole(Mat image, int x, int y) {
        return image.get(y, x)[0] == -1;
    }
}
