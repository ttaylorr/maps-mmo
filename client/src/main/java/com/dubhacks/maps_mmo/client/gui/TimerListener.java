package com.dubhacks.maps_mmo.client.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.dubhacks.maps_mmo.client.Game;

public class TimerListener implements ActionListener {
    private final Game game;
    private final GamePanel gamePanel;

    public TimerListener(Game game, GamePanel gamePanel) {
        this.game = game;
        this.gamePanel = gamePanel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        game.tick();
        gamePanel.repaint();
    }
}
