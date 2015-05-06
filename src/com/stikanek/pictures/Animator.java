package com.stikanek.pictures;

import java.awt.image.BufferedImage;

public class Animator {
    private BufferedImage[] frames;
    private int currentFrame;
    private int numberOfFrames;
    private int count;
    private int delay;
    private int timesPlayed;
    
    public void setFrames(BufferedImage[] frames){
        this.frames = frames;
        numberOfFrames = frames.length;
    }
    
    public void setFrame(int currentFrame){
        this.currentFrame = currentFrame;
    }
    
    public void setDelay(int delay){
        this.delay = delay;
    }
    
    public void setNumberOfFrames(int numberOfFrames){
        this.numberOfFrames = numberOfFrames;
    }
    
    public void update(){
        if(++count == delay){   //delay bigger than zero assumed
            currentFrame++;
            count = 0;
        }
        if(currentFrame == numberOfFrames - 1){
            currentFrame = 0;
            timesPlayed++;
        }
    }
    
    public boolean isInActiveDamageZone(){
        return currentFrame > 2 && currentFrame < 8;
    }
    
    public void reset(){
        currentFrame = 0;
        timesPlayed = 0;
    }
    
    public BufferedImage getCurrentImage(){
        return frames[currentFrame];
    }
    
    public int getTimesPlayed(){
        return timesPlayed;
    }
    
    public boolean hasPlayedAnimationOnce(){
        return currentFrame == 0 && timesPlayed == 1;
    }
}
