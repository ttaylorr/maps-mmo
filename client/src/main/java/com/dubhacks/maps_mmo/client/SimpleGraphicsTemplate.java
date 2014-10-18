package com.dubhacks.maps_mmo.client;

import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author irving
 */
public class SimpleGraphicsTemplate {
    
    public static void main(String[] args) {
        JFrame frame = new JFrame("Window Caption");
        
        JPanel panel = new CustomPanel();
        
        frame.setContentPane(panel);
        
        frame.pack();
        
        frame.setVisible(true);
    }
    
    private static class CustomPanel extends JPanel {
        
        public CustomPanel() {
            
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(250,200);
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            g.drawString("This is my custom Panel!", 10, 20);
            g.drawLine(100, 100, 200, 100);
        }  
        
    }
    
}
