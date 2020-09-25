package utils;

import java.util.ArrayList;
import java.util.Queue;
import java.util.List;
import java.util.LinkedList;

import org.opencv.core.Mat;
import org.opencv.core.Point;

import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.RenderingHints;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.event.*;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JMenuBar;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.border.EmptyBorder;
import java.awt.Robot;

import java.lang.IndexOutOfBoundsException;

/**
 * @author Derek Li, Rithik Mamidi, Samarth Patel
 * @version 1.0 <br>
 *
 *          A class used during testing to preview the tablet's tracking as well
 *          as move the mouse according to the pen's position
 */
public class TabletPreviewer extends JFrame {
    private final int CANVAS_WIDTH = 1920;
    private final int CANVAS_HEIGHT = 1080;

    private JPanel contentPane;
    private ArrayList<ArrayList<Point>> lines = new ArrayList<>();

    private Point marker = new Point(0, 0);
    private Queue<Point> markerHistory = new LinkedList<Point>();
    private Robot robot;

    private Color penColor = Color.RED;
    private long lastTime = System.nanoTime();

    public TabletPreviewer() {
        try {
            robot = new Robot();
        } catch (Exception e) {
            System.out.println("could not ");
        }

        lines.add(new ArrayList<>());
        // setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(0, 0, CANVAS_HEIGHT, CANVAS_WIDTH);

        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        contentPane = new JPanel();
        contentPane.setLayout(null);
        contentPane.setDoubleBuffered(true);
        setContentPane(contentPane);

        JMenu settings = new JMenu("Settings");
        settings.add(new AbstractAction("Pen Color") {

            @Override
            public void actionPerformed(ActionEvent e) {
                Color chosen = JColorChooser.showDialog(null, "Choose Pen Color", penColor);
                penColor = chosen;
            }
        });
        menuBar.add(settings);

        setVisible(true);
    }

    private final int HISTORY_LENGTH = 1;

    public void setMarker(int x, int y, boolean permanent, boolean setPen) {
        if (setPen) {
            markerHistory.add(new Point(x, y));
            if (markerHistory.size() > HISTORY_LENGTH) {
                markerHistory.poll();
            }

            marker = getAvgHistory();
            System.out.println("setting point");
            robot.mouseMove((int) marker.x, (int) marker.y);

            if (permanent) {
                System.out.println("adding point");
                lines.get(lines.size() - 1).add(marker);
            }
            if (!permanent && lines.get(lines.size() - 1).size() != 0) {
                System.out.println("making new stroke");
                lines.add(new ArrayList<>());
            }
        }

        repaint();
    }

    private Point getAvgHistory() {
        double avgX = 0, avgY = 0;
        for (Point p : markerHistory) {
            avgX += p.x;
            avgY += p.y;
        }
        return new Point((avgX / (double) markerHistory.size()), avgY / (double) markerHistory.size());
    }

    public void clear() {
        lines.clear();
        lines.add(new ArrayList<>());
        repaint();
    }

    @Override
    synchronized public void paint(Graphics g) {
        long currTime = System.nanoTime();
        long delta = currTime - lastTime;
        long fps = 1000000000l / delta;
        lastTime = currTime;
        setTitle(String.format("FPS: %d", fps));

        g = contentPane.getGraphics();
        Graphics2D graphics2d = (Graphics2D) g;

        graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        graphics2d.setColor(java.awt.Color.BLACK);
        // graphics2d.fillRect(0, 0, 800, 600);
        g.clearRect(0, 0, CANVAS_WIDTH + 100, CANVAS_HEIGHT + 100);

        // System.out.println(lines.size());
        for (ArrayList<Point> line : lines) {
            if (line.size() == 0)
                continue;
            graphics2d.setColor(penColor);

            Point p0 = line.get(0);
            graphics2d.fillOval((int) p0.x, (int) p0.y, 5, 5);

            for (int i = 1; i < line.size(); i++) {
                Point p1 = line.get(i - 1);
                Point p2 = line.get(i);

                graphics2d.drawLine((int) p1.x, (int) p1.y, (int) p2.x, (int) p2.y);
                // graphics2d.fillOval((int) p.x, (int) p.y, 5, 5);
            }
        }

        graphics2d.setColor(java.awt.Color.BLUE);
        graphics2d.fillOval((int) marker.x, (int) marker.y, 5, 5);
    }
}