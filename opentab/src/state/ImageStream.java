package state;

import org.opencv.core.Mat;
import java.awt.image.BufferedImage;
import utils.ImageUtils;
import java.util.Observable;

/**
 * @author Derek Li, Rithik Mamidi, Samarth Patel
 * @version 1.0 <br>
 *
 *          A class used to store an image to display and notifies observers
 *          when the image changes. <br>
 */
public class ImageStream extends Observable {
    private static ImageStream instance = null;
    private Mat frame;

    public static ImageStream getInstance() {
        if (instance == null)
            instance = new ImageStream();
        return instance;
    }

    public void setFrame(Mat frame) {
        if (frame == null || frame.empty())
            return;

        this.frame = new Mat();
        frame.copyTo(this.frame);
        setChanged();
        notifyObservers();
    }

    public BufferedImage getFrame() {
        if (frame == null || frame.empty())
            return new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);

        return ImageUtils.mat2img(frame);
    }
}