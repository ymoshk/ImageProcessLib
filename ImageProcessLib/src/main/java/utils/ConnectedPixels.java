package utils;

import image.Pixel;

import java.util.ArrayList;
import java.util.List;

public class ConnectedPixels {
    /**
     * @param pixel              A pixel to get it's neighbors.
     * @param connectivityMethod A flag (enum) that indicates how to decide whether a particular pixel
     *                           is considered a neighbor of the pixel or not.
     * @return List of pixels where each one is a neighbor of the given pixel.
     */
    public static List<Pixel> getPixelNeighbors(Pixel pixel, PixelConnectivity connectivityMethod) {
        switch (connectivityMethod) {
            case FourConnected:
                return fourConnectedNeighbors(pixel);
            case EightConnected:
                return eightConnectedNeighbors(pixel);
            default:
                return new ArrayList<>();
        }
    }

    private static List<Pixel> fourConnectedNeighbors(Pixel pixel) {
        List<Pixel> neighbors = new ArrayList<>(4);
        int x = pixel.getX();
        int y = pixel.getY();

        neighbors.add(new Pixel(x, y - 1));
        neighbors.add(new Pixel(x + 1, y));
        neighbors.add(new Pixel(x, y + 1));
        neighbors.add(new Pixel(x - 1, y));

        return neighbors;
    }

    private static List<Pixel> eightConnectedNeighbors(Pixel pixel) {
        List<Pixel> neighbors = new ArrayList<>(8);

        int x = pixel.getX();
        int y = pixel.getY();

        neighbors.add(new Pixel(x, y - 1));
        neighbors.add(new Pixel(x + 1, y - 1));
        neighbors.add(new Pixel(x + 1, y));
        neighbors.add(new Pixel(x + 1, y + 1));
        neighbors.add(new Pixel(x, y + 1));
        neighbors.add(new Pixel(x - 1, y + 1));
        neighbors.add(new Pixel(x - 1, y));
        neighbors.add(new Pixel(x - 1, y - 1));

        return neighbors;
    }
}