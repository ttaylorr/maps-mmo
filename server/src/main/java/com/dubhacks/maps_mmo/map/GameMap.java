package com.dubhacks.maps_mmo.map;

import com.dubhacks.maps_mmo.core.IGameMap;

public class GameMap implements IGameMap {
    
    public static final byte TERRAIN_WATER = 10;
    public static final byte TERRAIN_FOREST = 11;
    
    public static final byte ROAD_SMALL  = 15;
    public static final byte ROAD_MEDIUM = 16;
    public static final byte ROAD_LARGE  = 17;
    
    public static final byte BUILDING_PLACEHOLDER = 50;
    
    private final int width;
    private final int height;
    
    private final byte[][] tiles;
    
    GameMap(byte[][] tiles) {
        this.tiles = tiles;
        this.height = tiles.length;
        this.width = tiles[0].length;
    }
    
    @Override
    public int getWidth() {
        return this.width;
    }

    @Override
    public int getHeight() {
        return this.height;
    }

    @Override
    public byte get(int x, int y) {
        return this.tiles[y][x];
    }

    @Override
    public boolean isTraversable(byte tile) {
        return tile == ROAD_SMALL || tile == ROAD_MEDIUM || tile == ROAD_LARGE;
    }
    
}
