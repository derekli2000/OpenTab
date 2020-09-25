package ui.utils;

import java.awt.Component;
import javax.swing.JOptionPane;

/**
 * @author Derek Li, Rithik Mamidi, Samarth Patel
 * @version 1.0 <br>
 *
 *          A class used to generate error message pop ups
 */
public class ErrorPopupGenerator {
    /*
     * @requires none
     * 
     * @modifies none
     * 
     * @effects creates an error popup with the given info
     * 
     * @returns none
     */
    public static void CreateErrorPopup(Component frame, String encounteredDuring, String detailedMessage) {
        JOptionPane.showMessageDialog(frame, detailedMessage, encounteredDuring, JOptionPane.ERROR_MESSAGE);
    }
}