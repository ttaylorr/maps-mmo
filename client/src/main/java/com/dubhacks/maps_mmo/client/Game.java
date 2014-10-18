package com.dubhacks.maps_mmo.client;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

import com.dubhacks.maps_mmo.net.SocketPlayer;
import com.dubhacks.maps_mmo.event.EventManager;
import com.dubhacks.maps_mmo.packets.ConnectPacket;

public class Game {
    public static final double TRANSLATE_X = 0.01;
    public static final double TRANSLATE_Y = 0.01;
    public static final double ZOOM_X = 0.01;
    public static final double ZOOM_Y = 0.01;

    private final EventManager eventManager;

    private Bounds bounds;

    private SocketPlayer connectingPlayer;
    private ClientPlayer player;

    private static Set<Integer> keysDown = new HashSet<>();

    public Game(EventManager eventManager) {
        this.eventManager = eventManager;
    }

    public boolean isPlaying() {
        return player != null;
    }

    public boolean isConnected() {
        return isPlaying() || connectingPlayer != null;
    }

    public void connect(String hostname, int port, String username) throws IOException {
        if (isConnected()) {
            throw new IllegalStateException("already connected");
        }
        Socket socket = new Socket();
        socket.connect(new InetSocketAddress(hostname, port));
        connectingPlayer = new SocketPlayer(socket);
        ConnectPacket packet = new ConnectPacket();
        packet.name = username;
        connectingPlayer.sendPacket(packet);
    }

    public Bounds getBounds() {
        return bounds;
    }

    public void setBounds(Bounds newBounds) {
        bounds = newBounds;
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
