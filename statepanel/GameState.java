package statepanel;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

public class GameState implements State{
    private final StatePanel panel;
    private final int pWidth;
    private final int pHeight;
    private final JButton btn = new JButton();
    private Image dbImage = null;
    private Graphics dbg;
    
    public GameState(StatePanel jpanel, int pWidth, int pHeight){
        this.panel = jpanel;
        this.pWidth = pWidth;
        this.pHeight = pHeight;
        btn.addActionListener(e->panel.setState(panel.getMenuState()));
    }
    
    @Override
    public void update(){}
    @Override
    public void entered(){
        panel.removeAll();
//        panel.revalidate();
        panel.repaint();
//        panel.add(btn);
    }
    @Override
    public void render(){
        if(dbImage == null){
//            dbImage = panel.createImage(pWidth, pHeight);
            dbImage = panel.createImage(panel.getWidth(), panel.getHeight());
        }
        if(dbImage == null){
            System.out.println("dbImage is null");
            return;
        }else{
            dbg = dbImage.getGraphics();
        }
        dbg.setColor(Color.white);
        dbg.fillRect(0, 0, panel.getWidth(), panel.getHeight());
        
//        if(gameOver){
//            gameOverMessage(dbg);
//        }    
    }
    @Override
    public void paintScreen(){
        Graphics g;
        try{
            g = panel.getGraphics();
            if(g != null && dbImage != null){
                g.drawImage(dbImage, 0, 0, null);
            }
            if(g != null)
                g.dispose();
        }catch(Exception e){
            System.out.println("Graphics exception " + e);
        }
    }
}
