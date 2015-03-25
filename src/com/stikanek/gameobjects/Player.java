package com.stikanek.gameobjects;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import com.stikanek.tiles.TileMap;
import com.stikanek.pictures.Animator;
import com.stikanek.pictures.Images;
import com.stikanek.mainclasses.StatePanel;
import com.stikanek.collisions.AABB;
import com.stikanek.math.Vec2;
import java.util.ArrayList;
import java.util.Arrays;

public class Player extends MovingObject {

    public enum State {

        STANDING_LEFT, STANDING_RIGHT, STARTED_RUNNING_RIGHT, STARTED_RUNNING_LEFT, RUNNING_RIGHT, RUNNING_LEFT
    }
    private int xOnMap;
    private int yOnMap;
    private boolean left;
    private boolean right;
    private boolean up;
    private boolean down;
    private final int mapWidth;
    private final int mapHeight;
    private final int yTreshold;
    private final int screenHeight;
    private final int width;
    private final int xHalfExtent;
    private final int yHalfExtent;
//    private final TileMap map;
    private final Animator runningRightAnimator;
    private final Animator runningLeftAnimator;
    private final BufferedImage[] runningRightFrames;
    private final BufferedImage[] runningLeftFrames;
    private final BufferedImage standingRightImage;
    private final BufferedImage standingLeftImage;
    private final BufferedImage startedRunningRightImage;
    private final BufferedImage startedRunningLeftImage;
    private State currentState;

    public Player(int xOnMap, int yOnMap, int speed, int mapWidth, int mapHeight, int screenHeight) {
        //TODO:center from xOnMap, yOnMap, halfExtent from Image size
        this.xOnMap = xOnMap;//upper left corner
        this.yOnMap = yOnMap;//upper left corner
        direction = new Vec2(speed, speed);
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
        this.screenHeight = screenHeight;
        yTreshold = (int)((double)1 / 3 * screenHeight);
        Images.setSubdirectoryPath("sprites/");
        runningRightFrames = Images.loadImageStripe("runningRight.gif", 13);
        runningLeftFrames = Images.loadImageStripe("runningLeft.gif", 13);
        runningRightAnimator = new Animator();
        runningRightAnimator.setFrames(runningRightFrames);
        runningRightAnimator.setDelay(1);
        runningLeftAnimator = new Animator();
        runningLeftAnimator.setFrames(runningLeftFrames);
        runningLeftAnimator.setDelay(1);
        standingRightImage = Images.loadImage("standingRight.gif");
        standingLeftImage = Images.loadImage("standingLeft.gif");
        startedRunningRightImage = Images.loadImage("startedRunningRight.gif");
        startedRunningLeftImage = Images.loadImage("startedRunningLeft.gif");
        width = getMaxWidthFromImages();
        currentState = State.STANDING_RIGHT;
        xHalfExtent = standingRightImage.getWidth() / 2;
        yHalfExtent = standingRightImage.getHeight() / 2;
        center = new Vec2(xOnMap + xHalfExtent, yOnMap + yHalfExtent);
        aabb = new AABB(center, new Vec2(xHalfExtent, yHalfExtent));
    }

    public void setCurrentState(State state) {
        currentState = state;
    }

    public void setLeft(boolean b) {
        left = b;
    }

    public void setRight(boolean b) {
        right = b;
    }

    public void setUp(boolean b){
        up = b;
    }
    
    public void setDown(boolean b){
        down = b;
    }
            
    public int getXOnMap() {
        return xOnMap;
    }
    
    public int getYOnMap(){
        return yOnMap;
    }
    
    public void setXOnMap(int x){
        xOnMap = x;
    }
    
    public int getXOnScreen(){
        int screenWidth = StatePanel.PWIDTH;
        if(xOnMap < screenWidth / 2)
            return xOnMap;
        else if(xOnMap > mapWidth - screenWidth / 2){
            return screenWidth - mapWidth + xOnMap;
        }
        else
            return screenWidth / 2;
    }
    
    public int getYOnScreen(){
        int yOnScreen = screenHeight - (mapHeight - yOnMap);
        return yOnScreen  >= yTreshold ? yOnScreen : yTreshold;
    }
    
    @Override
    public Vec2 getPredictedCenterPosition(){
        Vec2 predictedPosition = new Vec2(center);
        Vec2 currentDirection = getCurrentDirection();
//        if(right)
//            predictedPosition.addXTo(currentDirection.getX());
//        if(left)
//            predictedPosition.addXTo(currentDirection.getX());
//        if(up)
//            predictedPosition.addYTo(currentDirection.getY());
//        if(down)
//            predictedPosition.addYTo(currentDirection.getY());
        predictedPosition.addTo(currentDirection);
        return predictedPosition;
    }
    
