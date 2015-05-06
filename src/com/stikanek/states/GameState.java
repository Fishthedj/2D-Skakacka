package com.stikanek.states;
import com.stikanek.tiles.TileMap;
import com.stikanek.math.Vec2;
import com.stikanek.collisions.Collisions;
import com.stikanek.gameobjects.Enemy;
import com.stikanek.gameobjects.MovingObject;
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
import com.stikanek.gameobjects.Urzice;
import com.stikanek.gravity.Gravity;
import com.stikanek.pictures.Images;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author Pavel
 */
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
    private final Collisions collisions;
    private final ArrayList<MovingObject> movingObjects;
    private final Gravity gravity;
    private final ArrayList<Enemy> enemies;
    private final ArrayList<Sign> signs;
    private final Urzice urzice;
    
    public GameState(StatePanel jpanel, int pWidth, int pHeight, Logger l){
        this.panel = jpanel;
        this.pWidth = pWidth;
        this.pHeight = pHeight;
        this.logger = l;
        mountains = new Background.Builder("mountains.gif").build();
        sky = new Background.Builder("sky.gif").build();
        clouds = new Background.Builder("clouds.gif").dx(-1.0).build(); 
        map = new TileMap(5, pHeight);
        map.loadTileMap("/com/stikanek/map.txt");
        map.loadTiles("grasstileset.gif");
        movingObjects = new ArrayList<>();
        player = new Player(pWidth / 2, 290, 8, map.getMapWidth(), map.getMapHeight(), pHeight);
        movingObjects.add(player);
        collisions = new Collisions(map);
        gravity = new Gravity();
        l.log(Level.INFO,"Images loaded.");
        enemies = new ArrayList<>();
        urzice = new Urzice(new Vec2(pWidth / 2 + 150, 296), new Vec2(43,34));
        enemies.add(urzice);
        signs = new ArrayList<>();
        Images.setSubdirectoryPath("sprites/");
    }
    
    @Override
    public void update(){
        sky.update();
        clouds.update();
        gravity.applyGravity(movingObjects);
        Vec2 maximumMovement = new Vec2(collisions.getMaximumMovement(player));
        player.update(maximumMovement);//parameters providing maximum movement for x and y coordinate
        map.update(player.getXOnMap() - pWidth / 2, player.getYOnMap());
        //
        if(player.isOnGround(maximumMovement)){
            player.setDirection(new Vec2(player.getDirection().getX(), 0));
        }
        if(!map.isNearEdgesHorizontally() && maximumMovement.getX() != 0 && !player.isAttacking())
            mountains.update();        
        int playerXOnMap = player.getXOnMap();
        int playerYOnMap = player.getYOnMap();
        for(Iterator<Enemy> iterator = enemies.iterator(); iterator.hasNext();){
            Enemy enemy = iterator.next();
            if(enemy.isDead() && player.hasPlayedAttackAnimationOnce())
                iterator.remove();
        }
        enemies.stream().filter((e) -> (e.shouldBeDrawn(playerXOnMap, playerYOnMap, map.getMapWidth()))).forEach((e) -> {
            e.update();
            if(player.canDealDamage() && player.getAttackingRectangle().intersects(e.getCollisionRectangle())){
                signs.add(new DamageSign(player.getXOnScreen() +50, player.getYOnScreen() - 10, Images.loadImage("20.gif"), 20));
                player.setCanDealDamage(false);
                e.receiveDamage(player.dealDamage());
            }
        });
        signs.stream().forEach((s) -> {s.update();});
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
//        player.drawAttackingRectangle((Graphics2D)dbg);
        player.draw((Graphics2D)dbg);
        int playerXOnMap = player.getXOnMap();
        int playerYOnMap = player.getYOnMap();        
        enemies.stream().filter((e) -> (e.shouldBeDrawn(playerXOnMap, playerYOnMap, map.getMapWidth()))).forEach((e) -> {
            e.draw((Graphics2D)dbg, player);
        });
//        signs.stream().filter((s) -> s.shouldBeShown()).forEach((s) -> {s.draw((Graphics2D)dbg);});
        signs.stream().filter((s) -> (s.shouldBeShown())).forEach((s) -> {
            s.draw((Graphics2D)dbg);
        });
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
        if(e.getKeyCode() == KeyEvent.VK_UP){
            player.jump();
        }
        if(e.getKeyCode() == KeyEvent.VK_DOWN)
            player.setDown(true);
        if(e.getKeyCode() == KeyEvent.VK_SPACE)
            player.attack();
    }
    
    @Override
    public void processKeyReleased(KeyEvent e){
        if(e.getKeyCode() == KeyEvent.VK_LEFT){
           player.setLeft(false);
           mountains.setVector(0,0);
       }
       if(e.getKeyCode() == KeyEvent.VK_RIGHT){
           player.setRight(false);
           mountains.setVector(0,0);
       }
        if(e.getKeyCode() == KeyEvent.VK_UP)
            player.setUp(false);
        if(e.getKeyCode() == KeyEvent.VK_DOWN)
            player.setDown(false);       
    }
    
    abstract class Sign{
        private int xOnScreen;
        private int yOnScreen;
        private BufferedImage image;
        private int count;
        private boolean show = true;
        
        Sign(int xOnScreen, int yOnScreen, BufferedImage image, int count){
            this.xOnScreen = xOnScreen;
            this.yOnScreen = yOnScreen;
            this.image = image;
            this.count = count;
        }
        
        void update(){
            count--;
            yOnScreen -= 2;
        }
        
        boolean shouldBeShown(){
            return count >= 0;
        }
        
        void draw(Graphics2D g){
            g.drawImage(image, xOnScreen, yOnScreen, 20, 20, null);
        }
    }
    
    class DamageSign extends Sign{
        public DamageSign(int xOnScreen, int yOnScreen, BufferedImage image, int count){
            super(xOnScreen, yOnScreen, image, count);
        }
    }
}
