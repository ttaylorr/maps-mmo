package com.dubhacks.maps_mmo.client;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

import com.dubhacks.maps_mmo.core.IGameMap;
import com.dubhacks.maps_mmo.event.EventManager;
import com.dubhacks.maps_mmo.net.SocketPlayer;
import com.dubhacks.maps_mmo.packets.ConnectPacket;

public class Game {

    private final EventManager eventManager;

    private Rectangle bounds;

    private SocketPlayer connectingPlayer;
    private ClientPlayer player;

    private final Set<Integer> keysDown = new HashSet<>();

    private IGameMap map;

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

    public Rectangle getBounds() {
        return bounds;
    }

    public void setBounds(Rectangle newBounds) {
        bounds = newBounds;
    }

    public boolean isMapLoaded() {
        return map != null;
    }

    public void setMap(IGameMap map) {
        this.map = map;
        setBounds(new Rectangle(30, 30));
    }

    public void paint(Graphics2D g) {
        if (isMapLoaded()) {
            setBounds(new Rectangle(getBounds().x, getBounds().y, (int)Math.ceil(g.getClipBounds().getWidth() / 32), (int)Math.ceil(g.getClipBounds().getHeight()/ 32)));
            Rectangle b = getBounds();
            for (int x = 0; x < b.width; x++) {
                for (int y = 0; y < b.height; y++) {
                    byte tile = map.get(b.x + x, b.y + y);
                    BufferedImage image = GameAssets.getMapTile(tile);
                    g.drawImage(image, null, 32 * x, 32 * y);
                }
            }
        } else {
            g.drawString("Waiting for map data to load...", 50, 50);
        }
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

        int dx = 0, dy = 0;
        if (isKeyDown(KeyEvent.VK_LEFT)) {
            dx--;
        }
        if (isKeyDown(KeyEvent.VK_RIGHT)){
            dx++;
        }
        if (isKeyDown(KeyEvent.VK_UP)){
            dy--;
        }
        if (isKeyDown(KeyEvent.VK_DOWN)) {
            dy++;
        }

        if (dx != 0 || dy != 0) {
            tryMoveBounds(dx, dy);
        }
    }

    private void tryMoveBounds(int dx, int dy) {
        Rectangle newBounds = new Rectangle(bounds);
        newBounds.x += dx;
        newBounds.y += dy;
        if (isValidBounds(newBounds)) {
            bounds = newBounds;
        }
    }

    private boolean isValidBounds(Rectangle bounds) {
        if (!isMapLoaded()) return false;
        if (bounds.x < 0 || bounds.y < 0) return false;
        if (bounds.x + bounds.width >= map.getWidth()) return false;
        if (bounds.y + bounds.height >= map.getHeight()) return false;
        return true;
    }
}
