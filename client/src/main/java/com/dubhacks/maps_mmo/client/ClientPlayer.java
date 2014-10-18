package com.dubhacks.maps_mmo.client;

import com.dubhacks.maps_mmo.net.SocketPlayer;

public class ClientPlayer {
    private final SocketPlayer socketPlayer;

    private int x;
    private int y;

    public ClientPlayer(SocketPlayer socketPlayer) {
        this.socketPlayer = socketPlayer;
    }

    public SocketPlayer getSocketPlayer() {
        return socketPlayer;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setLocation(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
