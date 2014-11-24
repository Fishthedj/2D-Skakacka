package com.stikanek.states;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import javax.swing.JButton;
import com.stikanek.pictures.Background;
import com.stikanek.mainclasses.StatePanel;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameState implements State{
    private final StatePanel panel;
    private final int pWidth;
    private final int pHeight;
    private Image dbImage = null;
    private Graphics dbg;
    private final Background mountains;
    private final Background sky;
    private final Background clouds;
    private final Logger logger;
    
    public GameState(StatePanel jpanel, int pWidth, int pHeight, Logger l){
        this.panel = jpanel;
        this.pWidth = pWidth;
        this.pHeight = pHeight;
        this.logger = l;
        mountains = new Background.Builder("mountains.gif").dx(1).build();
        sky = new Background.Builder("sky.gif").build();
        clouds = new Background.Builder("clouds.gif").dx(1.5).build();        
        l.log(Level.INFO,"Images loaded.");
    }
    
    @Override
    public void update(){
        mountains.update();
        sky.update();
        clouds.update();
    }
    @Override
    public void entered(){
        panel.removeAll();
//        panel.revalidate();
        panel.repaint();
//        panel.add(btn);
    }
    @Override
    public void render(){
        if(dbImage == null){
            dbImage = panel.createImage(pWidth, pHeight);
//            dbImage = panel.createImage(panel.getWidth(), panel.getHeight());
        }
        if(dbImage == null){
            logger.severe("dbImage is null");
            return;
        }else{
            dbg = dbImage.getGraphics();
        }
        dbg.setColor(Color.white);
//        dbg.fillRect(0, 0, panel.getWidth(), panel.getHeight());
        dbg.fillRect(0, 0, pWidth, pHeight);
        sky.draw((Graphics2D)dbg);
        clouds.draw((Graphics2D)dbg);
        mountains.draw((Graphics2D)dbg);
        
//        if(gameOver){
//            gameOverMessage(dbg);
//        }    
    }
    @Override
    public void paintScreen(){
        Graphics g;
        try{
            g = panel.getGraphics();
            if(g != null && dbImage != null){
                g.drawImage(dbImage, 0, 0, null);
            }
            if(g != null)
                g.dispose();
        }catch(Exception e){
            logger.log(Level.SEVERE,"Graphics exception {0}",e);
        }
    }
}
