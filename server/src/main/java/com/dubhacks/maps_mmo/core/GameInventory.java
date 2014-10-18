package com.dubhacks.maps_mmo.core;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

public class GameInventory implements IGameInventory {

    private final Map<IGameInventoryObject, Integer> map;
    
    public GameInventory() {
        this.map = new TreeMap<>(new Comparator<IGameInventoryObject>() {
            @Override
            public int compare(IGameInventoryObject o1, IGameInventoryObject o2) {
                return o1.getId() - o2.getId();
            }
        });
    }
    
    @Override
    public int size() {
        return this.map.size();
    }

    @Override
    public boolean contains(IGameInventoryObject object) {
        return this.map.containsKey(object);
    }

    @Override
    public int getCount(IGameInventoryObject object) {
        Integer i = this.map.get(object);
        return i == null ? 0 : i;
    }

    @Override
    public void add(IGameInventoryObject object) {
        this.add(object, 1);
    }

    @Override
    public void add(IGameInventoryObject object, int count) {
        Integer i = this.map.get(object);
        i = (i == null) ? count : i + count;
        this.map.put(object, i);
    }

    @Override
    public boolean remove(IGameInventoryObject object) {
        return this.remove(object, 1);
    }

    @Override
    public boolean remove(IGameInventoryObject object, int count) {
        int currentCount = this.getCount(object);
        if (currentCount < count) {
            return false;
        }
        
        this.map.put(object, currentCount - count);
        return true;
    }
    
}
