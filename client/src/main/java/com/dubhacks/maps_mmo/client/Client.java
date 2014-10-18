package com.dubhacks.maps_mmo.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

import com.dubhacks.map_mmo.net.NetworkDefaults;

import com.fasterxml.jackson.core.JsonParseException;

public class Client {
    public static void main(String[] args) throws JsonParseException, IOException {
        Socket s = new Socket();
        s.connect(new InetSocketAddress(NetworkDefaults.DEFAULT_PORT));

//        JFrame frame = new JFrame("Maps MMO");
//        frame.setContentPane(new GamePanel(game));
//        frame.pack();
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setVisible(true);
    }
}
