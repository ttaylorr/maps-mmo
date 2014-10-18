package com.dubhacks.maps_mmo.client;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

import javax.swing.JFrame;

import org.geojson.FeatureCollection;
import org.geojson.LngLatAlt;

import com.dubhacks.map_mmo.net.NetworkDefaults;
import com.dubhacks.map_mmo.net.SocketPlayer;
import com.dubhacks.maps_mmo.event.EventManager;
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

        EventManager eventManager = new EventManager();
        Game game = new Game(eventManager, featureCollection);

        JFrame frame = new JFrame("Maps MMO");
        frame.setContentPane(new GamePanel(game));
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        game.setBounds(bounds);

        Socket socket = new Socket();
        socket.connect(new InetSocketAddress("localhost", NetworkDefaults.DEFAULT_PORT));
        game.setConnectingPlayer(new SocketPlayer(socket));
    }

    private static String toString(LngLatAlt p) {
        return "{lat=" + p.getLatitude() + ", long=" + p.getLongitude() + ", alt=" + p.getAltitude() + "}";
    }
}
