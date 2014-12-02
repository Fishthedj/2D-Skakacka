package com.stikanek.gameobjects;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import com.stikanek.tiles.TileMap;
import com.stikanek.pictures.Animator;
import com.stikanek.pictures.Images;
import com.stikanek.mainclasses.StatePanel;

public class Player {

    public enum State{
        STANDING_LEFT, STANDING_RIGHT, STARTED_RUNNING_RIGHT, STARTED_RUNNING_LEFT, RUNNING_RIGHT, RUNNING_LEFT
    }
    private int xOnScreen;
    private int xOnMap;
    private int y;
    private int dx;
    private int dy;
    private final int speed;
    private boolean left;
    private boolean right;
    private boolean up;
    private boolean down;
    private final TileMap map;
    private final Animator runningRightAnimator;
    private final Animator runningLeftAnimator;
    private final BufferedImage[] runningRightFrames;
    private final BufferedImage[] runningLeftFrames;
    private final BufferedImage standingRightImage;
    private final BufferedImage standingLeftImage;
    private final BufferedImage startedRunningRightImage;
    private final BufferedImage startedRunningLeftImage;
    private State currentState;

    public Player(int xOnScreen, int yOnScreen, int speed, TileMap map) {
        this.xOnScreen = xOnScreen;
        xOnMap = xOnScreen;
        this.y = yOnScreen;
        this.speed = speed;
        this.map = map;
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
        currentState = State.STANDING_RIGHT;
    }
    public void setCurrentState(State state){
        currentState = state;
    }
    public void setLeft(boolean b) {
        left = b;
    }
    public void setRight(boolean b){
        right = b;
    }
    public int getXOnMap(){
        return xOnMap;
    }

    public void update() {
        int mapWidth = map.getMapWidth();
        boolean isNearLeftEdge = xOnMap <= StatePanel.PWIDTH / 2;
        boolean willBeNearLeftEdge = xOnMap - speed <= StatePanel.PWIDTH / 2;
        boolean isNearRightEdge = xOnMap >= mapWidth - StatePanel.PWIDTH / 2;
        if(left){
            if(isNearLeftEdge || willBeNearLeftEdge){
//                xOnScreen = xOnMap = (xOnMap - speed >= 0)? xOnMap - speed : 0;
                xOnScreen = (xOnScreen - speed >= 0)? xOnScreen - speed : 0;
                xOnMap = (xOnMap - speed >= 0)? xOnMap - speed : 0;
            }else if(isNearRightEdge){
                xOnMap -= speed;
                xOnScreen -= speed;
            }else{
                xOnMap -= speed;
            }
            switch(currentState){
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
        if(right){
            if(isNearLeftEdge){
                xOnScreen += speed;
                xOnMap += speed;
            }else if(isNearRightEdge){
                xOnMap = (xOnMap + speed >= mapWidth)? mapWidth : xOnMap + speed;
                xOnScreen = (xOnScreen + speed >= StatePanel.PWIDTH)? StatePanel.PWIDTH : xOnScreen + speed;
            }else{
                xOnMap += speed;
            }
            switch(currentState){
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
    }
    
    public void draw(Graphics2D g){
        switch(currentState){
            case STANDING_RIGHT:
                g.drawImage(standingRightImage, xOnScreen, y, null);
                break;
            case STANDING_LEFT:
                g.drawImage(standingLeftImage, xOnScreen, y, null);
                break;
            case STARTED_RUNNING_LEFT:
                g.drawImage(startedRunningLeftImage, xOnScreen, y, null);
                break;
            case STARTED_RUNNING_RIGHT:
                g.drawImage(startedRunningRightImage, xOnScreen, y, null);                
                break;
            case RUNNING_LEFT:
                g.drawImage(runningLeftAnimator.getCurrentImage(), xOnScreen, y, null);
                break;
            case RUNNING_RIGHT:
                g.drawImage(runningRightAnimator.getCurrentImage(), xOnScreen, y, null);
                break;
        }
    }
}