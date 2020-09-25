package state;

import java.util.Observable;

import ui.utils.Storage;

/**
 * @author Derek Li, Rithik Mamidi, Samarth Patel
 * @version 1.0 <br>
 *
 *          A class used to store the user's prefered color range for tracking
 *          the pen. <br>
 */
@SuppressWarnings("deprecation")
public class PenConfig extends Observable {
    private static PenConfig instance = null;

    private int minH = 20, maxH = 80, minS = 50, maxS = 150, minV = 100, maxV = 150;

    public PenConfig() {
        String raw = Storage.readSettingOrDefault("pen-config", "20 80 50 150 100 150");
        String[] values = raw.split(" ");

        minH = Integer.parseInt(values[0]);
        maxH = Integer.parseInt(values[1]);
        minS = Integer.parseInt(values[2]);
        maxS = Integer.parseInt(values[3]);
        minV = Integer.parseInt(values[4]);
        maxV = Integer.parseInt(values[5]);
    }

    public static PenConfig getInstance() {
        if (instance == null)
            instance = new PenConfig();
        return instance;
    }

    public void setMinH(int minH) {
        this.minH = minH;

        String newSettings = String.format("%d %d %d %d %d %d", minH, maxH, minS, maxS, minV, maxV);
        Storage.setSetting("pen-config", newSettings);

        setChanged();
        notifyObservers();
    }

    public void setMaxH(int maxH) {
        this.maxH = maxH;

        String newSettings = String.format("%d %d %d %d %d %d", minH, maxH, minS, maxS, minV, maxV);
        Storage.setSetting("pen-config", newSettings);

        setChanged();
        notifyObservers();
    }

    public void setMinS(int minS) {
        this.minS = minS;

        String newSettings = String.format("%d %d %d %d %d %d", minH, maxH, minS, maxS, minV, maxV);
        Storage.setSetting("pen-config", newSettings);

        setChanged();
        notifyObservers();
    }

    public void setMaxS(int maxS) {
        this.maxS = maxS;

        String newSettings = String.format("%d %d %d %d %d %d", minH, maxH, minS, maxS, minV, maxV);
        Storage.setSetting("pen-config", newSettings);

        setChanged();
        notifyObservers();
    }

    public void setMinV(int minV) {
        this.minV = minV;

        String newSettings = String.format("%d %d %d %d %d %d", minH, maxH, minS, maxS, minV, maxV);
        Storage.setSetting("pen-config", newSettings);

        setChanged();
        notifyObservers();
    }

    public void setMaxV(int maxV) {
        this.maxV = maxV;

        String newSettings = String.format("%d %d %d %d %d %d", minH, maxH, minS, maxS, minV, maxV);
        Storage.setSetting("pen-config", newSettings);

        setChanged();
        notifyObservers();
    }

    public int getMinH() {
        return minH;
    }

    public int getMaxH() {
        return maxH;
    }

    public int getMinS() {
        return minS;
    }

    public int getMaxS() {
        return maxS;
    }

    public int getMinV() {
        return minV;
    }

    public int getMaxV() {
        return maxV;
    }
}