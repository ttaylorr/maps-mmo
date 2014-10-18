package com.dubhacks.maps_mmo.server;

import java.io.IOException;
import java.net.ServerSocket;

import com.dubhacks.map_mmo.net.NetworkDefaults;
import com.dubhacks.maps_mmo.event.EventManager;

public class ServerLauncher {
    public static final int TICK_FREQUENCY = 20; // ticks per second

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(NetworkDefaults.DEFAULT_PORT);
        EventManager eventManager = new EventManager();
        final ServerConnectionManager connManager = new ServerConnectionManager(serverSocket, eventManager);
        Server server = new Server(connManager);

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
