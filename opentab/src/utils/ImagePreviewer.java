package utils;

import java.util.ArrayList;

import org.opencv.core.Mat;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import java.lang.IndexOutOfBoundsException;

/**
 * @author Derek Li, Rithik Mamidi, Samarth Patel
 * @version 1.0 <br>
 *
 *          Class used for testing that helps show Images at intermediary
 *          processing steps
 */
public class ImagePreviewer extends JFrame {
    private JPanel contentPane;
    private List<List<BufferedImage>> images = new ArrayList<>();

    public ImagePreviewer() {
        // setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(0, 0, 100, 100);
        contentPane = new JPanel();
        contentPane.setLayout(null);
        setContentPane(contentPane);
        setVisible(true);
    }

    public void addImage(int row, int col, Mat mat) {
        BufferedImage image = ImageUtils.mat2img(mat);

        if (row < 0 || row > images.size())
            throw new IndexOutOfBoundsException();
        if (row == images.size()) {
            if (col != 0)
                throw new IndexOutOfBoundsException();
            images.add(new ArrayList<BufferedImage>());
            images.get(row).add(image);
            return;
        }
        if (col < 0 || col > images.get(row).size())
            throw new IndexOutOfBoundsException();
        if (col == images.get(row).size()) {
            images.get(row).add(image);
        } else {
            images.get(row).get(col).flush();
            images.get(row).set(col, image);
        }
    }

    public void drawImage() {
        repaint();
    }

    @Override
    public void paint(Graphics g) {
        g = contentPane.getGraphics();
        int rowHeight = 0;

        for (int i = 0; i < images.size(); i++) {
            int colWidth = 0;
            int maxHeight = 0;

            for (int j = 0; j < images.get(i).size(); j++) {
                BufferedImage img = images.get(i).get(j);

                g.drawImage(img, colWidth, rowHeight, this);

                maxHeight = Math.max(maxHeight, img.getHeight());
                colWidth += img.getWidth();
            }

            rowHeight += maxHeight;
        }
    }
}