package ui.components;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.EmptyBorder;

import state.ImageStream;
import state.ImageStreams;
import state.ModeConfig;
import state.PreprocessingConfig;
import state.TabletConfig;
import state.PenConfig;

interface SliderOnChangeCallback {
    void run(int newValue);
}

class SliderConfig {
    final String sliderLabel;
    final SliderOnChangeCallback callback;
    final int min, max, defaultValue;

    public SliderConfig(String sliderLabel, int min, int max, int defaultValue, SliderOnChangeCallback callback) {
        this.sliderLabel = sliderLabel;
        this.min = min;
        this.max = max;
        this.defaultValue = defaultValue;
        this.callback = callback;
    }
}

/**
 * @author Derek Li, Rithik Mamidi, Samarth Patel
 * @version 1.0 <br>
 *
 *          A panel that displays a list of settings as well as the
 *          corresponding image stream for that configuration <br>
 */

class ConfigTab extends JPanel {
    class ImagePreviewPanel extends JPanel implements Observer {
        private ImageStream imageStream;

        public ImagePreviewPanel(ImageStream imageStream) {
            imageStream.addObserver(this);
            this.imageStream = imageStream;
            setBorder(new EmptyBorder(10, 10, 10, 10));
        }

        @Override
        public void paint(Graphics g) {
            super.paintComponent(g);

            BufferedImage image = imageStream.getFrame();

            int panelWidth = getWidth();
            int panelHeight = getHeight();
            int imageWidth = image.getWidth();
            int imageHeight = image.getHeight();
            float imageRatio = (float) imageWidth / (float) imageHeight;

            int xOrigin, yOrigin;
            int newWidth, newHeight;

            if (panelWidth > panelHeight) {
                newHeight = panelHeight;
                newWidth = (int) ((float) panelHeight * imageRatio);
                yOrigin = 0;
                xOrigin = (panelWidth - newWidth) / 2;
            } else {
                newHeight = (int) ((float) panelWidth / imageRatio);
                newWidth = panelWidth;
                xOrigin = 0;
                yOrigin = (panelHeight - newHeight) / 2;
            }

            g.drawImage(image, xOrigin, yOrigin, newWidth, newHeight, null);
        }

        @Override
        public void update(Observable a, Object b) {
            repaint();
        }
    }

    public ConfigTab(ImageStream o) {
        this(o, new SliderConfig[] {});
    }

    public ConfigTab(ImageStream o, SliderConfig[] s) {
        setLayout(new BorderLayout());

        add(new ImagePreviewPanel(o), BorderLayout.CENTER);
        add(new SettingsPanel(s), BorderLayout.EAST);
    }
}

/**
 * @author Derek Li, Rithik Mamidi, Samarth Patel
 * @version 1.0 <br>
 *
 *          A JPanel that generates and displays all all the previewable tabs
 *          <br>
 */

public class TabbedView extends JPanel {
    private static final long serialVersionUID = -4242177351728038716L;
    private static final PreprocessingConfig pConfig = PreprocessingConfig.getInstance();
    private static final TabletConfig tConfig = TabletConfig.getInstance();
    private static final PenConfig penC = PenConfig.getInstance();
    private static final ModeConfig modeC = ModeConfig.getInstance();

    private static final SliderConfig[] erosionSliders = {
            new SliderConfig("Erosion Kernel Size", 1, 10, pConfig.getErosionKernelSize(),
                    (v) -> pConfig.setErosionKernelSize(v)),
            new SliderConfig("Dilation Kernel Size", 1, 10, pConfig.getDilationKernelSize(),
                    (v) -> pConfig.setDilationKernelSize(v)), };
    private static final SliderConfig[] tabletSliders = {
            new SliderConfig("Minimum Hue", 1, 255, tConfig.getMinH(), (v) -> tConfig.setMinH(v)),
            new SliderConfig("Maximum Hue", 1, 255, tConfig.getMaxH(), (v) -> tConfig.setMaxH(v)),
            new SliderConfig("Minimum Saturation", 1, 255, tConfig.getMinS(), (v) -> tConfig.setMinS(v)),
            new SliderConfig("Maximum Saturation", 1, 255, tConfig.getMaxS(), (v) -> tConfig.setMaxS(v)),
            new SliderConfig("Minimum Value", 1, 255, tConfig.getMinV(), (v) -> tConfig.setMinV(v)),
            new SliderConfig("Maximum Value", 1, 255, tConfig.getMaxV(), (v) -> tConfig.setMaxV(v)) };
    private static final SliderConfig[] penSliders = {
            new SliderConfig("Minimum Hue", 1, 255, penC.getMinH(), (v) -> penC.setMinH(v)),
            new SliderConfig("Maximum Hue", 1, 255, penC.getMaxH(), (v) -> penC.setMaxH(v)),
            new SliderConfig("Minimum Saturation", 1, 255, penC.getMinS(), (v) -> penC.setMinS(v)),
            new SliderConfig("Maximum Saturation", 1, 255, penC.getMaxS(), (v) -> penC.setMaxS(v)),
            new SliderConfig("Minimum Value", 1, 255, penC.getMinH(), (v) -> penC.setMinV(v)),
            new SliderConfig("Maximum Value", 1, 255, penC.getMaxH(), (v) -> penC.setMaxV(v)) };
    private static final SliderConfig[] trackSliders = {
            new SliderConfig("Orientation", 1, 4, modeC.getRotation(), (v) -> modeC.setRotation(v)) };

    public TabbedView() {
        ImageStreams sampleStream = ImageStreams.getInstance();
        JTabbedPane tabbedPane = new JTabbedPane();

        setLayout(new GridLayout(0, 1));

        ConfigTab rawImageTab = new ConfigTab(sampleStream.getImageStream(0));
        ConfigTab erodedTab = new ConfigTab(sampleStream.getImageStream(1), TabbedView.erosionSliders);
        ConfigTab tabletTab = new ConfigTab(sampleStream.getImageStream(2), TabbedView.tabletSliders);
        ConfigTab penTab = new ConfigTab(sampleStream.getImageStream(3), TabbedView.penSliders);
        ConfigTab finalImageTab = new ConfigTab(sampleStream.getImageStream(4), TabbedView.trackSliders);

        tabbedPane.addTab("Live", null, rawImageTab, "Raw Camera Stream");
        tabbedPane.addTab("Tablet Color", null, tabletTab, "Tablet Color");
        tabbedPane.addTab("Preprocessing", null, erodedTab, "Noise Removal");
        tabbedPane.addTab("Pen Color", null, penTab, "Pen Color");
        tabbedPane.addTab("Tracking", null, finalImageTab, "Live Tracking");

        tabbedPane.setSelectedIndex(0);
        add(tabbedPane);
    }
}