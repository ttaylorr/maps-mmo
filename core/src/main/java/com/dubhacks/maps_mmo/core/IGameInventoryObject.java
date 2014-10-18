package com.dubhacks.maps_mmo.core;

import java.io.Serializable;

public interface IGameInventoryObject extends Serializable {
    
    String getDisplayName();
    
    int getId();
    
}
