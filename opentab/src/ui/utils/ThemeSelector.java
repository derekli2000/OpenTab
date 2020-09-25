package ui.utils;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import state.Theme;

/**
 * @author Derek Li, Rithik Mamidi, Samarth Patel
 * @version 1.0 <br>
 *
 *          A class used to edit the current theme
 */
public class ThemeSelector extends JPanel {
    private static final long serialVersionUID = 2127345128047162998L;
    private static final Theme theme = Theme.getInstance();

    JButton primaryChooserButton, secondaryChooserButton;

    /*
     * @requires none
     * 
     * @modifies this
     * 
     * @effects initializes component according to the class definition
     * 
     * @returns none
     */
    public ThemeSelector() {
        JColorChooser primaryPicker = new JColorChooser(theme.getPrimary());
        primaryPicker.getSelectionModel().addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                theme.setPrimary(primaryPicker.getColor());
            }
        });

        primaryChooserButton = new JButton("Set Primary Color");
        primaryChooserButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PopupGenerator.CreatePopup(primaryPicker, primaryChooserButton, "Set Primary Color", false);
            }
        });
        add(primaryChooserButton);

        JColorChooser secondaryPicker = new JColorChooser(theme.getSecondary());
        secondaryPicker.getSelectionModel().addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                theme.setSecondary(secondaryPicker.getColor());
            }
        });

        secondaryChooserButton = new JButton("Set Secondary Color");
        secondaryChooserButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PopupGenerator.CreatePopup(secondaryPicker, secondaryChooserButton, "Set Secondary Color", false);
            }
        });
        add(secondaryChooserButton);
    }
}