package state;

import ui.utils.Storage;
import java.util.Observable;
import java.awt.Color;

/**
 * @author Derek Li, Rithik Mamidi, Samarth Patel
 * @version 1.0 <br>
 *
 *          A class used to change the theme. <br>
 */
@SuppressWarnings("deprecation")
public class Theme extends Observable {
    private static Theme instance = null;
    private Color primary;
    private Color secondary;

    /*
     * @requires none
     * 
     * @modifies this
     * 
     * @effects attempts to read configured theme from settings and sets a default
     * otherwise
     * 
     * @returns none
     */
    public Theme() {
        String primarySetting = Storage.readSettingOrDefault("theme-primary", Integer.toString(Color.BLACK.getRGB()));
        String secondarySetting = Storage.readSettingOrDefault("theme-secondary",
                Integer.toString(Color.PINK.getRGB()));

        try {
            this.primary = new Color(Integer.parseInt(primarySetting));
        } catch (NumberFormatException e) {
            this.primary = Color.PINK;
        }
        try {
            this.secondary = new Color(Integer.parseInt(secondarySetting));
        } catch (NumberFormatException e) {
            this.secondary = Color.BLACK;
        }
    }

    public static Theme getInstance() {
        if (instance == null)
            instance = new Theme();
        return instance;
    }

    /*
     * @requires none
     * 
     * @modifies none
     * 
     * @effects none
     * 
     * @returns the primary color
     */
    public Color getPrimary() {
        return primary;
    }

    /*
     * @requires none
     * 
     * @modifies none
     * 
     * @effects none
     * 
     * @returns thge secondary color
     */
    public Color getSecondary() {
        return secondary;
    }

    /*
     * @requires none
     * 
     * @modifies this
     * 
     * @effects sets the primary color, saves to storage, and notifies observers
     * 
     * @returns none
     */
    public void setPrimary(Color primary) {
        this.primary = primary;
        Storage.setSetting("theme-primary", Integer.toString(primary.getRGB()));

        setChanged();
        notifyObservers();
    }

    /*
     * @requires none
     * 
     * @modifies this
     * 
     * @effects sets the secondary color, saves to storage, and notifies observers
     * 
     * @returns none
     */
    public void setSecondary(Color secondary) {
        this.secondary = secondary;
        Storage.setSetting("theme-secondary", Integer.toString(secondary.getRGB()));

        setChanged();
        notifyObservers();
    }
}