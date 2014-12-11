package com.stikanek.tiles;

import java.awt.image.BufferedImage;

/**
 * The <code>Tile</code> class is used for storing information about individual tiles as well as images
 * used while drawing these tiles.
 * @author Pavel
 */
public class Tile {
    private final TileType type;
    private final BufferedImage image;
    
    /**
     * Creates a new <code>Tile</code>
     * @param type type of the <code>Tile</code>
     * @param image image to be used when drawing this <code>Tile</code>
     */
    public Tile(TileType type, BufferedImage image){
        this.type = type;
        this.image = image;
    }

    /**
     * Defines possible types for <code>Tile</code>s. These can be either BLOCKING or NONBLOCKING.
     * BLOCKING tiles could be walked through whereas NONBLOCKING can.
     */
    public enum TileType{
        NONBLOCKING, BLOCKING;
    }

    /**
     * Returns <code>BufferedImage</code> used by this tile when drawn.
     * @return image used when drawing this tile.
     */
    public BufferedImage getImage(){
        return image;
    }

    /**
     * Returns the type of this Tile. This type can be either BLOCKING or NONBLOCKING.
     * @return the type of this tile.
     */
    public TileType getType(){
        return type;
    }    
}
