package com.dubhacks.maps_mmo.client;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

public class GamePanel extends JPanel {
    
    private final Game game;

    public GamePanel(Game game) {
        this.game = game;
        game.setPanel(this);
        setFocusable(true);
        addKeyListener(new GameKeyListener(game));
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(3000, 1800);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.game.paint((Graphics2D)g);
    }

}
