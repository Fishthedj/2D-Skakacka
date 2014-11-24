package com.stikanek.mainclasses;

import com.stikanek.states.GameState;
import com.stikanek.states.State;
import com.stikanek.states.MenuState;
import com.stikanek.states.GameStateManager;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StatePanel extends JPanel implements GameStateManager, Runnable {

    public static final int PWIDTH = 320;
    public static final int PHEIGHT = 240;
    private static final int NO_DELAYS_PER_YIELD = 16;
    private static final int MAX_FRAME_SKIPS = 5;
    private volatile boolean running = false;
    private State currentState;
    private final State gameState;
    private final State menuState;
    private Thread animator;
    private volatile boolean gameOver = false;
    private volatile boolean isPaused = false;
    private final int period;
    private static final Logger logger = Logger.getLogger("");

    public StatePanel() {
        period = 100000000;
        try {
            logger.addHandler(new FileHandler("2D-Skakacka.%u.log"));
        } catch (IOException | SecurityException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
        gameState = new GameState(this, PWIDTH, PHEIGHT, logger);
        menuState = new MenuState(this, logger);
//        setBackground(Color.white);
//        setPreferredSize(new Dimension(PWIDTH, PHEIGHT));
        setFocusable(true);
        requestFocus();
        init();
        readyForTermination();
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                testPress(e.getX(), e.getY());
            }
        });
    }

    private void readyForTermination() {
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();
                if (keyCode == KeyEvent.VK_ESCAPE
                        || keyCode == KeyEvent.VK_Q
                        || keyCode == KeyEvent.VK_END
                        || keyCode == KeyEvent.VK_ESCAPE && e.isControlDown()) {
                    logger.log(Level.INFO, "End key pressed. Key code: {0}", keyCode);
                    running = false;
                }
            }
        });
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(PWIDTH, PHEIGHT);
    }

    private void testPress(int x, int y) {
        if (!isPaused && !gameOver) {
            logger.log(Level.INFO,"Clicked on {0} {1}.",new Object[]{x,y});
        }
    }

    private void init() {
        setState(menuState);
    }

    public State getGameState() {
        return gameState;
    }

    public State getMenuState() {
        return menuState;
    }

    @Override
    public void addNotify() {
        super.addNotify();
        startGame();
    }

    public void startGame() {
        if (animator == null || !running) {
            animator = new Thread(this);
            animator.start();
        }
    }

    @Override
    public void setState(State s) {
        currentState = s;
        currentState.entered();
    }

    public void update() {
        currentState.update();
    }

    public void render() {
        currentState.render();
    }

    public void paintScreen() {
        currentState.paintScreen();
    }

    @Override
    public void run() {
        long beforeTime, afterTime, timeDiff, sleepTime;
        long overSleepTime = 0L;
        int noDelays = 0;
        long excess = 0L;

        beforeTime = System.nanoTime();
        running = true;
        while (running) {
            update();
            render();
            paintScreen();

            afterTime = System.nanoTime();
            timeDiff = beforeTime - afterTime;
            sleepTime = (period - timeDiff) - overSleepTime;
            if (sleepTime > 0) {
                try {
                    Thread.sleep(sleepTime / 1000000L);
                } catch (InterruptedException e) {
                    overSleepTime = System.nanoTime() - afterTime - sleepTime;
                }
            } else {
                overSleepTime = 0L;
                excess -= sleepTime;
                if (++noDelays >= NO_DELAYS_PER_YIELD) {
                    Thread.yield();
                    noDelays = 0;
                }
            }
            beforeTime = System.nanoTime();
            int skips = 0;
            while (excess >= period && skips <= MAX_FRAME_SKIPS) {
                excess -= period;
                update();
                skips++;
            }
        }
        System.exit(0);
    }

}
