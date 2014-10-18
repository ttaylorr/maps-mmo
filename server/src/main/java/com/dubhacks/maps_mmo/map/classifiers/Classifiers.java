package com.dubhacks.maps_mmo.map.classifiers;

import com.dubhacks.maps_mmo.core.map.MinMaxLngLat;
import org.geojson.*;

import java.util.List;

public abstract class Classifiers {
    public static void put(MinMaxLngLat mm, LngLatAlt point) {
        mm.getLongitude().put(point.getLongitude());
        mm.getLatitude().put(point.getLatitude());
    }

    public static void put(MinMaxLngLat mm, Polygon poly) {
        for (LngLatAlt point : poly.getExteriorRing()) {
            put(mm, point);
        }
    }

    public static void put(MinMaxLngLat mm, MultiPolygon multiPoly) {
        for (List<List<LngLatAlt>> poly : multiPoly.getCoordinates()) {
            for (List<LngLatAlt> ring : poly) {
                for (LngLatAlt point : ring) {
                    put(mm, point);
                }
            }
        }
    }

    public static void put(MinMaxLngLat mm, LineString line) {
        for (LngLatAlt point : line.getCoordinates()) {
            put(mm, point);
        }
    }

}
