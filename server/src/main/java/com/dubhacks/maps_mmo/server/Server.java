package com.dubhacks.maps_mmo.server;

import com.dubhacks.maps_mmo.event.EventManager;

public class Server {
    private final ServerConnectionManager connectionManager;
    private final EventManager eventManager;

    public Server(ServerConnectionManager connectionManager, EventManager eventManager) {
        this.connectionManager = connectionManager;
        this.eventManager = eventManager;
    }

    public ServerConnectionManager getConnectionManager() {
        return connectionManager;
    }

    public EventManager getEventManager() {
        return eventManager;
    }

    public void tick() {
        connectionManager.tick();
    }
}
