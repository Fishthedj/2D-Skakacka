package com.stikanek.states;

import java.awt.event.KeyEvent;

/**
 * This interface is to be implemented by individual states the program can reach. It provides
 * method for updating game logic between rendering and also a method for painting current 
 * program state on screen. Apart from updating and rendering it also deals with processing
 * keyboard inputs.
 * @author Pavel
 */
public interface State {

    /**
     * Updates game logic between painting individual frames.
     */
    void update();

    /**
     * Renders current program state to double buffered image.
     */
    void render();

    /**
     * Initializes the state when first entered.
     */
    void entered();

    /**
     * Paints double buffered image on the screen.
     */
    void paintScreen();

    /**
     * Processed the KeyEvent when keyboard key is pressed.
     * @param e KeyEvent to be processed
     */
    void processKeyPressed(KeyEvent e);

    /**
     * Processed the KeyEvent when keyboard key is released.
     * @param e KeyEvent to be processed
     */
    void processKeyReleased(KeyEvent e);
}
