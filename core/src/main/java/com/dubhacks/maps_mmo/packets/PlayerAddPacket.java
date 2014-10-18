package com.dubhacks.maps_mmo.packets;

public class PlayerAddPacket extends Packet {
    private static final long serialVersionUID = 1L;

    public int id;
    public String name;
    public int x;
    public int y;

    public PlayerAddPacket(int id, String name, int x, int y) {
        this.id = id;
        this.name = name;
        this.x = x;
        this.y = y;
    }
}
