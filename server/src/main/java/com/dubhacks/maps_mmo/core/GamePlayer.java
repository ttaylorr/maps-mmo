package com.dubhacks.maps_mmo.core;

public class GamePlayer implements IGamePlayer {

    private String displayName;
    private IGameInventory inventory;
    private IGamePlayerStats stats;
    
    public GamePlayer(String displayName) {
        this.displayName = displayName;
    }
    
    @Override
    public String getDisplayName() {
        return this.displayName;
    }

    @Override
    public IGameInventory getInventory() {
        return this.inventory;
    }

    @Override
    public IGamePlayerStats getStats() {
        return this.stats;
    }
    
}
