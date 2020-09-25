package state;

import java.util.Observable;

import ui.utils.Storage;

/**
 * @author Derek Li, Rithik Mamidi, Samarth Patel
 * @version 1.0 <br>
 *
 *          A class used to store the user's prefered color range for tracking
 *          the tablet. <br>
 */
@SuppressWarnings("deprecation")
public class TabletConfig extends Observable {
    private static TabletConfig instance = null;

    private int minH = 1, maxH = 255, minS = 1, maxS = 255, minV = 1, maxV = 150;

    public TabletConfig() {
        String raw = Storage.readSettingOrDefault("tablet-config", "1 255 1 255 1 150");
        String[] values = raw.split(" ");

        minH = Integer.parseInt(values[0]);
        maxH = Integer.parseInt(values[1]);
        minS = Integer.parseInt(values[2]);
        maxS = Integer.parseInt(values[3]);
        minV = Integer.parseInt(values[4]);
        maxV = Integer.parseInt(values[5]);

    }

    public static TabletConfig getInstance() {
        if (instance == null)
            instance = new TabletConfig();
        return instance;
    }

    public void setMinH(int minH) {
        this.minH = minH;

        String newSettings = String.format("%d %d %d %d %d %d", minH, maxH, minS, maxS, minV, maxV);
        Storage.setSetting("tablet-config", newSettings);

        setChanged();
        notifyObservers();
    }

    public void setMaxH(int maxH) {
        this.maxH = maxH;

        String newSettings = String.format("%d %d %d %d %d %d", minH, maxH, minS, maxS, minV, maxV);
        Storage.setSetting("tablet-config", newSettings);

        setChanged();
        notifyObservers();
    }

    public void setMinS(int minS) {
        this.minS = minS;

        String newSettings = String.format("%d %d %d %d %d %d", minH, maxH, minS, maxS, minV, maxV);
        Storage.setSetting("tablet-config", newSettings);

        setChanged();
        notifyObservers();
    }

    public void setMaxS(int maxS) {
        this.maxS = maxS;

        String newSettings = String.format("%d %d %d %d %d %d", minH, maxH, minS, maxS, minV, maxV);
        Storage.setSetting("tablet-config", newSettings);

        setChanged();
        notifyObservers();
    }

    public void setMinV(int minV) {
        this.minV = minV;

        String newSettings = String.format("%d %d %d %d %d %d", minH, maxH, minS, maxS, minV, maxV);
        Storage.setSetting("tablet-config", newSettings);

        setChanged();
        notifyObservers();
    }

    public void setMaxV(int maxV) {
        this.maxV = maxV;

        String newSettings = String.format("%d %d %d %d %d %d", minH, maxH, minS, maxS, minV, maxV);
        Storage.setSetting("tablet-config", newSettings);

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