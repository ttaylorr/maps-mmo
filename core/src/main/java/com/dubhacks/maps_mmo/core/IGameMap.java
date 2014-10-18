package com.dubhacks.maps_mmo.core;

import java.io.Serializable;

public interface IGameMap extends Serializable {
    
    // get the tile at coordinates
    byte get(int x, int y);
    
    boolean isTraversable(byte tile);
    
}
