package com.stikanek.tiles;

import java.awt.image.BufferedImage;

public class Tile {
    private final TileType type;
    private final BufferedImage image;
    
    public Tile(TileType type, BufferedImage image){
        this.type = type;
        this.image = image;
    }
    public enum TileType{
        NONBLOCKING, BLOCKING;
    }
    public BufferedImage getImage(){
        return image;
    }
    public TileType getType(){
        return type;
    }    
}
