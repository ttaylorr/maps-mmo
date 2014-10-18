package com.dubhacks.maps_mmo.server;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;

import com.dubhacks.maps_mmo.map.GameMap;
import com.dubhacks.maps_mmo.map.GameMapBuilder;
import com.dubhacks.maps_mmo.map.GeoJsonFileType;
import com.dubhacks.maps_mmo.net.NetworkDefaults;
import com.dubhacks.maps_mmo.event.EventManager;

public class ServerLauncher {
    public static final int TICK_FREQUENCY = 20; // ticks per second

    public static void main(String[] args) throws IOException {
        GameMapBuilder gmb = new GameMapBuilder();

        gmb.addFile(GeoJsonFileType.WATER, new File("geojson/seattle_washington-waterareas.geojson"));
        gmb.addFile(GeoJsonFileType.ROADS, new File("geojson/seattle_washington-roads.geojson"));
        gmb.addFile(GeoJsonFileType.LAND_USAGES, new File("geojson/seattle_washington-landusages.geojson"));
        gmb.addFile(GeoJsonFileType.BUILDINGS, new File("geojson/seattle_washington-buildings.geojson"));

        GameMap map = gmb.process();
        map.saveAsImage(new File("gamemap.png"));

        System.out.println("Starting server");

        ServerSocket serverSocket = new ServerSocket(NetworkDefaults.DEFAULT_PORT);
        EventManager eventManager = new EventManager();
        final ServerConnectionManager connManager = new ServerConnectionManager(serverSocket, eventManager);
        Server server = new Server(connManager, eventManager);

        eventManager.addListener(new ConnectListener(server));

        // client accept thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    connManager.accept();
                }
            }
        }).start();

        while (true) {
            long start = System.currentTimeMillis();

            server.tick();

            long delay = (1000 / TICK_FREQUENCY) - (System.currentTimeMillis() - start);
            if (delay > 0) {
                try {
                    Thread.sleep(delay);
                } catch (InterruptedException e) {
                    System.exit(1);
                }
            }
        }
    }
}
