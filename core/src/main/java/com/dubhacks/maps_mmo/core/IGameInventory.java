package com.dubhacks.maps_mmo.core;

import java.io.Serializable;

/**
 * Interface for a player's inventory.
 */
public interface IGameInventory extends Serializable {
    
    /**
     * Returns the size of the inventory.
     * @return the size of the inventory
     */
    int size();
    
    /**
     * Checks whether the specified object is in this inventory.
     * 
     * @param object the object whose presence to check
     * @return <code>true</code> if the specified object is present in this
     *         inventory, otherwise <code>false</code>
     */
    boolean contains(IGameInventoryObject object);
    
    /**
     * Gets the count of the specified object.
     * 
     * @param object the object whose count to get
     * @return the count of the specified object
     */
    int getCount(IGameInventoryObject object);
    
    /**
     * Adds a single instance of the specified object to this inventory. Should
     * be equivalent to <code>add(object, 1)</code>.
     * 
     * @param object the object whose count to increment
     */
    void add(IGameInventoryObject object);
    
    /**
     * Adds to the count of the specified object.
     * 
     * @param object the object whose count to increase
     * @param count the amount by which the count should be increased
     */
    void add(IGameInventoryObject object, int count);
    
    /**
     * Removes a single instance of the specified object from this inventory.
     * Should be equivalent to <code>remove(object, 1)</code>.
     * 
     * @param object the object whose count to decrement
     * @return <code>true</code> if the operation was carried out successfully,
     *         otherwise <code>false</code>
     */
    boolean remove(IGameInventoryObject object);
    
    /**
     * Subtracts from the count of the specified object.
     * 
     * @param object the object whose count to decrease
     * @param count the amount by which the count should be decreased
     * @return <code>true</code> if the operation was carried out successfully,
     *         otherwise <code>false</code>
     */
    boolean remove(IGameInventoryObject object, int count);
    
}
