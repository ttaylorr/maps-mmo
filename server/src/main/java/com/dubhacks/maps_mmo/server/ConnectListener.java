package com.dubhacks.maps_mmo.server;

import com.dubhacks.maps_mmo.event.EventHandler;
import com.dubhacks.maps_mmo.event.Listener;
import com.dubhacks.maps_mmo.net.SocketPlayer;
import com.dubhacks.maps_mmo.packets.ConnectPacket;
import com.dubhacks.maps_mmo.packets.MapPacket;
import com.dubhacks.maps_mmo.packets.PlayerAddPacket;

public class ConnectListener implements Listener {
    private final Server server;

    public ConnectListener(Server server) {
        this.server = server;
    }

    private static int nextPlayerId = 1;
    @EventHandler
    public void handleConnect(SocketPlayer player, ConnectPacket packet) {
        System.out.println("Received connect from: " + packet.name);

        // notify of existing players
        for (ServerPlayer other : server.getPlayers()) {
            System.out.println("Telling " + packet.name + " about " + other.getName());
            player.sendPacket(playerAddFromPlayer(other));
        }

        // send map data
        player.sendPacket(new MapPacket(server.getMap()));

        // add and broadcast to finalize connect
        ServerPlayer newPlayer = new ServerPlayer(player, nextPlayerId++, packet.name);
        newPlayer.setLocation(server.getMap().getWidth() / 2, server.getMap().getHeight() / 2);
        server.getConnectionManager().addPlayer(newPlayer);
        server.getConnectionManager().broadcastPacket(playerAddFromPlayer(newPlayer));
    }

    private static PlayerAddPacket playerAddFromPlayer(ServerPlayer player) {
        return new PlayerAddPacket(player.getId(), player.getName(), player.getX(), player.getY());
    }
}
