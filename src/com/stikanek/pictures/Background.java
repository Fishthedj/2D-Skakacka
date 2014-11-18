package com.stikanek.pictures;

import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import com.stikanek.mainclasses.StatePanel;

public class Background {
    private final BufferedImage image;
    private double x;
    private double y;
    private double dx;
    private double dy;
    private int width;
    private int height;
    private double xscale;
    private double yscale;
    
    private Background(Builder builder){
        this.xscale = builder.xscale;
        this.yscale = builder.yscale;
        this.dx = builder.dx;
        this.dy = builder.dy;
        Images.setSubdirectoryPath("Backgrounds/");
        image = Images.loadImage(builder.filename);
        width = image.getWidth();
        height = image.getHeight();
    }
    
    public void setPosition(double x, double y){
        this.x = (x * xscale) % width;
        this.y = (y * yscale) % height;
    }
    public void setVector(double dx, double dy) {
            this.dx = dx;
            this.dy = dy;
    }
    public void setScale(double xscale, double yscale) {
            this.xscale = xscale;
            this.yscale = yscale;
    }   
    public void setDimension(int width, int height){
        this.width = width;
        this.height = height;
    }
    public double getX(){
        return x;
    }
    public double getY(){
        return y;
    }
    public void update() {
            x += dx;
            while(x <= -width) x += width;
            while(x >= width) x -= width;
            y += dy;
            while(y <= -height) y += height;
            while(y >= height) y -= height;
    }    
    public void draw(Graphics2D g) {
            g.drawImage(image, (int)x, (int)y, null);
            if(x < 0) {
                    g.drawImage(image, (int)x + StatePanel.PWIDTH, (int)y, null);
            }
            if(x > 0) {
                    g.drawImage(image, (int)x - StatePanel.PWIDTH, (int)y, null);
            }
            if(y < 0) {
                    g.drawImage(image, (int)x, (int)y + StatePanel.PHEIGHT, null);
            }
            if(y > 0) {
                    g.drawImage(image, (int)x, (int)y - StatePanel.PHEIGHT, null);
            }
    }    
    
    public static class Builder{
        //required parameters
        private final String filename;
        //optional parameters
        private double xscale = 0.1;
        private double yscale = 0.1;
        private double dx = 0;
        private double dy = 0;
        
        public Builder(String filename){
            this.filename = filename;
        }
        public Builder xscale(int value){
            xscale = value;
            return this;
        }
        public Builder yscale(int value){
            yscale = value;
            return this;
        }
        public Builder dx(double value){
            dx = value;
            return this;
        }
        public Builder dy(double value){
            dy = value;
            return this;
        }        
        public Background build(){
            return new Background(this);
        }
    }
}