package com.dubhacks.maps_mmo.core;

public class GameInventoryObject implements IGameInventoryObject {

    private final String displayName;
    private final int id;
    
    public GameInventoryObject(String name, int id) {
        this.displayName = name;
        this.id = id;
    }
    
    @Override
    public String getDisplayName() {
        return this.displayName;
    }

    @Override
    public int getId() {
        return this.id;
    }
    
}
