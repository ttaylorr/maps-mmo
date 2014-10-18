package com.dubhacks.maps_mmo.core.map;

import com.dubhacks.maps_mmo.core.IGameMap;

public class GameMap implements IGameMap {
    private static final long serialVersionUID = 1L;

    public static final byte BLANK_TILE = 1;
    public static final byte TERRAIN_WATER = 10;
    public static final byte TERRAIN_FOREST = 11;

    public static final byte ROAD_SMALL  = 15;
    public static final byte ROAD_MEDIUM = 16;
    public static final byte ROAD_LARGE  = 17;

    public static final byte BUILDING_PLACEHOLDER = 50;

    private final int width;
    private final int height;

    public final byte[][] tiles;
    public transient final MapInfo info;

    public GameMap(byte[][] tiles, MapInfo info) {
        this.tiles = tiles;
        this.info = info;
        height = tiles.length;
        width = tiles[0].length;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public byte get(int x, int y) {
        return tiles[y][x];
    }

    public void set(int x, int y, byte b) {
        tiles[y][x] = b;
    }

    @Override
    public boolean isTraversable(byte tile) {
        return tile == ROAD_SMALL || tile == ROAD_MEDIUM || tile == ROAD_LARGE;
    }

}
