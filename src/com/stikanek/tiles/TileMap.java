package com.stikanek.tiles;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import com.stikanek.tiles.Tile.TileType;
import com.stikanek.pictures.Images;

/**
 * The <code>TileMap</code> class enables user to load <code>TileMap</code> configuration file and images for
 * individual Tiles. It has methods for updating current position and for drawing this <code>TileMap</code>. 
 * @author Pavel
 */
public class TileMap {

    private int[][] map;
    private int rowsInMap;
    private int colsInMap;
    private final int tilesInTileset;
    private int tileWidth;
    private final Tile[] tiles;
    private final static int PWIDTH = 320;
    private final static int PHEIGHT = 240;
    private int tileOffsetX;
    private int tilesToDrawX;
    private int currentX, currentY;
    private final int speed;
    private int maxMapMovementToRight;
    private final int maxMapMovementToLeft = 0;
//    private boolean isNearEnd;

    /**
     * Constructs a <code>TileMap</code> with <code>tilesInTileset</code> number of tiles.
     * @param tilesInTileset number of tiles in used TileSet
     */
    public TileMap(int tilesInTileset) {
        this.tilesInTileset = tilesInTileset;
        tiles = new Tile[tilesInTileset];
        tileOffsetX = 0;
        currentX = currentY = 0;
        speed = 10;
    }
    
    /**
     * Returns width of the whole map in pixels.
     * @return the width of this map
     */
    public int getMapWidth() {
        return tileWidth * colsInMap;
    }

    /**
     * Returns whether or not the current position of the map reached its boundaries.
     * @return <code>true</code> if the current position of the map is in its leftmost or rightmost position; false otherwise.
     */
    public boolean isNearEdges() {
        return (currentX <= maxMapMovementToLeft) || (currentX >= maxMapMovementToRight);
    }

    /**
     * Loads TileMap configuration file from the file specified by <code>file</code> String.
     * @param file the name of the file to be read from
     */
    public void loadTileMap(String file) {
        InputStream is = getClass().getResourceAsStream(file);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String delimiter = "\\s+";
        try {
            rowsInMap = Integer.parseInt(br.readLine());
            colsInMap = Integer.parseInt(br.readLine());
            map = new int[rowsInMap][colsInMap];
            for (int row = 0; row < rowsInMap; row++) {
                String input = br.readLine();
                String[] values = input.split(delimiter);

                for (int column = 0; column < colsInMap; column++) {
                    map[row][column] = Integer.parseInt(values[column]);
                }
            }
        } catch (IOException e) {
            System.out.println("IOException");
        }
    }

    /**
     * Loads tile images from the file specified by <code>file</code> String.
     * @param file the name of the file to be read from
     */
    public void loadTiles(String file) {
        Images.setSubdirectoryPath("tilesets/");
        BufferedImage[] tileImages = Images.loadImageStripe(file, tilesInTileset);
        tileWidth = tileImages[0].getWidth();
        tilesToDrawX = PWIDTH / tileWidth + 1;
        for (int i = 0; i < tileImages.length; i++) {
            tiles[i] = new Tile(TileType.BLOCKING, tileImages[i]);
        }
        maxMapMovementToRight = tileWidth * (colsInMap - tilesToDrawX);
    }

    /**
     * Sets the current x-coordinate position of this TileMap to <code>x</code>. The x-coordinate is the value of the 
     * leftmost visible point of the map.
     * 
     * @param x the x coordinate of the left point of this TileMap
     */
    public void update(int x) {
        currentX = (x <= maxMapMovementToLeft) ? maxMapMovementToLeft : x;
        if (x <= maxMapMovementToLeft) {
            currentX = maxMapMovementToLeft;
        } else if (x >= maxMapMovementToRight) {
            currentX = maxMapMovementToRight;
        } else {
            currentX = x;
        }
    }

    /**
     * Draws visible tiles depending on the current value of <code>x</code>.
     * @param g Graphics object used to draw this TileMap
     * @see Graphics2D
     */
    public void draw(Graphics2D g) {
        int currentTile = 0;
        int firstTileToDraw = currentX / tileWidth;
        tileOffsetX = currentX % tileWidth;
        if (firstTileToDraw == colsInMap - tilesToDrawX) {
            for (int column = firstTileToDraw; column < firstTileToDraw + tilesToDrawX; column++, currentTile++) {
                g.drawImage(tiles[map[0][column]].getImage(), -tileOffsetX + currentTile * tileWidth, PHEIGHT - tileWidth, null);
            }
        } else {
            for (int column = firstTileToDraw; column <= firstTileToDraw + tilesToDrawX; column++, currentTile++) {
                g.drawImage(tiles[map[0][column]].getImage(), -tileOffsetX + currentTile * tileWidth, PHEIGHT - tileWidth, null);
            }
        }
    }
}
