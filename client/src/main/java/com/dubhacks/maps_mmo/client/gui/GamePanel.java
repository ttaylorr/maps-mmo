package com.dubhacks.maps_mmo.client.gui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JPanel;

import com.dubhacks.maps_mmo.client.Game;

public class GamePanel extends JPanel {

    private final Game game;

    /**
     * Create the panel.
     */
    public GamePanel(Game game) {
        this.game = game;

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                requestFocusInWindow();
            }
        });

        setDoubleBuffered(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.game.paint((Graphics2D)g);
    }

}
