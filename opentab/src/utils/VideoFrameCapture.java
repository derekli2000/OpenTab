package utils;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

/**
 * @author Derek Li, Rithik Mamidi, Samarth Patel
 * @version 1.0 <br>
 *
 *          Starts a video stream, tries to capture a new frame whenever
 *          available, and allows for non-blocking access of the current camera
 *          frame
 */
public class VideoFrameCapture {
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    private VideoCapture cap = new VideoCapture();
    private Thread videoCapThread = new Thread(new VideoGrabber());
    private Mat currFrame;

    public VideoFrameCapture() {
        cap.open(0);
        videoCapThread.start();
        currFrame = new Mat();
        cap.read(currFrame);
    }

    public void close() {
        cap.release();
        videoCapThread.stop();
    }

    public Mat getCameraFrame() {
        return currFrame;
    }

    class VideoGrabber implements Runnable {
        @Override
        public void run() {
            while (true) {
                Mat mat = new Mat();
                cap.read(mat);
                currFrame = mat;
            }
        }
    }
}