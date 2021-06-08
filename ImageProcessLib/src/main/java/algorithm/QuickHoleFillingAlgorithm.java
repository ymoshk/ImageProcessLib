package algorithm;

import image.Pixel;
import org.opencv.core.Mat;
import utils.ConnectedPixels;
import utils.PixelConnectivity;
import weighting.function.WeightingFunction;

import java.util.*;
import java.util.stream.Collectors;

public class QuickHoleFillingAlgorithm implements ImageProcessAlgorithm {
    private static int setIndex = 0;
    private final WeightingFunction weightingFunction;
    private final PixelConnectivity pixelConnectivity;
    private final List<Set<Pixel>> boundarySectionsList;
    private int sectionsCount;
    private Set<Pixel> boundary;
    private Set<Pixel> holePixelsSet;
    private Mat image;

    public QuickHoleFillingAlgorithm(WeightingFunction weightingFunction,
                                     PixelConnectivity pixelConnectivity,
                                     int boundarySize) {
        this.weightingFunction = weightingFunction;
        this.pixelConnectivity = pixelConnectivity;
        this.holePixelsSet = new HashSet<>();
        this.boundarySectionsList = new ArrayList<>();
        this.sectionsCount = boundarySize;
        this.boundary = new HashSet<>();
        initSetsList();
    }

    /**
     * Creates sectionsCount number of hash sets and add them into the boundary sections list.
     */
    private void initSetsList() {
        for (int i = 0; i < this.sectionsCount; i++) {
            this.boundarySectionsList.add(new HashSet<>());
        }
    }

    /**
     * @return A list which contains the boundary pixels ordered in flood fill order.
     */
    private List<Pixel> floodFillOverBoundary() {
        List<Pixel> orderedListOfBoundary = new LinkedList<>();
        Set<Pixel> handled = new HashSet<>();
        Stack<Pixel> stack = new Stack<>();
        stack.add(this.boundary.iterator().next());

        while (!stack.isEmpty()) {
            Pixel current = stack.pop();
            if (!handled.contains(current)) {
                orderedListOfBoundary.add(current);
                handled.add(current);
                List<Pixel> neighbors =
                        ConnectedPixels.getPixelNeighbors(current, PixelConnectivity.FourConnected).stream()
                                .filter(neighbor -> !handled.contains(neighbor))
                                .filter(this.boundary::contains)
                                .collect(Collectors.toList());

                neighbors.forEach(stack::push);
            }
        }

        return orderedListOfBoundary;
    }

    /**
     * @return the index of the current set that need to be filled.
     */
    private int getSetIndex() {
        int maxPerSection = this.boundary.size() / this.sectionsCount;

        if (this.boundarySectionsList.get(setIndex).size() >= maxPerSection) {
            setIndex = (setIndex + 1) % this.sectionsCount;
        }

        return setIndex;
    }

    /**
     * @param pixels list of ordered pixels.
     *               The method will split the pixels into sections where each section is in a different set.
     */
    private void splitBoundaryToSections(List<Pixel> pixels) {
        pixels.forEach(pixel -> this.boundarySectionsList.get(getSetIndex()).add(pixel));
    }

    /**
     * - Clear the current boundary set.
     * - For each section set, calculate it's pixels values average.
     * - Take the first pixel of the set and change it's value to the average value calculated above.
     * - Add that pixel into the empty boundary.
     */
    private void createSmallerBoundarySet() {
        this.boundary.clear();
        for (Set<Pixel> group : this.boundarySectionsList) {
            if (!group.isEmpty()) {
                Pixel rep = (Pixel) group.toArray()[0];
                rep.setValue((float) group.stream()
                        .mapToDouble(pixel -> this.image.get(pixel.getY(), pixel.getX())[0])
                        .average()
                        .getAsDouble());
                this.boundary.add(rep);
            }
        }
    }

    /**
     * @param pixel a pixel that is a part of the hole.
     * @return the new value of the pixel according to the algorithm calculation.
     */
    private float calcPixelNewVal(Pixel pixel) {
        float numerator = 0, denominator = 0;

        for (Pixel boundaryPixel : this.boundary) {
            float weightingFunctionResult = this.weightingFunction.calculate(pixel, boundaryPixel);
            numerator += weightingFunctionResult * boundaryPixel.getValue();
            denominator += weightingFunctionResult;
        }

        return numerator / denominator;
    }

    @Override
    public Mat invoke(Mat image) {
        this.image = image;
        this.holePixelsSet = CommonOp.findHolePixels(this.image);
        this.boundary = CommonOp.findBoundaryPixels(this.image, this.holePixelsSet, this.pixelConnectivity);
        this.sectionsCount = Math.min(this.sectionsCount, this.boundary.size());
        List<Pixel> orderedBoundary = floodFillOverBoundary();
        splitBoundaryToSections(orderedBoundary);
        createSmallerBoundarySet();
        this.holePixelsSet.forEach(holePixel ->
                this.image.put(holePixel.getY(), holePixel.getX(), calcPixelNewVal(holePixel)));

        return this.image;
    }
}
