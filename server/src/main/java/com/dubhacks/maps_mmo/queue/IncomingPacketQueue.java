package com.dubhacks.maps_mmo.queue;

import com.dubhacks.maps_mmo.events.PacketManager;
import com.dubhacks.maps_mmo.packets.Packet;

import java.io.IOException;
import java.io.ObjectInputStream;

import static com.google.common.base.Preconditions.checkNotNull;

public class IncomingPacketQueue implements Runnable {
    protected final ObjectInputStream stream;
    protected final PacketManager manager;

    public IncomingPacketQueue(PacketManager manager, ObjectInputStream stream) {
        this.manager = checkNotNull(manager, "manager");
        this.stream = checkNotNull(stream, "stream");
    }

    @Override public void run() {
        try {
            for (;;) {
                if (this.stream.available() == 0) {
                    try {
                        Object obj = this.stream.readObject();
                        if (obj instanceof Packet) {
                            this.manager.handlePacket((Packet) obj);
                        }
                    } catch (ClassNotFoundException e) {
                        continue;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
