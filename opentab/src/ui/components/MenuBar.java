package ui.components;

import java.util.Observable;
import java.util.Observer;

import javax.swing.*;
import java.awt.event.*;

/**
 * @author Derek Li, Rithik Mamidi, Samarth Patel
 * @version 1.0 <br>
 *
 *          A class used to display the menu bar <br>
 */
@SuppressWarnings("deprecation")
public class MenuBar extends JMenuBar implements Observer {
    private static final long serialVersionUID = 1L;

    private JMenuItem key1, key2, start, stop;

    public MenuBar() {
        JMenu commands = new JMenu("Commands");
        add(commands);

        start = new JMenuItem("Start");
        MenuBar this_ = this;
        start.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            }
        });
        commands.add(start);

        stop = new JMenuItem("Stop");
        stop.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            }
        });
        commands.add(stop);

        JMenu bindings = new JMenu("Bindings");
        add(bindings);

        key1 = new JMenuItem("Key1");
        key1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            }
        });
        bindings.add(key1);

        key2 = new JMenuItem("Key2");
        key2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            }
        });
        bindings.add(key2);
    }

    @Override
    public void update(Observable o, Object arg) {
        // TODO Auto-generated method stub

    }
}