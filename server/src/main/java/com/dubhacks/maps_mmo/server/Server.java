package com.dubhacks.maps_mmo.server;

import com.dubhacks.maps_mmo.core.map.GameMap;
import com.dubhacks.maps_mmo.event.EventManager;

public class Server {
    private final ServerConnectionManager connectionManager;
    private final EventManager eventManager;
    private final GameMap map;

    public Server(ServerConnectionManager connectionManager, EventManager eventManager, GameMap map) {
        this.connectionManager = connectionManager;
        this.eventManager = eventManager;
        this.map = map;
    }

    public ServerConnectionManager getConnectionManager() {
        return connectionManager;
    }

    public EventManager getEventManager() {
        return eventManager;
    }

    public GameMap getMap() {
        return map;
    }

    public void tick() {
        connectionManager.tick();
    }

    public void setMap(GameMap map) {
        this.map = map;
    }

    public GameMap getMap() {
        return map;
    }
}
