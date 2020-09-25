package ui.components;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.UIManager;
import com.formdev.flatlaf.FlatDarculaLaf;
import image_processing.Processing;

/**
 * @author Derek Li, Rithik Mamidi, Samarth Patel
 * @version 1.0 <br>
 *
 *          Runnable that creates the main UI container and sub elements <br>
 */
public class MainUI extends JFrame {
    private TabbedView tabbedView;
    private ButtonPanel buttonPanel;

    public MainUI() {
        // setJMenuBar(new MenuBar());
        setLayout(new BorderLayout());
        buttonPanel = new ButtonPanel();
        tabbedView = new TabbedView();
        add(buttonPanel, BorderLayout.NORTH);
        add(tabbedView, BorderLayout.CENTER);

        setPreferredSize(new Dimension(1000, 600));
        setMinimumSize(new Dimension(1000, 600));
        setTitle("OpenTab Viewer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public static void main(String[] args) {
        FlatDarculaLaf.install();
        var frame = new MainUI();
        new Processing(frame);
    }
}