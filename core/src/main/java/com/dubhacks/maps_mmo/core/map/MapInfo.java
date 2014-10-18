package com.dubhacks.maps_mmo.core.map;


public class MapInfo {
    public final MinMax latitude;
    public final MinMax longitude;

    public final int width;
    public final int height;

    public MapInfo(MinMaxLngLat mm, double resolution) {
        latitude = mm.getLatitude();
        longitude = mm.getLongitude();

        height = (int) (latitude.getRange() / resolution);
        width = (int) (longitude.getRange() / resolution);
    }
}
