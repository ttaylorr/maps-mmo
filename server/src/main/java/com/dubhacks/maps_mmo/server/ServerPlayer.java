package com.dubhacks.maps_mmo.server;

import com.dubhacks.map_mmo.net.SocketPlayer;


public class ServerPlayer {
    private final SocketPlayer socket;

    public ServerPlayer(SocketPlayer socket) {
        this.socket = socket;
    }

    public SocketPlayer getSocketPlayer() {
        return socket;
    }
}
