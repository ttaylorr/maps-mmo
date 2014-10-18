package com.dubhacks.maps_mmo.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.dubhacks.maps_mmo.event.EventManager;
import com.dubhacks.maps_mmo.net.SocketPlayer;
import com.dubhacks.maps_mmo.packets.Packet;

public class ServerConnectionManager {
    private final ServerSocket serverSocket;
    private final ConcurrentMap<Socket, SocketPlayer> incomingSockets;
    private final ConcurrentMap<Socket, ServerPlayer> socketPlayers;
    private final EventManager eventManager;

    public ServerConnectionManager(ServerSocket serverSocket, EventManager eventManager) {
        this.serverSocket = serverSocket;
        incomingSockets = new ConcurrentHashMap<>();
        socketPlayers = new ConcurrentHashMap<>();
        this.eventManager = eventManager;
    }

    public void accept() {
        try {
            Socket clientSocket = serverSocket.accept();
            System.out.println("Accepting client from: " + clientSocket.getRemoteSocketAddress());
            SocketPlayer player = new SocketPlayer(clientSocket);
            incomingSockets.put(clientSocket, player);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void tick() {
        // make copies because we're not sure what will happen when we dispatch
        SocketPlayer[] incomingSocketValues = incomingSockets.values().toArray(new SocketPlayer[0]);
        for (SocketPlayer player : incomingSocketValues) {
            while (player.hasPacket()) {
                eventManager.dispatch(player, player.getNextPacket());
            }
            if (player.socket().isClosed()) {
                incomingSockets.remove(player);
            }
        }

        ServerPlayer[] players = socketPlayers.values().toArray(new ServerPlayer[0]);
        for (ServerPlayer player : players) {
            while (player.getSocketPlayer().hasPacket()) {
                eventManager.dispatch(player, player.getSocketPlayer().getNextPacket());
            }
            if (player.getSocketPlayer().socket().isClosed()) {
                socketPlayers.remove(player.getSocketPlayer().socket());
            }
        }
    }

    public Collection<ServerPlayer> getPlayers() {
        return socketPlayers.values();
    }

    public void addPlayer(ServerPlayer player) {
        SocketPlayer socketPlayer = player.getSocketPlayer();
        Socket socket = socketPlayer.socket();
        incomingSockets.remove(socket);
        socketPlayers.put(socket, player);
    }

    public void removePlayer(ServerPlayer player) {
        Socket socket = player.getSocketPlayer().socket();
        incomingSockets.remove(socket);
        socketPlayers.remove(socket);
    }

    public void broadcastPacket(Packet packet) {
        for (ServerPlayer player : socketPlayers.values()) {
            System.out.println("Broadcasting to " + player.getName());
            player.getSocketPlayer().sendPacket(packet);
        }
    }

    public void broadcastPacketExcept(Packet packet, ServerPlayer except) {
        for (ServerPlayer player : socketPlayers.values()) {
            if (player != except) {
                player.getSocketPlayer().sendPacket(packet);
            }
        }
    }
}
