package com.dubhacks.maps_mmo.client;

import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;

import org.geojson.FeatureCollection;
import org.geojson.LngLatAlt;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Client {
    public static void main(String[] args) throws JsonParseException, IOException {
        long start = System.currentTimeMillis();
        final FeatureCollection featureCollection = new ObjectMapper().readValue(new File("seattle_washington-roads.geojson"), FeatureCollection.class);
        System.out.println("Parsed json in " + (System.currentTimeMillis() - start) + "ms");

        Bounds bounds = Bounds.calculateBounds(featureCollection);
        System.out.println("Min: " + toString(bounds.min));
        System.out.println("Max: " + toString(bounds.max));

        Game game = new Game(featureCollection);

        JFrame frame = new JFrame("Maps MMO");
        frame.setContentPane(new GamePanel(game));
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        game.setBounds(bounds);
    }

    private static String toString(LngLatAlt p) {
        return "{lat=" + p.getLatitude() + ", long=" + p.getLongitude() + ", alt=" + p.getAltitude() + "}";
    }
}
