package statepanel;
//Class for loading single images and image stripes

import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Images {
    private final static String IMAGE_DIR = "/Images/";
    private String directoryPath;
    private final GraphicsConfiguration gc;
    
    public Images(){
        gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
    }
    
    public void setSubdirectoryPath(String subdirectory){
        this.directoryPath = IMAGE_DIR + subdirectory;
    }
    public BufferedImage loadImage(String fileName){
        try{
            BufferedImage image = ImageIO.read(getClass().getResource(directoryPath + fileName));
            int transparency = image.getColorModel().getTransparency();
            BufferedImage copy = gc.createCompatibleImage(image.getWidth(), image.getHeight(), transparency);
            Graphics2D g2d = copy.createGraphics();
            g2d.drawImage(image,0,0,null);
            g2d.dispose();
            return copy;
        }catch(IOException e){
            System.out.println("Error loading image: " + directoryPath + "/" + fileName);
            return null;
        }
    }
    public BufferedImage[] loadImageStripe(String filename, int numberOfImages){
        BufferedImage strip;
        if(numberOfImages <= 0){
            System.out.println("numberOfImages <= 0");
            return null;
        }
        if((strip = loadImage(filename)) == null){
            System.out.println("filename is null");
            return null;
        }
        int frameWidth = strip.getWidth()/numberOfImages;
        int frameHeight = strip.getHeight();
        int transparency = strip.getColorModel().getTransparency();
        BufferedImage frames[] = new BufferedImage[numberOfImages];
        Graphics2D g2d;
        for(int i = 0; i < numberOfImages; i++){
            frames[i] = gc.createCompatibleImage(frameWidth, frameHeight, transparency);
            g2d = frames[i].createGraphics();
            g2d.drawImage(strip.getSubimage(i * frameWidth, 0, frameWidth, frameHeight),0,0,frameWidth, frameHeight, null);
//            g2d.drawImage(strip,0,0,frameWidth,frameHeight,i*frameWidth,0,(i*frameWidth)+frameWidth,frameHeight,null);
            g2d.dispose();
        }
        return frames;
    }
}