package com.dubhacks.maps_mmo.core;

import java.io.Serializable;

public interface IGamePlayerStats extends Serializable {
    
    int get(String stat);
    
    void set(String stat, int value);
    
}
