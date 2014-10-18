package com.dubhacks.maps_mmo.server;

import com.dubhacks.maps_mmo.event.EventHandler;
import com.dubhacks.maps_mmo.event.Listener;
import com.dubhacks.maps_mmo.packets.PlayerMovePacket;

public class PacketListener implements Listener {
    private final Server server;

    public PacketListener(Server server) {
        this.server = server;
    }

    @EventHandler
    public void handlePlayerMove(ServerPlayer player, PlayerMovePacket move) {
        player.setLocation(move.x, move.y);
        move.playerId = player.getId();
        server.getConnectionManager().broadcastPacketExcept(move, player);
    }
}
