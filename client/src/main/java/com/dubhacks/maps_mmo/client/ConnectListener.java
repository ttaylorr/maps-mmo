package com.dubhacks.maps_mmo.client;

import com.dubhacks.maps_mmo.event.EventHandler;
import com.dubhacks.maps_mmo.event.Listener;
import com.dubhacks.maps_mmo.net.SocketPlayer;
import com.dubhacks.maps_mmo.packets.MapPacket;
import com.dubhacks.maps_mmo.packets.PlayerAddPacket;

public class ConnectListener implements Listener {
    private final Game game;

    public ConnectListener(Game game) {
        this.game = game;
    }

    @EventHandler
    public void handleMapData(SocketPlayer self, MapPacket mapPacket) {
        game.setMap(mapPacket.getMap());
        System.out.println("Received map data");
    }

    @EventHandler
    public void handleInitialPlayerAdd(SocketPlayer self, PlayerAddPacket playerAdd) {
        System.out.println("Received player add for: " + playerAdd.name);
        if (playerAdd.name.equals(game.getConnectingName())) {
            // finished initial connection
            LocalPlayer player = new LocalPlayer(self, playerAdd.id, playerAdd.name);
            player.setLocation(playerAdd.x, playerAdd.y);
            game.setLocalPlayer(player);
            System.out.println("Found ourselves");
        } else {
            ClientPlayer player = new ClientPlayer(playerAdd.id, playerAdd.name);
            player.setLocation(playerAdd.x, playerAdd.y);
            game.addPlayer(player);
            System.out.println("Adding: " + player.getName());
        }
    }
}
