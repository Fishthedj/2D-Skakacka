package com.stikanek.gameobjects;

import com.stikanek.math.Vec2;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public abstract class StaticEnemy extends StaticObject implements Enemy{
    protected int hitpoints;
    protected int hitpointsLeft;
    protected boolean isDead;
    
    @Override
    public boolean isDead(){
        return isDead;
    }
    
    @Override
    public void receiveDamage(int damage){
        hitpointsLeft -= damage;
        if(hitpointsLeft <= 0)
            isDead = true;
    }
    
    @Override
    public Rectangle getCollisionRectangle(){
        return new Rectangle(center.getX() - halfExtent.getX(), center.getY() - halfExtent.getY(), 2 * halfExtent.getX(), 2 * halfExtent.getY());
    }
    
    public Vec2 getPositionOnScreen(int playerXOnMap, int playerYOnMap, int playerXOnScreen, int playerYOnScreen){
        int xDifference = (center.getX() - halfExtent.getX()) - playerXOnMap;
        int yDifference = (center.getY() - halfExtent.getY()) - playerYOnMap; 
        return new Vec2(playerXOnScreen + xDifference, playerYOnScreen + yDifference);
    }
    
//    public abstract void draw(Graphics2D g, Player player);
}
