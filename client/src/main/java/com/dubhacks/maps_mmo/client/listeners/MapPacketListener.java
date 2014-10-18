package com.dubhacks.maps_mmo.client.listeners;

import com.dubhacks.maps_mmo.client.Game;
import com.dubhacks.maps_mmo.event.EventHandler;
import com.dubhacks.maps_mmo.event.Listener;
import com.dubhacks.maps_mmo.packets.MapPacket;

import static com.google.common.base.Preconditions.checkNotNull;

public class MapPacketListener implements Listener {
    protected final Game game;

    public MapPacketListener(Game game) {
        this.game = checkNotNull(game, "game");
    }

    @EventHandler
    public void onMapPacket(MapPacket packet) {
        this.game.setCurrentMap(packet.getMap());
    }
}
