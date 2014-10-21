package statepanel;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Color;
//import java.awt.Graphics;
//import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class StatePanel extends JPanel implements GameStateManager, Runnable{
    private static final int PHEIGHT = 400;
    private static final int PWIDTH = 400;
    private static final int NO_DELAYS_PER_YIELD = 16;
    private static final int MAX_FRAME_SKIPS = 5;    
    private volatile boolean running = false;
    private State currentState;
    private final State gameState;
    private final State menuState;
    private Thread animator;
//    private Graphics dbg;
//    private Image dbImage = null;
    private volatile boolean gameOver = false;
    private volatile boolean isPaused = false;   
    private int period;
    
    public StatePanel(){
        gameState = new GameState(this, PWIDTH, PHEIGHT);
        menuState = new MenuState(this);
        setBackground(Color.white);
        setPreferredSize(new Dimension(PHEIGHT, PWIDTH));
        setFocusable(true);
        requestFocus();
        init();
        readyForTermination();
        addMouseListener(new MouseAdapter(){
            @Override
            public void mousePressed(MouseEvent e){
                testPress(e.getX(), e.getY());
            }
        });        
    }
    private void readyForTermination(){
        addKeyListener(new KeyAdapter(){
            @Override
           public void keyPressed(KeyEvent e){
               int keyCode = e.getKeyCode();
               if(keyCode == KeyEvent.VK_ESCAPE ||
                  keyCode == KeyEvent.VK_Q ||
                  keyCode == KeyEvent.VK_END ||
                  keyCode == KeyEvent.VK_ESCAPE && e.isControlDown()){
                   running = false;
               }
           } 
        });
    }
//   @Override
//   public Dimension getPreferredSize(){
//       return new Dimension(PWIDTH, PHEIGHT);
//   }
    private void testPress(int x, int y){
        if(!isPaused && !gameOver){
            
        }
    }   
    private void init(){
        setState(menuState);
    }
    public State getGameState(){
        return gameState;
    }
    public State getMenuState(){
        return menuState;
    }
    @Override
    public void addNotify(){
        super.addNotify();
        startGame();
    }
    public void startGame(){
        if(animator == null || !running){
            animator = new Thread(this);
            animator.start();
        }
    }
    @Override
    public void setState(State s){
        currentState = s;
        currentState.entered();
    }
    public void update(){
        currentState.update();
    }
    public void render(){
        currentState.render();
    }
    public void paintScreen(){
        currentState.paintScreen();
    }
    @Override
    public void run(){
        long beforeTime, afterTime, timeDiff, sleepTime;
        long overSleepTime = 0L;
        int noDelays = 0;
        long excess = 0L;
        
        beforeTime = System.nanoTime();        
        running = true;
        while(running){
            update();
            render();
            paintScreen();
            
            
            afterTime = System.nanoTime();
            timeDiff = beforeTime - afterTime;
            sleepTime = (period - timeDiff) - overSleepTime;
            if(sleepTime > 0)
                try{
                    Thread.sleep(sleepTime/1000000L);
                }catch(InterruptedException e){
                    overSleepTime = System.nanoTime() - afterTime - sleepTime;
            }else{
                overSleepTime = 0L;
                excess -= sleepTime;
                if(++noDelays >= NO_DELAYS_PER_YIELD){
                    Thread.yield();
                    noDelays = 0;
                }
            }
            beforeTime = System.nanoTime();
            int skips = 0;
            while(excess >= period && skips <= MAX_FRAME_SKIPS){
                excess -= period;
                update();
                skips++;
            }            
        }
        System.exit(0);
    }        
    
}
