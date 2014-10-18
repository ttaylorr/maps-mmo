package com.dubhacks.maps_mmo.client;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GameKeyListener implements KeyListener {
    private final Game game;

    public GameKeyListener(Game game) {
        this.game = game;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        game.setKeyDown(e.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent e) {
        game.setKeyUp(e.getKeyCode());
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // ignore
    }
}
