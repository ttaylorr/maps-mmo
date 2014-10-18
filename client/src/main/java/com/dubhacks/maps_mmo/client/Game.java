package com.dubhacks.maps_mmo.client;

import java.util.Iterator;

import org.geojson.Feature;
import org.geojson.FeatureCollection;
import org.geojson.LineString;
import org.geojson.LngLatAlt;

public class Game {
    private final FeatureCollection roads;
    private Bounds bounds;
    private GamePanel panel;

    public Game(FeatureCollection roads) {
        this.roads = roads;
    }

    public void setPanel(GamePanel panel) {
        this.panel = panel;
    }

    public Bounds getBounds() {
        return bounds;
    }

    public void setBounds(Bounds newBounds) {
        bounds = newBounds;
        repaint();
    }

    public void repaint() {
        if (panel != null) {
            panel.repaint();
        }
    }

    public void paint(MapView view) {
        for (Feature road : roads.getFeatures()) {
            if (road.getGeometry() instanceof LineString) {
                LineString str = (LineString)road.getGeometry();
                Iterator<LngLatAlt> coords = str.getCoordinates().iterator();
                LngLatAlt prev = coords.next();
                while (coords.hasNext()) {
                    LngLatAlt next = coords.next();
                    view.drawLine(prev.getLongitude(), prev.getLatitude(), next.getLongitude(), next.getLatitude());
                    prev = next;
                }
            }
        }
        //Bounds bounds = calculateBounds(featureCollection);
        //double scaleX = g.getClipBounds().getWidth() / (bounds.max.getLongitude() - bounds.min.getLongitude());
        //double scaleY = g.getClipBounds().getHeight() / (bounds.max.getLatitude() - bounds.min.getLatitude());
        //System.out.println("ScaleX: " + scaleX);
        //System.out.println("ScaleY: " + scaleY);
        //for (Feature feature : featureCollection.getFeatures()) {
        //    if (feature.getGeometry() instanceof LineString) {
        //        LineString str = (LineString)feature.getGeometry();
        //        Iterator<LngLatAlt> coords = str.getCoordinates().iterator();
        //        LngLatAlt prev = coords.next();
        //        while (coords.hasNext()) {
        //            LngLatAlt next = coords.next();
        //            g.drawLine((int)(((prev.getLongitude() - bounds.min.getLongitude()) * scaleX)),
        //                       (int)(g.getClipBounds().getHeight() - 1 - ((prev.getLatitude() - bounds.min.getLatitude()) * scaleY)),
        //                       (int)(((next.getLongitude() - bounds.min.getLongitude()) * scaleX)),
        //                       (int)(g.getClipBounds().getHeight() - 1 - ((next.getLatitude() - bounds.min.getLatitude()) * scaleY)));
        //            prev = next;
        //        }
        //    }
        //}

    }
}
