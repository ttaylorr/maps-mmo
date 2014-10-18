package com.dubhacks.maps_mmo.server;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;

import javax.imageio.ImageIO;

import com.dubhacks.maps_mmo.core.IGameMap;
import com.dubhacks.maps_mmo.core.map.GameMap;
import com.dubhacks.maps_mmo.event.EventManager;
import com.dubhacks.maps_mmo.map.GameMapBuilder;
import com.dubhacks.maps_mmo.map.GeoJsonFileType;
import com.dubhacks.maps_mmo.net.NetworkDefaults;

public class ServerLauncher {
    public static final int TICK_FREQUENCY = 20; // ticks per second
    public static final String FILE_PREFIX = "portland_maine.imposm-geojson/portland_maine";

    public static void main(String[] args) throws IOException {
        GameMapBuilder gmb = new GameMapBuilder();

        gmb.addFile(GeoJsonFileType.WATER, new File(FILE_PREFIX + "-waterareas.geojson"));
        gmb.addFile(GeoJsonFileType.ROADS, new File(FILE_PREFIX + "-roads.geojson"));
        gmb.addFile(GeoJsonFileType.LAND_USAGES, new File(FILE_PREFIX + "-landusages.geojson"));
        gmb.addFile(GeoJsonFileType.BUILDINGS, new File(FILE_PREFIX + "-buildings.geojson"));

        GameMap map = gmb.process();

        saveMapAsImage(map, new File("gamemap.png"));

        System.out.println("Starting server");

        ServerSocket serverSocket = new ServerSocket(NetworkDefaults.DEFAULT_PORT);
        EventManager eventManager = new EventManager();
        final ServerConnectionManager connManager = new ServerConnectionManager(serverSocket, eventManager);
        Server server = new Server(connManager, eventManager, map);

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

    public static void saveMapAsImage(IGameMap map, File out) throws IOException {
        BufferedImage image = new BufferedImage(map.getWidth(), map.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D g = image.createGraphics();

        for (int x = 0; x < map.getWidth(); x++) {
            for (int y = 0; y < map.getHeight(); y++) {
                byte b = map.get(x, y);
                if (b != 0) {
                    g.setColor(byteColor(b));
                    g.drawLine(x, y, x, y);
                }
            }
        }

        g.dispose();
        ImageIO.write(image, "png", out);
    }
    
    private static Color byteColor(byte b) {
        return new Color(b | (b << 8) | (b << 16));
    }
    
}
