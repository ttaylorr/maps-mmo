package com.dubhacks.maps_mmo.map;

import java.awt.*;

public enum GeoJsonFileType {

    ROADS(Color.WHITE, GameMap.ROAD_MEDIUM),

    WATER(Color.BLUE, GameMap.TERRAIN_WATER),

    LAND_USAGES(Color.GREEN, GameMap.TERRAIN_FOREST),

    BUILDINGS(Color.GRAY, GameMap.BUILDING_PLACEHOLDER);

    protected final Color color;
    protected final byte mapValue;

    private GeoJsonFileType(Color color, byte mapValue) {
        this.color = color;
        this.mapValue = mapValue;
    }

    public Color getColor() {
        return this.color;
    }

    public byte asByte() {
        return this.mapValue;
    }

    public static GeoJsonFileType fromByte(byte b) {
        for (GeoJsonFileType t : values()) {
            if (t.asByte() == b) {
                return t;
            }
        }

        return null;
    }
}
