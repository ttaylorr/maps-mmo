package com.dubhacks.maps_mmo.server;

import com.dubhacks.maps_mmo.net.SocketPlayer;


public class ServerPlayer {
    private final SocketPlayer socket;

    private final String name;

    public ServerPlayer(SocketPlayer socket, String name) {
        this.socket = socket;
        this.name = name;
    }

    public SocketPlayer getSocketPlayer() {
        return socket;
    }

    public String getName() {
        return name;
    }
}
