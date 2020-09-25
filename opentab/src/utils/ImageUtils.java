package utils;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Core;
import org.opencv.videoio.VideoCapture;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

/**
 * @author Derek Li, Rithik Mamidi, Samarth Patel
 * @version 1.0 <br>
 *
 *          A class used to perform simply BufferedImage and Mat operations
 */
public class ImageUtils {
    private static BufferedImage allocateImage(Mat mat) {
        int type = 0;

        switch (mat.channels()) {
            case 1:
                type = BufferedImage.TYPE_BYTE_GRAY;
                break;
            case 3:
                type = BufferedImage.TYPE_3BYTE_BGR;
                break;
            default:
                throw new Error("could not convert image");
        }

        return new BufferedImage(mat.cols(), mat.rows(), type);
    }

    private static Mat allocateMat(BufferedImage image) {
        int type = 0;

        switch (image.getType()) {
            case BufferedImage.TYPE_3BYTE_BGR:
                type = CvType.CV_8UC3;
                break;
            case BufferedImage.TYPE_BYTE_GRAY:
                type = CvType.CV_8UC1;
                break;
            default:
                throw new Error("could not convert image");
        }

        return new Mat(image.getHeight(), image.getWidth(), type);
    }

    public static BufferedImage mat2img(Mat mat) {
        BufferedImage image = allocateImage(mat);
        byte[] data = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        mat.get(0, 0, data);

        return image;
    }

    public static Mat img2mat(BufferedImage image) {
        Mat mat = allocateMat(image);
        byte[] data = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        mat.put(0, 0, data);

        return mat;
    }
}