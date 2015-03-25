package com.stikanek.tiles;

import com.stikanek.math.Vec2;
import com.stikanek.pictures.Images;
import com.stikanek.tiles.Tile.TileType;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

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
    private int tileHeight;
    private final Tile[] tiles;
    private final static int PWIDTH = 320;
    private final static int PHEIGHT = 240;
    private int tileOffsetX;
    private int tilesToDrawHorizontally;
    private int tilesToDrawVertically;  
    private int currentX, currentY;
    private final int speed;
    private final int screenHeight;
    private int yTreshold;
    private int maxMapMovementToRight;
    private final int maxMapMovementToLeft = 0;

    /**
     * Constructs a <code>TileMap</code> with <code>tilesInTileset</code> number of tiles.
     * @param tilesInTileset number of tiles in used TileSet
     * @param screenHeight height of screen displayed
     */
    public TileMap(int tilesInTileset, int screenHeight) {
        this.tilesInTileset = tilesInTileset;
        tiles = new Tile[tilesInTileset];
        tileOffsetX = 0;
        currentX = currentY = 0;
        speed = 10;
        this.screenHeight = screenHeight;
    }
    
    /**
     * Returns width of the whole map in pixels.
     * @return the width of this map
     */
    public int getMapWidth() {
        return tileWidth * colsInMap;
    }
    
    /**
     * Return height of the whole map in pixels.
     * @return 
     */
    public int getMapHeight(){
        return tileHeight * rowsInMap;
    }

    /**
     * Returns whether or not the current position of the map reached its boundaries.
     * @return <code>true</code> if the current position of the map is in its leftmost or rightmost position; false otherwise.
     */
    public boolean isNearEdgesHorizontally() {
        return (currentX <= maxMapMovementToLeft) || (currentX >= maxMapMovementToRight);
    }
    
//    public boolean isNearEdgesVertically(){
//        return (currentY >= maxMapMovementDown) || (currentY <= )
//    }

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
        tileHeight = tileImages[0].getHeight();
        yTreshold = tileHeight * rowsInMap - (int)((double)2 / 3 * screenHeight);
        tilesToDrawHorizontally = PWIDTH / tileWidth + 1;
        tilesToDrawVertically = PHEIGHT / tileHeight + 1;
        for (int i = 0; i < tileImages.length; i++) {
            tiles[i] = new Tile(TileType.BLOCKING, tileImages[i]);
        }
        maxMapMovementToRight = tileWidth * (colsInMap - tilesToDrawHorizontally);

    }

    /**
     * Sets the current x-coordinate position of this TileMap to <code>x</code>. The x-coordinate is the value of the 
     * leftmost visible point of the map.
     * 
     * @param x the x coordinate of the left point of this TileMap
     * @param y the y coordinate of the left point of this TileMap
     */
    public void update(int x, int y) {
        currentX = (x <= maxMapMovementToLeft) ? maxMapMovementToLeft : x;
        if (x <= maxMapMovementToLeft) {
            currentX = maxMapMovementToLeft;
        } else if (x >= maxMapMovementToRight) {
            currentX = maxMapMovementToRight;
        } else {
            currentX = x;
        }
        currentY = y; //currentY == player.getYOnMap()
    }
    
    public int worldCoordToTileX(int x){
        return x / tileWidth;
    }
    
    public int worldCoordToTileY(int y){
        return y / tileHeight;
    }
    
    public int tileCoordToWorldX(int j){
        //returns x coordinate of tile's centre
        return tileWidth * j + tileWidth / 2;
    }
    
    public int tileCoordToWorldY(int i){
        //returns y coordinate of tile's centre
        return tileHeight * i + tileHeight / 2;
    }
    
    public TileType getTileType(int row, int column){
        int type = map[row][column];
        return type == -1? TileType.NONBLOCKING : tiles[map[row][column]].getType();
    }
    
    public int getTileWidth(){
        return tileWidth;
    }
    
    public int getTileHeight(){
        return tileHeight;
    }

    /**
     * Draws visible tiles depending on the current value of <code>x</code>.
     * @param g Graphics object used to draw this TileMap
     * @see Graphics2D
     */
    public void draw(Graphics2D g) {
        int additionalTilesToDraw;
        int tileOffsetY;
        if(currentY >= yTreshold){
            for(int row = 0; row < tilesToDrawVertically; row++ ){
                int currentTile = 0;
                int firstTileToDrawHorizontally = currentX / tileWidth;
                tileOffsetX = currentX % tileWidth;
                if (firstTileToDrawHorizontally == colsInMap - tilesToDrawHorizontally) {
                    for (int column = firstTileToDrawHorizontally; column < firstTileToDrawHorizontally + tilesToDrawHorizontally; column++, currentTile++) {
                        if(map[rowsInMap - row - 1][column] != -1){
                            g.drawImage(tiles[map[rowsInMap - row - 1][column]].getImage(), -tileOffsetX + currentTile * tileWidth, PHEIGHT - (row + 1) * tileHeight, null);
                        }
                    }
                } else {
                    for (int column = firstTileToDrawHorizontally; column <= firstTileToDrawHorizontally + tilesToDrawHorizontally; column++, currentTile++) {
                        if(map[rowsInMap - row - 1][column] != -1){
                            g.drawImage(tiles[map[rowsInMap - row - 1][column]].getImage(), -tileOffsetX + currentTile * tileWidth, PHEIGHT - (row + 1) * tileHeight, null);
                        }    
                    }
                }
            }
        }else{
            additionalTilesToDraw = (yTreshold - currentY) / tileHeight + 1;
            tileOffsetY = (currentY - yTreshold) % tileHeight;//negative value
            for(int row = 0; row < tilesToDrawVertically; row++ ){
//                System.out.println(additionalTilesToDraw);
//                System.out.println("vzorec: " + (rowsInMap - row - additionalTilesToDraw));
                int currentTile = 0;
                int firstTileToDrawHorizontally = currentX / tileWidth;
                tileOffsetX = currentX % tileWidth;
                if (firstTileToDrawHorizontally == colsInMap - tilesToDrawHorizontally) {
                    for (int column = firstTileToDrawHorizontally; column < firstTileToDrawHorizontally + tilesToDrawHorizontally; column++, currentTile++) {
                        if(map[rowsInMap - row - additionalTilesToDraw][column] != -1){
                            g.drawImage(tiles[map[rowsInMap - row - additionalTilesToDraw][column]].getImage(), -tileOffsetX + currentTile * tileWidth, PHEIGHT - (row + 1) * tileHeight - tileOffsetY, null);
                        }
                    }
                } else {
                    for (int column = firstTileToDrawHorizontally; column <= firstTileToDrawHorizontally + tilesToDrawHorizontally; column++, currentTile++) {
                        if(map[rowsInMap - row - additionalTilesToDraw][column] != -1){
                            g.drawImage(tiles[map[rowsInMap - row - additionalTilesToDraw][column]].getImage(), -tileOffsetX + currentTile * tileWidth, PHEIGHT - (row + 1) * tileHeight - tileOffsetY, null);
                        }
                    }
                } 
            }
        }
    }
}
