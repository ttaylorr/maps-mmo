package com.dubhacks.maps_mmo.server;

import com.dubhacks.maps_mmo.net.SocketPlayer;


public class ServerPlayer {
    private final SocketPlayer socket;

    private final int id;
    private final String name;
    private int x;
    private int y;

    public ServerPlayer(SocketPlayer socket, int id, String name) {
        this.socket = socket;
        this.id = id;
        this.name = name;
    }

    public SocketPlayer getSocketPlayer() {
        return socket;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setLocation(int newX, int newY) {
        x = newX;
        y = newY;
    }
}
