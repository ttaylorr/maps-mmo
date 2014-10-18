package com.dubhacks.maps_mmo.client;

import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;

import com.dubhacks.maps_mmo.net.SocketPlayer;
import com.dubhacks.maps_mmo.event.EventManager;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Game {

    private final EventManager eventManager;

    private Rectangle bounds;
    private GamePanel panel;

    private SocketPlayer connectingPlayer;
    private ClientPlayer player;

    private Set<Integer> keysDown = new HashSet<>();

    // TODO: someone is working on making this not null
    private byte[][] tiles;

    public Game(EventManager eventManager) {
        this.eventManager = eventManager;
    }

    public void setPanel(GamePanel panel) {
        this.panel = panel;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public void setBounds(Rectangle newBounds) {
        bounds = newBounds;
        repaint();
    }

    public void repaint() {
        if (panel != null) {
            panel.repaint();
        }
    }

    public void paint(Graphics2D g) {
        Rectangle b = this.getBounds();
        for (int x = 0; x < b.width; x++) {
            for (int y = 0; y < b.height; y++) {
                byte tile = this.tiles[b.y + y][b.x + x];
                BufferedImage image = GameAssets.getMapTile(tile);
                g.drawImage(image, null, 32 * x, 32 * y);
            }
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
            this.tryMoveBounds(dx, dy);
        }
    }
    
    private void tryMoveBounds(int dx, int dy) {
        Rectangle newBounds = new Rectangle(this.bounds);
        newBounds.x += dx;
        newBounds.y += dy;
        if (this.isValidBounds(newBounds)) {
            this.bounds = newBounds;
            this.repaint();
        }
    }
    
    private boolean isValidBounds(Rectangle bounds) {
        if (bounds.x < 0 || bounds.y < 0) return false;
        if (bounds.x + bounds.width >= this.tiles[0].length) return false;
        if (bounds.y + bounds.height >= this.tiles.length) return false;
        return true;
    }
    
}
