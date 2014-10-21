package statepanel;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class StateFrame extends JFrame{
    private final JPanel panel;
    
    public StateFrame(){
        panel = new StatePanel();
        add(panel);
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(()->{
                JFrame frame = new StateFrame();
                frame.pack();
                frame.setResizable(false);
                frame.setVisible(true);
                frame.setLocationRelativeTo(null);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);            
        });
    }
}
