package com.stikanek.states;

import javax.swing.JButton;
import java.awt.Color;
import com.stikanek.mainclasses.StatePanel;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MenuState implements State {

    private final StatePanel panel;
    private final JButton btn = new JButton();
    private final Logger logger;

    public MenuState(StatePanel jpanel, Logger l) {
        this.panel = jpanel;
        this.logger = l;
        btn.addActionListener(e -> {
            logger.log(Level.INFO, "Menu button clicked. Switching to GameState.");
            panel.setState(panel.getGameState());
        });
    }

    @Override
    public void update() {
    }

    @Override
    public void entered() {
        panel.removeAll();
        panel.revalidate();
        panel.repaint();
        panel.setBackground(Color.blue);
        panel.add(btn);
    }

    @Override
    public void render() {
    }

    @Override
    public void paintScreen() {
    }
}
