package com.dubhacks.maps_mmo.client;

import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;

import com.dubhacks.maps_mmo.net.SocketPlayer;
import com.dubhacks.maps_mmo.event.EventManager;

public class Game {
    public static final double TRANSLATE_X = 0.01;
    public static final double TRANSLATE_Y = 0.01;
    public static final double ZOOM_X = 0.01;
    public static final double ZOOM_Y = 0.01;

    private final EventManager eventManager;

    private Bounds bounds;
    private GamePanel panel;

    private SocketPlayer connectingPlayer;
    private ClientPlayer player;

    private static Set<Integer> keysDown = new HashSet<>();

    public Game(EventManager eventManager) {
        this.eventManager = eventManager;
    }

    public void setPanel(GamePanel panel) {
        this.panel = panel;
    }

    public Bounds getBounds() {
        return bounds;
    }

    public void setBounds(Bounds newBounds) {
        bounds = newBounds;
        repaint();
    }

    public void repaint() {
        if (panel != null) {
            panel.repaint();
        }
    }

    public void paint(MapView view) {
        /*for (Feature road : roads.getFeatures()) {
            if (road.getGeometry() instanceof LineString) {
                LineString str = (LineString)road.getGeometry();
                Iterator<LngLatAlt> coords = str.getCoordinates().iterator();
                LngLatAlt prev = coords.next();
                while (coords.hasNext()) {
                    LngLatAlt next = coords.next();
                    view.drawLine(prev.getLongitude(), prev.getLatitude(), next.getLongitude(), next.getLatitude());
                    prev = next;
                }
            }
        }*/
    }

    public ClientPlayer getPlayer() {
        return player;
    }

    public void setConnectingPlayer(SocketPlayer socketPlayer) {
        connectingPlayer = socketPlayer;
    }

    public void setPlayer(ClientPlayer player) {
        this.player = player;
    }

    public void setKeyDown(int keyCode) {
        keysDown.add(keyCode);
    }

    public void setKeyUp(int keyCode) {
        keysDown.remove(keyCode);
    }

    public boolean isKeyDown(int keyCode) {
        return keysDown.contains(keyCode);
    }

    public void tick() {
        if (connectingPlayer != null) {
            while (connectingPlayer.hasPacket()) {
                eventManager.dispatch(connectingPlayer, connectingPlayer.getNextPacket());
            }
        }
        if (player != null) {
            while (player.getSocketPlayer().hasPacket()) {
                eventManager.dispatch(player, player.getSocketPlayer().getNextPacket());
            }
        }

        if (isKeyDown(KeyEvent.VK_LEFT)) {
            setBounds(Bounds.transformByAddition(getBounds(), -TRANSLATE_X, 0, -TRANSLATE_X, 0));
        }
        if (isKeyDown(KeyEvent.VK_RIGHT)){
            setBounds(Bounds.transformByAddition(getBounds(), TRANSLATE_X, 0, TRANSLATE_X, 0));
        }
        if (isKeyDown(KeyEvent.VK_UP)){
            setBounds(Bounds.transformByAddition(getBounds(), 0, TRANSLATE_Y, 0, TRANSLATE_Y));
        }
        if (isKeyDown(KeyEvent.VK_DOWN)) {
            setBounds(Bounds.transformByAddition(getBounds(), 0, -TRANSLATE_Y, 0, -TRANSLATE_Y));
        }
        if (isKeyDown(KeyEvent.VK_EQUALS)) {
            setBounds(Bounds.zoom(getBounds(), -ZOOM_X, -ZOOM_Y));
        }
        if (isKeyDown(KeyEvent.VK_MINUS)) {
            setBounds(Bounds.zoom(getBounds(), ZOOM_X, ZOOM_Y));
        }
    }
}
