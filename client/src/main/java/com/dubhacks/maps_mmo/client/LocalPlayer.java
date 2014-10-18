package com.dubhacks.maps_mmo.client;

import com.dubhacks.maps_mmo.net.SocketPlayer;
import com.dubhacks.maps_mmo.packets.PlayerMovePacket;

public class LocalPlayer extends ClientPlayer {
    private final SocketPlayer socketPlayer;

    public LocalPlayer(SocketPlayer socketPlayer, int id, String name) {
        super(id, name);
        this.socketPlayer = socketPlayer;
    }

    public SocketPlayer getSocketPlayer() {
        return socketPlayer;
    }

    @Override
    public void setLocation(int x, int y) {
        super.setLocation(x, y);
        socketPlayer.sendPacket(new PlayerMovePacket(null, x, y));
    }
}
