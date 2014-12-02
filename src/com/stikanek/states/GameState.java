package com.stikanek.states;
import com.stikanek.tiles.TileMap;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import com.stikanek.pictures.Background;
import com.stikanek.mainclasses.StatePanel;
import com.stikanek.gameobjects.Player;
import java.awt.event.KeyEvent;

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
    private final TileMap map;
    private final Player player;
    
    public GameState(StatePanel jpanel, int pWidth, int pHeight, Logger l){
        this.panel = jpanel;
        this.pWidth = pWidth;
        this.pHeight = pHeight;
        this.logger = l;
        mountains = new Background.Builder("mountains.gif").build();
        sky = new Background.Builder("sky.gif").build();
        clouds = new Background.Builder("clouds.gif").dx(-0.5).build(); 
        map = new TileMap(5);
        map.loadTileMap("/com/stikanek/map.txt");
        map.loadTiles("grasstileset.gif");
        player = new Player(pWidth / 2, 160, 10, map);
        l.log(Level.INFO,"Images loaded.");
    }
    
    @Override
    public void update(){
        if(!map.isNearEdges())
            mountains.update();
        sky.update();
        clouds.update();
        map.update(player.getXOnMap() - pWidth / 2);
        player.update();
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
        map.draw((Graphics2D)dbg);
        player.draw((Graphics2D)dbg);
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
                g.drawImage(dbImage, 0, 0, pWidth * StatePanel.SCALE, pHeight * StatePanel.SCALE, null);
            }
            if(g != null)
                g.dispose();
        }catch(Exception e){
            logger.log(Level.SEVERE,"Graphics exception {0}",e);
        }
    }
    
    @Override
    public void processKeyPressed(KeyEvent e){
        if(e.getKeyCode() == KeyEvent.VK_RIGHT){
            mountains.setVector(-1.0, 0);
            player.setRight(true);
        }
        if(e.getKeyCode() == KeyEvent.VK_LEFT){
            mountains.setVector(1.0, 0);
            player.setLeft(true);
        }      
    }
    
    @Override
    public void processKeyReleased(KeyEvent e){
        if(e.getKeyCode() == KeyEvent.VK_LEFT){
           player.setLeft(false);
           player.setCurrentState(Player.State.STANDING_LEFT);
           mountains.setVector(0,0);
       }
       if(e.getKeyCode() == KeyEvent.VK_RIGHT){
           player.setRight(false);
           player.setCurrentState(Player.State.STANDING_RIGHT);
           mountains.setVector(0,0);
       }        
    }
}
