package com.stikanek.tiles;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import com.stikanek.tiles.Tile.TileType;
import com.stikanek.pictures.Images;

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
    private boolean left;
    private boolean right;
    private final int speed;
    private int maxMapMovementToRight;
    private final int maxMapMovementToLeft = 0;
//    private boolean isNearEnd;

    public TileMap(int tilesInTileset) {
        this.tilesInTileset = tilesInTileset;
        tiles = new Tile[tilesInTileset];
        tileOffsetX = 0;
        currentX = currentY = 0;
        speed = 10;
    }

    public void setLeft(boolean b) {
        left = b;
    }

    public void setRight(boolean b) {
        right = b;
    }

    public int getMapWidth() {
        return tileWidth * colsInMap;
    }

    public boolean isNearEdges() {
        return (currentX <= maxMapMovementToLeft) || (currentX >= maxMapMovementToRight);
    }

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
