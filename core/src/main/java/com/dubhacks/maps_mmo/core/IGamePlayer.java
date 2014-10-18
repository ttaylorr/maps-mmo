package com.dubhacks.maps_mmo.core;

import java.io.Serializable;

public interface IGamePlayer extends Serializable {
    
    String getDisplayName();
    
    IGameInventory getInventory();
    
    IGamePlayerStats getStats();
    
}
