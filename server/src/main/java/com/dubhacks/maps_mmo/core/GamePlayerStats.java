package com.dubhacks.maps_mmo.core;

import java.util.Map;
import java.util.TreeMap;

public class GamePlayerStats implements IGamePlayerStats {

    private Map<String, Integer> map;
    
    public GamePlayerStats() {
        this.map = new TreeMap<>();
    }
    
    @Override
    public int get(String stat) {
        Integer i = map.get(stat);
        return i == null ? 0 : i;
    }

    @Override
    public void set(String stat, int value) {
        this.map.put(stat, value);
    }
    
}
