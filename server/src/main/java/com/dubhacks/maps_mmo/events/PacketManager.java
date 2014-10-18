package com.dubhacks.maps_mmo.events;

import com.dubhacks.maps_mmo.packets.Packet;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PacketManager {
    protected Multimap<Class<? extends Packet>, ListenerHandler> handlers;

    public PacketManager() {
        this.handlers = HashMultimap.create();
    }

    public void registerHandler(Listener listener) {
        for (Method method : listener.getClass().getMethods()) {
            EventHandler annotation = method.getAnnotation(EventHandler.class);
            if (annotation != null) {
                Class<?> type = method.getParameterTypes()[0];
                if (Packet.class.isAssignableFrom(type)) {
                    // We have an event handler!
                    this.handlers.put((Class<? extends Packet>) type, new ListenerHandler(listener, method, annotation.priority()));
                }
            }
        }
    }

    public void handlePacket(Packet packet) {
        List<ListenerHandler> handlers = new ArrayList<>(this.handlers.get(packet.getClass()));
        Collections.sort(handlers); // Sort by the priority of the handlers

        for (ListenerHandler handler : handlers) {
            handler.invoke(packet);
        }
    }
}
