package com.dubhacks.maps_mmo.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

import javax.swing.JFrame;

import com.dubhacks.map_mmo.net.NetworkDefaults;
import com.dubhacks.map_mmo.net.SocketPlayer;
import com.dubhacks.maps_mmo.event.EventManager;
import com.dubhacks.maps_mmo.packets.ConnectPacket;
import com.fasterxml.jackson.core.JsonParseException;

public class Client {
    public static final int TICK_FREQUENCY = 20; // ticks per second

    public static void main(String[] args) throws JsonParseException, IOException {
        EventManager eventManager = new EventManager();
        Game game = new Game(eventManager);

        JFrame frame = new JFrame("Maps MMO");
        frame.setContentPane(new GamePanel(game));
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        Socket socket = new Socket();
        socket.connect(new InetSocketAddress("localhost", NetworkDefaults.DEFAULT_PORT));

        SocketPlayer connectingPlayer = new SocketPlayer(socket);
        game.setConnectingPlayer(connectingPlayer);

        ConnectPacket connectPacket = new ConnectPacket();
        connectPacket.name = "Test User";
        connectingPlayer.sendPacket(connectPacket);

        while (true) {
            long start = System.currentTimeMillis();

            game.tick();

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
