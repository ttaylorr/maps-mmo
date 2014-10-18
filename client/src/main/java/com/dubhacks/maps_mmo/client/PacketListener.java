package com.dubhacks.maps_mmo.client;

import com.dubhacks.maps_mmo.event.EventHandler;
import com.dubhacks.maps_mmo.event.Listener;
import com.dubhacks.maps_mmo.packets.PlayerAddPacket;
import com.dubhacks.maps_mmo.packets.PlayerMovePacket;

public class PacketListener implements Listener {
    private final Game game;

    public PacketListener(Game game) {
        this.game = game;
    }

    @EventHandler
    public void handleLateJoiners(LocalPlayer self, PlayerAddPacket playerAdd) {
        game.addPlayer(new ClientPlayer(playerAdd.id, playerAdd.name));
    }

    @EventHandler
    public void handlePlayerMove(LocalPlayer self, PlayerMovePacket move) {
        ClientPlayer player = game.getPlayerById(move.playerId);
        if (player != null) {
            player.setLocation(move.x, move.y);
        }
    }
}
