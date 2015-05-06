package com.stikanek.gameobjects;

import java.awt.Graphics2D;
import java.awt.Rectangle;

public interface Enemy {
//marker interface for enemies  
    public abstract void update();
    
    public abstract boolean shouldBeDrawn(int playerXOnMap, int playerYOnMap, int mapWidth);
    
    public abstract void draw(Graphics2D g, Player player);
    
    public abstract boolean isDead();
    
    public abstract Rectangle getCollisionRectangle();
    
    public abstract void receiveDamage(int damage);
}
