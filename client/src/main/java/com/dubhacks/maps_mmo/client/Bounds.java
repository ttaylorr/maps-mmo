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

    public static Bounds transformByAddition(Bounds old, double dx1, double dy1, double dx2, double dy2) {
        LngLatAlt min = new LngLatAlt(old.min.getLongitude() + dx1, old.min.getLatitude() + dy1);
        LngLatAlt max = new LngLatAlt(old.max.getLongitude() + dx2, old.max.getLatitude() + dy2);
        return new Bounds(min, max);
    }

    public static Bounds transformByMultiplication(Bounds old, double dx1, double dy1, double dx2, double dy2) {
        LngLatAlt min = new LngLatAlt(old.min.getLongitude() * dx1, old.min.getLatitude() * dy1);
        LngLatAlt max = new LngLatAlt(old.max.getLongitude() * dx2, old.max.getLatitude() * dy2);
        return new Bounds(min, max);
    }

    public static Bounds zoom(Bounds old, double zoomX, double zoomY) {
        double meanLongitude = (old.min.getLongitude() + old.max.getLongitude()) / 2;
        double meanLatitude = (old.min.getLatitude() + old.max.getLatitude()) / 2;
        LngLatAlt min = new LngLatAlt(meanLongitude + (old.min.getLongitude() - meanLongitude) * (1 + zoomX),
                                      meanLatitude + (old.min.getLatitude() - meanLatitude) * (1 + zoomY));
        LngLatAlt max = new LngLatAlt(meanLongitude + (old.max.getLongitude() - meanLongitude) * (1 + zoomX),
                                      meanLatitude + (old.max.getLatitude() - meanLatitude) * (1 + zoomY));
        return new Bounds(min, max);
    }
}
