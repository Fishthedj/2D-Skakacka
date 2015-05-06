package com.stikanek.gameobjects;

import com.stikanek.collisions.AABB;
import com.stikanek.math.Vec2;
import com.stikanek.pictures.Animator;
import com.stikanek.pictures.Images;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Urzice extends StaticEnemy{
    private final Animator urzaAnimator;
    private final BufferedImage[] standingUrzaFrames;
    
    public Urzice(Vec2 center, Vec2 halfExtent){
        this.center = center;
        this.halfExtent = halfExtent;
        aabb = new AABB(center, halfExtent);
        Images.setSubdirectoryPath("sprites/");
        standingUrzaFrames = Images.loadImageStripe("urzice.gif", 5);
        urzaAnimator = new Animator();
        urzaAnimator.setFrames(standingUrzaFrames);
        urzaAnimator.setDelay(1);
        hitpoints = hitpointsLeft = 60;
    }
    
    @Override
    public void draw(Graphics2D g, Player player){
        Vec2 positionOnScreen = getPositionOnScreen(player.getXOnMap(), player.getYOnMap(), player.getXOnScreen(), player.getYOnScreen());
        g.drawImage(urzaAnimator.getCurrentImage(), positionOnScreen.getX(), positionOnScreen.getY(), null);
    }
    
    @Override
    public Vec2 getCurrentCenterPosition(){
        return center;
    }
    
    @Override
    public void update(){
        urzaAnimator.update();
    }
    
    @Override
    public boolean shouldBeDrawn(int playerXOnMap, int playerYOnMap, int mapWidth){
        return Math.abs(playerXOnMap - (center.getX()) + halfExtent.getX()) <= mapWidth;
    }    
}
