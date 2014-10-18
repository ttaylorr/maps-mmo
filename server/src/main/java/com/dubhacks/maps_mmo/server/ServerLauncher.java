package com.dubhacks.maps_mmo.server;

import com.dubhacks.maps_mmo.core.IGameMap;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;

import com.dubhacks.maps_mmo.core.map.GameMap;
import com.dubhacks.maps_mmo.map.GameMapBuilder;
import com.dubhacks.maps_mmo.map.GeoJsonFileType;
import com.dubhacks.maps_mmo.net.NetworkDefaults;
import com.dubhacks.maps_mmo.event.EventManager;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class ServerLauncher {
    public static final int TICK_FREQUENCY = 20; // ticks per second

    public static void main(String[] args) throws IOException {
        GameMapBuilder gmb = new GameMapBuilder();

        gmb.addFile(GeoJsonFileType.WATER, new File("seattle_washington.imposm-geojson\\seattle_washington-waterareas.geojson"));
        gmb.addFile(GeoJsonFileType.ROADS, new File("seattle_washington.imposm-geojson\\seattle_washington-roads.geojson"));
        gmb.addFile(GeoJsonFileType.LAND_USAGES, new File("seattle_washington.imposm-geojson\\seattle_washington-landusages.geojson"));
        gmb.addFile(GeoJsonFileType.BUILDINGS, new File("seattle_washington.imposm-geojson\\seattle_washington-buildings.geojson"));

        GameMap map = gmb.process();
        
        saveMapAsImage(map, new File("gamemap.png"));

        System.out.println("Starting server");

        ServerSocket serverSocket = new ServerSocket(NetworkDefaults.DEFAULT_PORT);
        EventManager eventManager = new EventManager();
        final ServerConnectionManager connManager = new ServerConnectionManager(serverSocket, eventManager);
        Server server = new Server(connManager, eventManager);
        server.setMap(map);

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
                    GeoJsonFileType type = GeoJsonFileType.fromByte(b);

                    g.setColor(type.getColor());
                    g.drawLine(x, y, x, y);
                }
            }
        }

        g.dispose();
        ImageIO.write(image, "png", out);
    }
}
