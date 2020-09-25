package ui.utils;

import ui.components.ButtonPanel;

import java.util.Observable;
import java.util.Observer;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.event.KeyListener;
import java.awt.Toolkit;
import java.awt.Dimension;

import state.Theme;

/**
 * @author Derek Li, Rithik Mamidi, Samarth Patel
 * @version 1.0 <br>
 *
 *          A class used to set the theme on all sub elements
 */
@SuppressWarnings("deprecation")
class PopupThemeObserver implements Observer {
    JFrame frame;
    final Theme theme = Theme.getInstance();

    PopupThemeObserver(JFrame frame) {
        this.frame = frame;
        theme.addObserver(this);
        ThemeSetter.setGlobalTheme(frame);
    }

    @Override
    public void update(Observable o, Object arg) {
        ThemeSetter.setGlobalTheme(frame);
    }
}

/**
 *
 * GUI Version of Conway's Game of Life
 * 
 * @author Anonymous Mouse
 * @version 1.0 <br>
 *
 *          A class used to create pop up messages that contain a given
 *          component <br>
 */
public class PopupGenerator {
    /*
     * @requires none
     * 
     * @modifies none
     * 
     * @effects creates a popup with the given panel
     * 
     * @returns none
     */
    public static void CreatePopup(JComponent component, JComponent root, String title, boolean resizeable) {
        JFrame frame2 = new JFrame(title);
        frame2.add(component);
        frame2.pack();
        frame2.setVisible(true);
        frame2.setResizable(resizeable);
        frame2.setLocationRelativeTo(root);
        new PopupThemeObserver(frame2);
    }

}