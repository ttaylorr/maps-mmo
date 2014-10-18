package com.dubhacks.maps_mmo.server;

import com.dubhacks.maps_mmo.event.EventManager;
import com.dubhacks.maps_mmo.map.GameMap;

public class Server {
    private final ServerConnectionManager connectionManager;
    private final EventManager eventManager;
    private GameMap map;

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

    public void setMap(GameMap map) {
        this.map = map;
    }

    public GameMap getMap() {
        return this.map;
    }
}
