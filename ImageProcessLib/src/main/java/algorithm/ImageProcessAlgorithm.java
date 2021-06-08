package algorithm;

import org.opencv.core.Mat;

public interface ImageProcessAlgorithm {
    Mat invoke(Mat image);
}
