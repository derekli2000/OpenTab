package state;

import java.util.Observable;

import ui.utils.Storage;

/**
 * @author Derek Li, Rithik Mamidi, Samarth Patel
 * @version 1.0 <br>
 *
 *          A class used to store preprocessing kernel size preferences. <br>
 */
@SuppressWarnings("deprecation")
public class PreprocessingConfig extends Observable {
    private static PreprocessingConfig instance = null;

    private int erosionKernelSize = 5, dilationKernelSize = 5, thresholdLow = 0, thresholdHigh = 75;

    public PreprocessingConfig() {
        String raw = Storage.readSettingOrDefault("pre-config", "5 5 0 75");
        String[] values = raw.split(" ");

        erosionKernelSize = Integer.parseInt(values[0]);
        dilationKernelSize = Integer.parseInt(values[1]);
        thresholdLow = Integer.parseInt(values[2]);
        thresholdHigh = Integer.parseInt(values[3]);
    }

    public static PreprocessingConfig getInstance() {
        if (instance == null)
            instance = new PreprocessingConfig();
        return instance;
    }

    public int getThresholdHigh() {
        return thresholdHigh;
    }

    public void setThresholdHigh(int thresholdHigh) {
        this.thresholdHigh = thresholdHigh;

        String newSettings = String.format("%d %d %d %d", erosionKernelSize, dilationKernelSize, thresholdLow,
                thresholdHigh);
        Storage.setSetting("pre-config", newSettings);

        setChanged();
        notifyObservers();
    }

    public int getDilationKernelSize() {
        return dilationKernelSize;
    }

    public void setDilationKernelSize(int dilationKernelSize) {
        this.dilationKernelSize = dilationKernelSize;

        String newSettings = String.format("%d %d %d %d", erosionKernelSize, dilationKernelSize, thresholdLow,
                thresholdHigh);
        Storage.setSetting("pre-config", newSettings);

        setChanged();
        notifyObservers();
    }

    public void setErosionKernelSize(int erosionKernelSize) {
        this.erosionKernelSize = erosionKernelSize;

        String newSettings = String.format("%d %d %d %d", erosionKernelSize, dilationKernelSize, thresholdLow,
                thresholdHigh);
        Storage.setSetting("pre-config", newSettings);

        setChanged();
        notifyObservers();
    }

    public int getErosionKernelSize() {
        return erosionKernelSize;
    }

    public void setThresholdLow(int thresholdLow) {
        this.thresholdLow = thresholdLow;

        String newSettings = String.format("%d %d %d %d", erosionKernelSize, dilationKernelSize, thresholdLow,
                thresholdHigh);
        Storage.setSetting("pre-config", newSettings);

        setChanged();
        notifyObservers();
    }

    public int getThresholdLow() {
        return thresholdLow;
    }

}