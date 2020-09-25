package image_processing;

import state.TabletConfig;
import state.PenConfig;
import state.PreprocessingConfig;
import state.KeybindingsConfig;
import state.ModeConfig;
import state.ImageStream;
import state.ImageStreams;

import ui.components.ButtonPanel;
import utils.VideoFrameCapture;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Observer;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Scalar;
import org.opencv.core.Point;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;
import org.opencv.highgui.HighGui;
import org.opencv.utils.Converters;
import org.opencv.core.Size;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.Container;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Observable;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.*;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.event.DocumentEvent.ElementChange;
import javax.xml.namespace.QName;

/**
 * @author Derek Li, Rithik Mamidi, Samarth Patel
 * @version 1.0 <br>
 *
 *          A class used to perform real time Processing via threads. <br>
 */
@SuppressWarnings("deprecation")
public class Processing implements Observer {
    //
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }
    private VideoFrameCapture videoCap = new VideoFrameCapture();
    private Robot robot;

    private static final TabletConfig tabletConfig = TabletConfig.getInstance();
    private static final PenConfig penConfig = PenConfig.getInstance();
    private static final PreprocessingConfig preConfig = PreprocessingConfig.getInstance();
    private static final KeybindingsConfig keyConfig = KeybindingsConfig.getInstance();
    private static final ModeConfig modeConfig = ModeConfig.getInstance();
    private static final ImageStream[] streams = ImageStreams.getInstance().getAllStreams();

    private final Thread tabletTrackingThread = new Thread(new TabletTracker());
    private AtomicBoolean isTabletTrackingMode = new AtomicBoolean(true);
    private AtomicBoolean isMouseControlLocked = new AtomicBoolean(false);
    // private AtomicBoolean isDrawing = new AtomicBoolean(false);

    private static final int IFW = JComponent.WHEN_IN_FOCUSED_WINDOW;

    private Point[] tabletLocation = null;
    JFrame component;

    Action pressedPrimaryAction = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            boolean wasSet = isTabletTrackingMode.compareAndSet(false, true);
            if (!wasSet) {
                modeConfig.setTablet(false);
                isTabletTrackingMode.compareAndSet(true, false);
            } else {
                modeConfig.setTablet(true);
            }
        }
    };

    Action pressedSecondaryAction = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            boolean wasSet = isMouseControlLocked.compareAndSet(false, true);
            if (!wasSet) {
                modeConfig.setMouseLocked(false);
                isMouseControlLocked.compareAndSet(true, false);
            } else {
                modeConfig.setMouseLocked(true);
            }
        }
    };

    Action releasedPrimaryAction = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            // boolean wasSet = isDrawing.compareAndSet(true, false);
            // if (wasSet)
            // System.out.println("stopped drawing");
        }
    };

    Action releasedSecondaryAction = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            // boolean wasSet = isTrackingLocked.compareAndSet(false, true);
            // if (wasSet)
            // System.out.println("started tracking");
        }
    };

    public Processing(JFrame component) {
        this.component = component;
        tabletTrackingThread.start();
        keyConfig.addObserver(this);
        modeConfig.addObserver(this);

        try {
            robot = new Robot();
        } catch (Exception e) {
            System.out.println("could not ");
        }

        update(keyConfig, null);
        update(modeConfig, null);

        component.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                tabletTrackingThread.stop();
                videoCap.close();
            }
        });

    }

    private Point[] orderSrc(Point[] points, int orientation) {
        Point[] ret;
        if (orientation == 1) {
            ret = new Point[] { points[0], points[1], points[2], points[3] };
        } else if (orientation == 2) {
            ret = new Point[] { points[1], points[2], points[3], points[0] };
        } else if (orientation == 3) {
            ret = new Point[] { points[2], points[3], points[0], points[1] };
        } else {
            ret = new Point[] { points[3], points[0], points[1], points[2] };
        }
        return ret;
    }

    private Mat getPerspectiveTransform(Point[] points, int height, int width) {
        MatOfPoint2f src = new MatOfPoint2f(orderSrc(points, modeConfig.getRotation()));

        MatOfPoint2f dst = new MatOfPoint2f(new Point(0, 0), new Point(width, 0), new Point(width, height),
                new Point(0, height));

        Mat warpMat = Imgproc.getPerspectiveTransform(src, dst);
        return warpMat;
    }

    private void sortPoints(Point[] points) {
        if (points.length != 4)
            return;

        double tX = 0d, tY = 0d;
        for (int i = 0; i < points.length; i++) {
            tX += points[i].x;
            tY += points[i].y;
        }

        final double cX = tX / 4d;
        final double cY = tY / 4d;

        Arrays.sort(points, new Comparator<Point>() {
            @Override
            public int compare(Point a, Point b) {
                double angleA = Math.atan2(cY - a.y, cX - a.x);
                double angleB = Math.atan2(cY - b.y, cX - b.x);

                return angleA < angleB ? -1 : 1;
            }
        });
    }

    /*
     *
     * Return a list of 4 points if the tablet is detected otherwise return null
     */

    private Point[] findTablet() {
        // Grab image from camera
        Mat videoFrame = videoCap.getCameraFrame();
        if (videoFrame.empty()) {
            return null;
        }

        streams[0].setFrame(videoFrame);

        // // Convert to HSV colorspace
        Mat hsv = new Mat();
        Imgproc.cvtColor(videoFrame, hsv, Imgproc.COLOR_BGR2HSV);

        // Binarize image
        Mat binarized = new Mat();

        int minH = tabletConfig.getMinH();
        int maxH = Math.max(tabletConfig.getMaxH(), tabletConfig.getMinH());
        int minS = tabletConfig.getMinS();
        int maxS = Math.max(tabletConfig.getMaxS(), tabletConfig.getMinS());
        int minV = tabletConfig.getMinV();
        int maxV = Math.max(tabletConfig.getMaxV(), tabletConfig.getMinV());

        Core.inRange(hsv, new Scalar(minH, minS, minV), new Scalar(maxH, maxS, maxV), binarized);

        // Dilate
        Mat element = Imgproc.getStructuringElement(Imgproc.CV_SHAPE_RECT,
                new Size(preConfig.getDilationKernelSize(), preConfig.getDilationKernelSize()));
        Mat dilated = new Mat();
        Imgproc.dilate(binarized, dilated, element);

        // Erode
        element = Imgproc.getStructuringElement(Imgproc.CV_SHAPE_RECT,
                new Size(preConfig.getErosionKernelSize(), preConfig.getErosionKernelSize()));
        Mat eroded = new Mat();
        Imgproc.erode(dilated, eroded, element);

        Mat overlay = new Mat();
        Mat mask = new Mat();
        Mat maskInverted = new Mat();
        Imgproc.cvtColor(eroded, mask, Imgproc.COLOR_GRAY2BGR);
        // Core.bitwise_not(mask, maskInverted);
        Core.addWeighted(mask, 1, videoFrame, 0.7, 0, overlay);
        streams[1].setFrame(overlay);

        // Get Contours and find the one most likely to be the tablet square
        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(eroded, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);

        int hierarchyValues[] = new int[(int) (hierarchy.total() * hierarchy.channels())];
        hierarchy.get(0, 0, hierarchyValues);

        double bestDonutRatio = -1;
        int bestParentContourIndex = -1;

        Mat contoursMat = new Mat();
        videoFrame.copyTo(contoursMat);

        for (int i = 0; i < contours.size(); i++) {
            int parentContourIndex = hierarchyValues[(i * 4) + 3];
            if (parentContourIndex == -1)
                continue;

            MatOfPoint childContour = contours.get(i);
            MatOfPoint parentContour = contours.get(parentContourIndex);

            double donutRatio = (double) Imgproc.contourArea(childContour)
                    / (double) Imgproc.contourArea(parentContour);

            if (donutRatio > bestDonutRatio) {
                bestDonutRatio = donutRatio;
                bestParentContourIndex = parentContourIndex;

                Imgproc.drawContours(contoursMat, contours, parentContourIndex, new Scalar(255, 0, 0));
            }
        }

        streams[2].setFrame(contoursMat);

        if (Math.abs(bestDonutRatio + 1) < 0.0001) {
            return null;
        }

        // // Approximate contour
        MatOfPoint2f bestContour = new MatOfPoint2f(contours.get(bestParentContourIndex).toArray());
        double e = 0.05d * Imgproc.arcLength(bestContour, true);
        MatOfPoint2f approx2f = new MatOfPoint2f();
        Imgproc.approxPolyDP(bestContour, approx2f, e, true);

        // Overlay approximated contour on original image
        List<MatOfPoint> approxContours = new ArrayList<>();
        MatOfPoint approx = new MatOfPoint();
        approx2f.convertTo(approx, CvType.CV_32S);
        approxContours.add(approx);
        Imgproc.drawContours(contoursMat, approxContours, 0, Scalar.all(255));

        Point[] ptsSorted = approx.toArray();
        if (ptsSorted.length != 4)
            return null;
        sortPoints(ptsSorted);

        videoFrame.release();
        hsv.release();
        binarized.release();
        element.release();
        dilated.release();
        eroded.release();
        hierarchy.release();
        contoursMat.release();

        return ptsSorted;
    }

    private Point toScreenCoordinates(Mat transformationMatrix, Point cameraCoordinates) {
        List<Point> inputPointsList = new ArrayList<>();
        inputPointsList.add(cameraCoordinates);
        Mat m1 = Converters.vector_Point_to_Mat(inputPointsList);
        Mat inputPointsMat = new Mat();
        m1.convertTo(inputPointsMat, CvType.CV_32F);

        List<Point> outputPointsList = new ArrayList<>();
        Mat m2 = Converters.vector_Point_to_Mat(outputPointsList);
        Mat outputPointsMat = new Mat();
        m2.convertTo(outputPointsMat, CvType.CV_32F);

        Core.perspectiveTransform(inputPointsMat, outputPointsMat, transformationMatrix);

        Point ret = new Point((int) outputPointsMat.get(0, 0)[0], (int) outputPointsMat.get(0, 0)[1]);
        return ret;
    }

    private Point findPen(Point[] tablet) {
        Mat videoFrame = videoCap.getCameraFrame();
        if (videoFrame.empty())
            return null;

        Mat hsv = new Mat();
        Imgproc.cvtColor(videoFrame, hsv, Imgproc.COLOR_BGR2HSV);

        Mat mask = new Mat(videoFrame.size(), CvType.CV_8UC3, Scalar.all(0d));
        Imgproc.fillConvexPoly(mask, new MatOfPoint(tablet), Scalar.all(255));

        Mat masked = new Mat();
        Core.bitwise_and(hsv, mask, masked);

        Mat added = new Mat();
        Core.addWeighted(mask, 1, videoFrame, 1, 0, added);
        videoFrame = added;

        Mat binarized = new Mat();

        int minH = penConfig.getMinH();
        int maxH = Math.max(penConfig.getMaxH(), penConfig.getMinH());
        int minS = penConfig.getMinS();
        int maxS = Math.max(penConfig.getMaxS(), penConfig.getMinS());
        int minV = penConfig.getMinV();
        int maxV = Math.max(penConfig.getMaxV(), penConfig.getMinV());

        Core.inRange(masked, new Scalar(minH, minS, minV), new Scalar(maxH, maxS, maxV), binarized);

        streams[3].setFrame(binarized);

        int INITIAL_KERNEL_SIZE = 3;
        Mat element = Imgproc.getStructuringElement(Imgproc.CV_SHAPE_RECT,
                new Size(INITIAL_KERNEL_SIZE, INITIAL_KERNEL_SIZE));

        element = Imgproc.getStructuringElement(Imgproc.CV_SHAPE_RECT,
                new Size(INITIAL_KERNEL_SIZE, INITIAL_KERNEL_SIZE));

        Mat dilated = new Mat();
        Imgproc.dilate(binarized, dilated, element);

        Mat penPreview = new Mat();
        Mat dilated3C = new Mat();
        Imgproc.cvtColor(dilated, dilated3C, Imgproc.COLOR_GRAY2BGR);

        Core.addWeighted(dilated3C, 1, videoFrame, 0.5, 0, penPreview);
        streams[4].setFrame(penPreview);

        Mat blurred = new Mat();
        Imgproc.GaussianBlur(dilated, blurred, new Size(7, 7), 0);

        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(blurred, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);

        double largestPen = -1;
        int largestPenIndex = -1;

        for (int i = 0; i < contours.size(); i++) {
            double area = Imgproc.contourArea(contours.get(i));
            if (area < 100)
                continue;
            if (area > largestPen) {
                largestPenIndex = i;
                largestPen = area;
            }
        }

        if (largestPenIndex == -1) {
            return null;
        }

        Moments m = Imgproc.moments(contours.get(largestPenIndex));
        double centerX = m.m10 / m.m00;
        double centerY = m.m01 / m.m00;

        Imgproc.drawContours(videoFrame, contours, largestPenIndex, new Scalar(0, 0, 0));
        Imgproc.circle(videoFrame, new Point(centerX, centerY), 2, new Scalar(0, 255, 0));

        return new Point(centerX, centerY);
    }

    class TabletTracker implements Runnable {
        @Override
        public void run() {
            Timer timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    if (!isTabletTrackingMode.get()) {
                        if (tabletLocation == null)
                            return;
                        Mat perspectiveTransform = getPerspectiveTransform(tabletLocation, 1080, 1920);
                        Point penCameraCoordinates = findPen(tabletLocation);
                        if (penCameraCoordinates == null) {
                            // tabletDisplay.setMarker(0, 0, false, false);
                            return;
                        }
                        Point penScreenCoordinates = toScreenCoordinates(perspectiveTransform, penCameraCoordinates);

                        if (!isMouseControlLocked.get()) {
                            setMouse(penScreenCoordinates.x, penScreenCoordinates.y);
                        }
                        // tabletDisplay.setMarker((int) penScreenCoordinates.x, (int)
                        // penScreenCoordinates.y, isDrawing.get(), true);

                        perspectiveTransform.release();
                    } else {
                        Point[] detected = findTablet();
                        if (detected != null) {
                            tabletLocation = detected;
                        }
                        // tabletDisplay.setMarker(0, 0, false, false);
                    }
                }
            }, 0, 20);
        }
    }

    private void setMouse(double x, double y) {
        robot.mouseMove((int) x, (int) y);
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o == modeConfig) {
            isMouseControlLocked.set(modeConfig.isMouseLocked());
            return;
        } else if (o == keyConfig) {
            JRootPane root = component.getRootPane();
            root.getInputMap().clear();
            root.getActionMap().clear();
            root.getInputMap(IFW).put(KeyStroke.getKeyStroke(ButtonPanel.getKeyText(keyConfig.getPrimary())),
                    "pressedPrimary");
            root.getInputMap(IFW).put(
                    KeyStroke.getKeyStroke("released " + ButtonPanel.getKeyText(keyConfig.getPrimary())),
                    "releasedPrimary");
            root.getActionMap().put("pressedPrimary", pressedPrimaryAction);
            root.getActionMap().put("releasedPrimary", releasedPrimaryAction);

            root.getInputMap(IFW).put(KeyStroke.getKeyStroke(ButtonPanel.getKeyText(keyConfig.getSecondary())),
                    "pressedSecondary");
            root.getInputMap(IFW).put(
                    KeyStroke.getKeyStroke("released " + ButtonPanel.getKeyText(keyConfig.getSecondary())),
                    "releasedSecondary");
            root.getActionMap().put("pressedSecondary", pressedSecondaryAction);
            root.getActionMap().put("releasedSecondary", releasedSecondaryAction);
        }
    }
}