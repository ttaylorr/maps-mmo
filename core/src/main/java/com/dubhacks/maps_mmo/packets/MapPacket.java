package com.dubhacks.maps_mmo.packets;

import com.dubhacks.maps_mmo.core.IGameMap;

public class MapPacket extends Packet {
    private static final long serialVersionUID = 1L;

    private final IGameMap map;

    public MapPacket(IGameMap map) {
        this.map = map;
    }

    public IGameMap getMap() {
        return map;
    }
}
