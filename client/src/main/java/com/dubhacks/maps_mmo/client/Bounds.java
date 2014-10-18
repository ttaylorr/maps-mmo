package com.dubhacks.maps_mmo.client;

import org.geojson.Feature;
import org.geojson.FeatureCollection;
import org.geojson.LineString;
import org.geojson.LngLatAlt;

public class Bounds {
    public final LngLatAlt min;
    public final LngLatAlt max;

    public Bounds(LngLatAlt min, LngLatAlt max) {
        this.min = min;
        this.max = max;
    }

    public static Bounds calculateBounds(FeatureCollection featureCollection) {
        LngLatAlt min = new LngLatAlt(Double.MAX_VALUE, Double.MAX_VALUE);
        LngLatAlt max = new LngLatAlt(-Double.MAX_VALUE, -Double.MAX_VALUE);
        for (Feature feature : featureCollection.getFeatures()) {
            if (feature.getGeometry() instanceof LineString) {
                LineString line = (LineString)feature.getGeometry();
                for (LngLatAlt point : line.getCoordinates()) {
                    if (point.getLatitude() < min.getLatitude()) min.setLatitude(point.getLatitude());
                    if (point.getLatitude() > max.getLatitude()) max.setLatitude(point.getLatitude());
                    if (point.getLongitude() < min.getLongitude()) min.setLongitude(point.getLongitude());
                    if (point.getLongitude() > max.getLongitude()) max.setLongitude(point.getLongitude());
                }
            }
        }
        return new Bounds(min, max);
    }
}
