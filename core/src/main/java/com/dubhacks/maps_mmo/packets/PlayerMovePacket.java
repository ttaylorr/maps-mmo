package com.dubhacks.maps_mmo.packets;

public class PlayerMovePacket extends Packet {
    private static final long serialVersionUID = 1L;

    public Integer playerId;
    public int x;
    public int y;

    public PlayerMovePacket(Integer playerId, int x, int y) {
        this.playerId = playerId;
        this.x = x;
        this.y = y;
    }
}
