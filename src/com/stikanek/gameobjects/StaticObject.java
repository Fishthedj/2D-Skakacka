package com.stikanek.gameobjects;

public abstract class StaticObject extends GameObject{
    public boolean shouldBeDrawn(int playerXOnMap, int playerYOnMap, int mapWidth){
        return Math.abs(playerXOnMap - (center.getX()) + halfExtent.getX()) <= mapWidth;
    }    
}
