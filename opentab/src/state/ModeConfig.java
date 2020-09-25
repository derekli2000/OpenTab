package state;

import java.util.Observable;

import ui.utils.Storage;

/**
 * @author Derek Li, Rithik Mamidi, Samarth Patel
 * @version 1.0 <br>
 *
 *          A class used change the current tracking mode. <br>
 */
public class ModeConfig extends Observable {
    private static ModeConfig instance = null;

    private boolean isTablet = true;
    private boolean isMouseLocked = true;
    private int rotation = Integer.parseInt(Storage.readSettingOrDefault("orientation", "1"));

    public static ModeConfig getInstance() {
        if (instance == null)
            instance = new ModeConfig();
        return instance;
    }

    public void setRotation(int rotation) {
        Storage.setSetting("orientation", Integer.toString(rotation));
        this.rotation = rotation;
    }

    public void setTablet(boolean isTablet) {
        this.isTablet = isTablet;
    }

    public void setMouseLocked(boolean isMouseLocked) {
        this.isMouseLocked = isMouseLocked;

        setChanged();
        notifyObservers();
    }

    public final boolean isTablet() {
        return isTablet;
    }

    public final boolean isMouseLocked() {
        return isMouseLocked;
    }

    public final int getRotation() {
        return rotation;
    }
}