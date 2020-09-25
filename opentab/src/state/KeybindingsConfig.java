package state;

import ui.utils.Storage;
import java.util.Observable;
import java.awt.Color;

/**
 * @author Derek Li, Rithik Mamidi, Samarth Patel
 * @version 1.0 <br>
 *
 *          A class used to store key binding prefernces <br>
 */
@SuppressWarnings("deprecation")
public class KeybindingsConfig extends Observable {
    private static KeybindingsConfig instance = null;
    private Integer primary;
    private Integer secondary;

    public KeybindingsConfig() {
        String primarySetting = Storage.readSettingOrDefault("keybinding-primary", "88");
        String secondarySetting = Storage.readSettingOrDefault("keybinding-secondary", "86");

        try {
            primary = Integer.valueOf(primarySetting);
            if (primary > 7000)
                throw new RuntimeException();
        } catch (Exception e) {
            primary = 88;
        }
        try {
            secondary = Integer.valueOf(secondarySetting);
            if (secondary > 7000)
                throw new RuntimeException();
        } catch (Exception e) {
            secondary = 86;
        }
    }

    public static KeybindingsConfig getInstance() {
        if (instance == null)
            instance = new KeybindingsConfig();
        return instance;
    }

    public Integer getPrimary() {
        return primary;
    }

    public Integer getSecondary() {
        return secondary;
    }

    public void setPrimary(Integer primary) {
        this.primary = primary;
        System.out.println("setting primary to " + primary);
        // Storage.setSetting("keybinding-primary", String.valueOf(primary));
        setChanged();
        notifyObservers();
    }

    public void setSecondary(Integer secondary) {
        this.secondary = secondary;
        System.out.println("setting secondary to " + secondary);
        // Storage.setSetting("keybinding-secondary", String.valueOf(secondary));

        setChanged();
        notifyObservers();
    }
}