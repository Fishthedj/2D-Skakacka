package com.stikanek.states;

import java.awt.event.KeyEvent;

public interface State {
    void update();
    void render();
    void entered();
    void paintScreen();
    void processKeyPressed(KeyEvent e);
    void processKeyReleased(KeyEvent e);
}
