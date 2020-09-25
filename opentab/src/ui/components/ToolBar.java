package ui.components;

import ui.utils.PopupGenerator;
import ui.utils.ThemeSelector;

import java.util.Observable;
import java.util.Observer;

import javax.swing.*;

/**
 * @author Derek Li, Rithik Mamidi, Samarth Patel
 * @version 1.0 <br>
 *
 *          A toolbar that dispalys common actions <br>
 */

@SuppressWarnings("deprecation")
public class ToolBar extends JToolBar implements Observer {
    private static final long serialVersionUID = 589768124516959666L;
    JButton load, start, pause, reset, theme;

    public ToolBar() {
        JLabel label = new JLabel("Quick Actions");
        add(label);

        load = new JButton(new ImageIcon("images/open.png"));
        load.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                // PopupGenerator.CreatePopup(new JoinGame(), this_, "Join Game", false);
            }
        });
        add(load);

        start = new JButton(new ImageIcon("images/start.png"));
        ToolBar this_ = this;
        start.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                // PopupGenerator.CreatePopup(new MakeGame(), this_, "Make Game", false);
            }
        });
        add(start);

        pause = new JButton(new ImageIcon("images/pause.png"));
        pause.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                // PopupGenerator.CreatePopup(new MakeGame(), this_, "Make Game", false);
            }
        });
        add(pause);

        reset = new JButton(new ImageIcon("images/reset.png"));
        reset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                // PopupGenerator.CreatePopup(new MakeGame(), this_, "Make Game", false);
            }
        });
        add(reset);

        theme = new JButton(new ImageIcon("images/theme.png"));
        theme.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PopupGenerator.CreatePopup(new ThemeSelector(), this_, "Change Theme", false);
            }
        });
        add(theme);
    }

    @Override
    public void update(Observable o, Object arg) {

    }
}