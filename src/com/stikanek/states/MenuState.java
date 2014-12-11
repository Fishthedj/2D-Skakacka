package com.stikanek.states;

import com.stikanek.mainclasses.StatePanel;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;

/**
 * The <code>MenuState</code> class is used to represent menu in a game. While entering this state the <code>entered</code>
 * method should be called in order to revalidate the underlying <code>JPanel</code>. This class implements <code>State</code> interface
 * therefore provides methods for updating the logic, rendering to double buffered image and subsequent painting this image on screen. 
 * 
 * @author Pavel
 * @see State
 */
public class MenuState implements State {

    private final StatePanel panel;
    private final JButton btn = new JButton();
    private final Logger logger;

    /**
     * Constructs new <code>MenuState</code> object that uses given <code>jpanel</code> object as its drawing
     * area.
     * @param jpanel <code>JPanel</code> used for painting 
     * @param l
     */
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
    
    @Override
    public void processKeyPressed(KeyEvent e){   
    }
    
    @Override
    public void processKeyReleased(KeyEvent e){
    }
}
