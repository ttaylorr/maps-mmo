package com.dubhacks.maps_mmo.core;

public interface IGameInventory {
    
    int size();
    
    boolean contains(IGameInventoryObject object);
    
    int getCount(IGameInventoryObject object);
    
    void add(IGameInventoryObject object);
    
    void add(IGameInventoryObject object, int count);
    
    boolean remove(IGameInventoryObject object);
    
    boolean remove(IGameInventoryObject object, int count);
    
}
