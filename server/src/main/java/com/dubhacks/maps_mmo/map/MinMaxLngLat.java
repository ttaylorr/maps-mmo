package com.dubhacks.maps_mmo.map;

import org.geojson.LineString;
import org.geojson.LngLatAlt;
import org.geojson.MultiPolygon;
import org.geojson.Polygon;

import java.util.List;

public class MinMaxLngLat {
    private final MinMax lng = new MinMax();
    private final MinMax lat = new MinMax();

    public MinMax getLongitude() {
        return this.lng;
    }

    public MinMax getLatitude() {
        return this.lat;
    }

    public void put(LngLatAlt point) {
        this.lng.put(point.getLongitude());
        this.lat.put(point.getLatitude());
    }

    public void put(Polygon poly) {
        for (LngLatAlt point : poly.getExteriorRing()) {
            this.put(point);
        }
    }

    public void put(MultiPolygon multiPoly) {
        for (List<List<LngLatAlt>> poly : multiPoly.getCoordinates()) {
            for (List<LngLatAlt> ring : poly) {
                for (LngLatAlt point : ring) {
                    this.put(point);
                }
            }
        }
    }

    public void put(LineString line) {
        for (LngLatAlt point : line.getCoordinates()) {
            this.put(point);
        }
    }

}
