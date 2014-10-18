package com.dubhacks.maps_mmo.server;

public class Server {
    private final ServerConnectionManager connectionManager;

    public Server(ServerConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    public void tick() {
        connectionManager.tick();
    }
}
