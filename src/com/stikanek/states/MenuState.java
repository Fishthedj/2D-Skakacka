package com.stikanek.states;
import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.Color;
import com.stikanek.mainclasses.StatePanel;
//import java.awt.event.ActionListener;
//import java.awt.event.ActionEvent;

public class MenuState implements State{
    private final StatePanel panel;
    private final JButton btn = new JButton();
    
    public MenuState(StatePanel jpanel){
        this.panel = jpanel;
        btn.addActionListener(e->panel.setState(panel.getGameState()));
    }
    
    @Override
    public void update(){}
    @Override
    public void entered(){
        panel.removeAll();
        panel.revalidate();
        panel.repaint(); 
        panel.setBackground(Color.blue);
        panel.add(btn);        
    }
    @Override
    public void render(){
    }
    @Override
    public void paintScreen(){
    }
}
