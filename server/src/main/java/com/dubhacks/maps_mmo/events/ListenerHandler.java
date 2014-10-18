package com.dubhacks.maps_mmo.events;

import com.dubhacks.maps_mmo.packets.Packet;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ListenerHandler implements Comparable<ListenerHandler> {
    protected Method handler;
    protected Listener instance;
    protected EventHandler.Priority priority;

    public ListenerHandler(Listener instance, Method method, EventHandler.Priority priority) {
        this.instance = instance;
        this.handler = method;
        this.priority = priority;
    }

    public void invoke(Packet packet) {
        try {
            this.handler.invoke(this.instance, packet);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int compareTo(ListenerHandler that) {
        return this.priority.compareTo(that.priority);
    }
}
