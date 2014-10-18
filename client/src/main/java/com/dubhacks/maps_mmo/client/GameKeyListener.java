package com.dubhacks.maps_mmo.client;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import org.geojson.LngLatAlt;

public class GameKeyListener implements KeyListener {
    public static final double TRANSLATE_X = 0.05;
    public static final double TRANSLATE_Y = 0.05;
    public static final double ZOOM_X = 0.01;
    public static final double ZOOM_Y = 0.01;

    private final Game game;

    public GameKeyListener(Game game) {
        this.game = game;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
        case KeyEvent.VK_LEFT:
            game.setBounds(transformBounds(game.getBounds(), -TRANSLATE_X, 0, -TRANSLATE_X, 0));
            break;
        case KeyEvent.VK_RIGHT:
            game.setBounds(transformBounds(game.getBounds(), TRANSLATE_X, 0, TRANSLATE_X, 0));
            break;
        case KeyEvent.VK_UP:
            game.setBounds(transformBounds(game.getBounds(), 0, TRANSLATE_Y, 0, TRANSLATE_Y));
            break;
        case KeyEvent.VK_DOWN:
            game.setBounds(transformBounds(game.getBounds(), 0, -TRANSLATE_Y, 0, -TRANSLATE_Y));
            break;
        case KeyEvent.VK_EQUALS:
            game.setBounds(transformBounds(game.getBounds(), ZOOM_X, ZOOM_Y, -ZOOM_X, -ZOOM_Y));
            break;
        case KeyEvent.VK_MINUS:
            game.setBounds(transformBounds(game.getBounds(), -ZOOM_X, -ZOOM_Y, ZOOM_X, ZOOM_Y));
            break;
        }
    }

    private static Bounds transformBounds(Bounds old, double dx1, double dy1, double dx2, double dy2) {
        LngLatAlt min = new LngLatAlt(old.min.getLongitude() + dx1, old.min.getLatitude() + dy1);
        LngLatAlt max = new LngLatAlt(old.max.getLongitude() + dx2, old.max.getLatitude() + dy2);
        return new Bounds(min, max);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void keyTyped(KeyEvent e) {
        // TODO Auto-generated method stub

    }

}
