package com.dubhacks.maps_mmo.core.map;

public class MinMaxLngLat {

    private final MinMax lng = new MinMax();
    private final MinMax lat = new MinMax();

    public MinMax getLongitude() {
        return this.lng;
    }

    public MinMax getLatitude() {
        return this.lat;
    }

}
