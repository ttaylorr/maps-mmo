package com.dubhacks.maps_mmo.packets;

public class MapPacket extends Packet {
    private byte[][] map;

    public MapPacket(byte[][] map) {
        this.map = map;
    }

    public byte[][] getMap() {
        return this.map;
    }
}
