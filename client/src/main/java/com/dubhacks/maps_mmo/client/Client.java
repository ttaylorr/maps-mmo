package com.dubhacks.maps_mmo.client;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.geojson.Feature;
import org.geojson.FeatureCollection;
import org.geojson.LineString;
import org.geojson.LngLatAlt;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Client {
    public static void main(String[] args) throws JsonParseException, IOException {
        final FeatureCollection featureCollection = new ObjectMapper().readValue(new File("seattle_washington-roads.geojson"), FeatureCollection.class);
        Bounds bounds = calculateBounds(featureCollection);
        System.out.println("Min: " + toString(bounds.min));
        System.out.println("Max: " + toString(bounds.max));

        JFrame frame = new JFrame("Window Caption");
        frame.setContentPane(new JPanel() {
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(2000, 2000);
            }
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);

                g.drawString("This is my custom Panel!", 10, 20);
                g.drawLine(100, 100, 200, 100);
                draw((Graphics2D)g, featureCollection);
            }
        });
        frame.setVisible(true);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //GameFrame frame = new GameFrame();
        //frame.setContentPane(new JPanel());
        //frame.run();
    }

    private static void draw(Graphics2D g, FeatureCollection featureCollection) {
        Bounds bounds = calculateBounds(featureCollection);
        double scaleX = g.getClipBounds().getWidth() / (bounds.max.getLongitude() - bounds.min.getLongitude());
        double scaleY = g.getClipBounds().getHeight() / (bounds.max.getLatitude() - bounds.min.getLatitude());
        System.out.println("ScaleX: " + scaleX);
        System.out.println("ScaleY: " + scaleY);
        for (Feature feature : featureCollection.getFeatures()) {
            if (feature.getGeometry() instanceof LineString) {
                LineString str = (LineString)feature.getGeometry();
                Iterator<LngLatAlt> coords = str.getCoordinates().iterator();
                LngLatAlt prev = coords.next();
                while (coords.hasNext()) {
                    LngLatAlt next = coords.next();
                    g.drawLine((int)(((prev.getLongitude() - bounds.min.getLongitude()) * scaleX)),
                               (int)(g.getClipBounds().getHeight() - 1 - ((prev.getLatitude() - bounds.min.getLatitude()) * scaleY)),
                               (int)(((next.getLongitude() - bounds.min.getLongitude()) * scaleX)),
                               (int)(g.getClipBounds().getHeight() - 1 - ((next.getLatitude() - bounds.min.getLatitude()) * scaleY)));
                    prev = next;
                }
            }
        }
    }

    private static class Bounds {
        public final LngLatAlt min;
        public final LngLatAlt max;

        public Bounds(LngLatAlt min, LngLatAlt max) {
            this.min = min;
            this.max = max;
        }
    }

    private static Bounds calculateBounds(FeatureCollection featureCollection) {
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

    private static String toString(LngLatAlt p) {
        return "{lat=" + p.getLatitude() + ", long=" + p.getLongitude() + ", alt=" + p.getAltitude() + "}";
    }
}
