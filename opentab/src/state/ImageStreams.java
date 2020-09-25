package state;

/**
 * @author Derek Li, Rithik Mamidi, Samarth Patel
 * @version 1.0 <br>
 *
 *          A class used to store and access multiple image streams. <br>
 */
public class ImageStreams {
    private static ImageStreams instance = null;

    private final ImageStream[] streams;

    public static ImageStreams getInstance() {
        if (instance == null)
            instance = new ImageStreams(5);
        return instance;
    }

    public ImageStreams(int numStreams) {
        streams = new ImageStream[numStreams];

        for (int i = 0; i < numStreams; i++) {
            streams[i] = new ImageStream();
        }
    }

    public ImageStream getImageStream(int i) {
        return streams[i];
    }

    public ImageStream[] getAllStreams() {
        return streams;
    }
}