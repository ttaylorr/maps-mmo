package com.dubhacks.maps_mmo.client;

import java.awt.Graphics2D;
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

    private String connectingName;
    private SocketPlayer connectingPlayer;
    private LocalPlayer localPlayer;
    private final Set<ClientPlayer> players = new HashSet<>();

    private final Set<Integer> keysDown = new HashSet<>();

    private IGameMap map;

    public Game(EventManager eventManager) {
        this.eventManager = eventManager;
    }

    public boolean isPlaying() {
        return localPlayer != null;
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
        connectingName = username;
        connectingPlayer = new SocketPlayer(socket);
        ConnectPacket packet = new ConnectPacket();
        packet.name = username;
        connectingPlayer.sendPacket(packet);
    }

    public Set<ClientPlayer> getPlayers() {
        return players;
    }

    public boolean isMapLoaded() {
        return map != null;
    }

    public void setMap(IGameMap map) {
        this.map = map;
    }

    public void paint(Graphics2D g) {
        if (isPlaying()) {
            int tilesWidth = (int)Math.ceil(g.getClipBounds().getWidth() / 32);
            int tilesHeight = (int)Math.ceil(g.getClipBounds().getHeight()/ 32);
            int offsetX = localPlayer.getX() - tilesWidth / 2;
            int offsetY = localPlayer.getY() - tilesHeight / 2;
            for (int x = 0; x < tilesWidth; x++) {
                for (int y = 0; y < tilesHeight; y++) {
                    int finalX = x + offsetX;
                    int finalY = y + offsetY;
                    if (0 <= finalX && finalX < map.getWidth() &&
                            0 <= finalY && finalY < map.getHeight()) {
                        byte tile = map.get(finalX, finalY);
                        BufferedImage image = GameAssets.getMapTile(tile);
                        g.drawImage(image, null, 32 * x, 32 * y);
                    }
                }
            }
            for (ClientPlayer other : getPlayers()) {
                g.drawImage(GameAssets.getOtherPlayerSprite(), null, 32 * (other.getX() - offsetX), 32 * (other.getY() - offsetY));
            }
            g.drawImage(GameAssets.getPlayerSprite(), null, 32 * (localPlayer.getX() - offsetX), 32 * (localPlayer.getY() - offsetY));
        } else {
            g.drawString("Waiting to connect...", 50, 50);
        }
    }

    public LocalPlayer getPlayer() {
        return localPlayer;
    }

    public ClientPlayer getPlayerById(int id) {
        for (ClientPlayer player : players) {
            if (player.getId() == id) return player;
        }
        return null;
    }

    public String getConnectingName() {
        return connectingName;
    }

    public void setConnectingPlayer(SocketPlayer socketPlayer) {
        connectingPlayer = socketPlayer;
    }

    public void setLocalPlayer(LocalPlayer player) {
        localPlayer = player;
        connectingPlayer = null;
        connectingName = null;
    }

    public void addPlayer(ClientPlayer player) {
        players.add(player);
    }

    public void removePlayer(ClientPlayer player) {
        players.remove(player);
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
        while (connectingPlayer != null && connectingPlayer.hasPacket()) {
            eventManager.dispatch(connectingPlayer, connectingPlayer.getNextPacket());
        }
        while (localPlayer != null && localPlayer.getSocketPlayer().hasPacket()) {
            eventManager.dispatch(localPlayer, localPlayer.getSocketPlayer().getNextPacket());
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
            localPlayer.setLocation(localPlayer.getX() + dx, localPlayer.getY() + dy);
        }
    }
}
