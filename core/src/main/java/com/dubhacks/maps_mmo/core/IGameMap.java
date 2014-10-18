package com.dubhacks.maps_mmo.core;

import java.io.Serializable;

/**
 * Interface for the game map.
 * 
 */
public interface IGameMap extends Serializable {
    
    /**
     * Returns the width of the map.
     * @return the width of the map
     */
    int getWidth();
    
    /**
     * Returns the height of the map.
     * @return the height of the map
     */
    int getHeight();
    
    /**
     * Gets the tile at the specified coordinates.
     * 
     * @param x column of desired tile
     * @param y row of desired tile
     * @return integer from 0-255 representing tile at specified coordinates
     */
    byte get(int x, int y);
    
    /**
     * Indicates whether the specified tile type is player-traversable.
     * 
     * @param tile integer from 0-255
     * @return <code>true</code> if the player is able to traverse the tile,
     *         other <code>false</code>
     */
    boolean isTraversable(byte tile);
    
}
