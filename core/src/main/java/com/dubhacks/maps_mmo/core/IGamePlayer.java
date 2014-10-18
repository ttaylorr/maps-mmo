package com.dubhacks.maps_mmo.core;

public interface IGamePlayer {
    
    String getDisplayName();
    
    IGameInventory getInventory();
    
    IGamePlayerStats getStats();
    
}
