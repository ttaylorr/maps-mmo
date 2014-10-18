package com.dubhacks.maps_mmo.core.map;

public class MapInfo {
    
    public final MinMax latitude;
    public final MinMax longitude;

    public final int width;
    public final int height;

    public MapInfo(MinMaxLngLat mm, double resolution) {
        this.latitude = mm.getLatitude();
        this.longitude = mm.getLongitude();

        this.height = (int) (this.latitude.getRange() / resolution);
        this.width = (int) (this.longitude.getRange() / resolution);
    }
}
