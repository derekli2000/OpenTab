package ui.utils;

import java.awt.Component;
import java.awt.Container;
import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;

import state.Theme;

/**
 * @author Derek Li, Rithik Mamidi, Samarth Patel
 * @version 1.0 <br>
 *
 *          A class used to recursively set the global theme on children
 *          elements
 */
public class ThemeSetter {
    static final Theme theme = Theme.getInstance();

    /*
     * @requires none
     * 
     * @modifies every child component
     * 
     * @effects sets foreground and background color of every child component of the
     * given container recursively
     * 
     * @returns none
     */
    public static void setGlobalTheme(Container container) {
        setGlobalTheme(container, theme.getPrimary(), theme.getSecondary());
    }

    private static void setGlobalTheme(Container container, Color primary, Color secondary) {
        container.setForeground(primary);

        if (container instanceof JButton || container instanceof JSpinner || container instanceof JTextField) {
            container.setBackground(theme.getPrimary().darker().darker().darker());
            return;
        }

        container.setBackground(secondary);

        for (Component child : container.getComponents()) {
            if (child instanceof Container) {
                if (child instanceof JPanel) {
                    child.setBackground(secondary);
                    child.setForeground(primary);
                }
                setGlobalTheme((Container) child, primary, secondary);
            }
        }
    }
}