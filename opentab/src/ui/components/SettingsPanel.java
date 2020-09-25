package ui.components;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.*;
import java.awt.Event.*;

/**
 * @author Derek Li, Rithik Mamidi, Samarth Patel
 * @version 1.0 <br>
 *
 *          A class used to display a single sliders panel. Takes in a generic
 *          panel list of slider <br>
 */

public class SettingsPanel extends JPanel {
    private static final long serialVersionUID = 3443035733110807320L;

    public SettingsPanel(SliderConfig[] sliders) {
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        c.weightx = 1;

        for (int i = 0; i < sliders.length; i++) {
            SliderConfig config = sliders[i];

            JLabel label = new JLabel(config.sliderLabel);
            JSlider slider = new JSlider(JSlider.HORIZONTAL, config.min, config.max, config.defaultValue);
            slider.setMinorTickSpacing(1);
            slider.setSnapToTicks(true);
            slider.setPaintTrack(true);
            slider.setPaintTicks(true);
            slider.setPaintLabels(true);
            slider.addChangeListener(new ChangeListener() {
                public void stateChanged(ChangeEvent event) {
                    int value = slider.getValue();
                    config.callback.run(value);
                }
            });
            slider.setBorder(new EmptyBorder(5, 10, 0, 10));

            c.gridx = 0;
            c.gridy = i;
            add(label, c);

            c.gridx = 1;
            c.gridy = i;
            add(slider, c);
        }
    }
}