    @Override
    public Vec2 getCurrentDirection(){
        Vec2 currentDirection = new Vec2();
        if(right)
            currentDirection.setX(direction.getX());
        if(left)
            currentDirection.setX(-direction.getX());
        if(up)
            currentDirection.setY(-direction.getY());
        if(down)
            currentDirection.setY(direction.getY());
        return currentDirection;
    }
    
    @Override
    public Vec2 getCurrentCenterPosition(){
        return new Vec2(center);
    }
    
    public void update(Vec2 maximumMovement) {
        boolean isNearLeftEdge = xOnMap <= StatePanel.PWIDTH / 2;
        boolean isNearRightEdge = xOnMap >= mapWidth - StatePanel.PWIDTH / 2;
        if (left) {
            if (isNearLeftEdge) {
                xOnMap = (xOnMap + maximumMovement.getX() >= 0) ? xOnMap + maximumMovement.getX() : 0;
            } else if (isNearRightEdge) {
                xOnMap += maximumMovement.getX();
            }else{
                xOnMap += maximumMovement.getX();
            }
            switch (currentState) {
                case STANDING_RIGHT:
                    currentState = State.STANDING_LEFT;
                    break;
                case STANDING_LEFT:
                    currentState = State.STARTED_RUNNING_LEFT;
                    runningLeftAnimator.reset();
                    break;
                case STARTED_RUNNING_LEFT:
                    currentState = State.RUNNING_LEFT;
                    break;
                case RUNNING_LEFT:
                    runningLeftAnimator.update();
                    break;
                case RUNNING_RIGHT:
                    currentState = State.STANDING_LEFT;
                    break;
            }
        }

        if (right) {
            if (isNearLeftEdge) {
                xOnMap += maximumMovement.getX();
            }else if(isNearRightEdge) {
                xOnMap = (xOnMap + maximumMovement.getX() >= mapWidth)? mapWidth : xOnMap + maximumMovement.getX();
            } else {
                xOnMap += maximumMovement.getX();
            }
            switch (currentState) {
                case STANDING_LEFT:
                    currentState = State.STANDING_RIGHT;
                    break;
                case STANDING_RIGHT:
                    currentState = State.STARTED_RUNNING_RIGHT;
                    runningLeftAnimator.reset();
                    break;
                case STARTED_RUNNING_RIGHT:
                    currentState = State.RUNNING_RIGHT;
                    break;
                case RUNNING_RIGHT:
                    runningRightAnimator.update();
                    break;
                case RUNNING_LEFT:
                    currentState = State.STANDING_RIGHT;
                    break;
            }
        }
        if(up){
            yOnMap += maximumMovement.getY();
        }
        
        if(down){
            yOnMap += maximumMovement.getY();
        }
        //update center and AABB coordinates
        center.setXY(xOnMap + xHalfExtent, yOnMap + yHalfExtent);
        aabb.setCenter(center);
//        System.out.println(center.getX());   //***************
//        System.out.println(center.getY());  //****************
    }

    public void draw(Graphics2D g) {
        switch (currentState) {
            case STANDING_RIGHT:
                g.drawImage(standingRightImage, getXOnScreen(), getYOnScreen(), null);
                break;
            case STANDING_LEFT:
                g.drawImage(standingLeftImage, getXOnScreen(), getYOnScreen(), null);
                break;
            case STARTED_RUNNING_LEFT:
                g.drawImage(startedRunningLeftImage, getXOnScreen(), getYOnScreen(), null);
                break;
            case STARTED_RUNNING_RIGHT:
                g.drawImage(startedRunningRightImage, getXOnScreen(), getYOnScreen(), null);                
                break;
            case RUNNING_LEFT:
                g.drawImage(runningLeftAnimator.getCurrentImage(), getXOnScreen(), getYOnScreen(), null);
                break;
            case RUNNING_RIGHT:
                g.drawImage(runningRightAnimator.getCurrentImage(), getXOnScreen(), getYOnScreen(), null);
                break;
        }
    }

    /**
     * Returns max width of player from all images used to represent him.
     */
    private int getMaxWidthFromImages() {
        ArrayList<BufferedImage> allImages = new ArrayList<BufferedImage>();
        allImages.add(standingLeftImage);
        allImages.add(standingRightImage);
        allImages.add(startedRunningLeftImage);
        allImages.add(startedRunningRightImage);
        allImages.addAll(Arrays.asList(runningLeftFrames));
        allImages.addAll(Arrays.asList(runningRightFrames));
        int maxWidth = 0;
        for (BufferedImage image : allImages) 
            if (maxWidth < image.getWidth()) 
                maxWidth = image.getWidth();

        return maxWidth;
    }

}
