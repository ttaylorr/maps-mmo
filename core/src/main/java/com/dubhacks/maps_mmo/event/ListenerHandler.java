package com.dubhacks.maps_mmo.event;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ListenerHandler implements Comparable<ListenerHandler> {
    private final Method handler;
    private final Listener instance;
    private final EventHandler.Priority priority;

    public ListenerHandler(Listener instance, Method method, EventHandler.Priority priority) {
        this.instance = instance;
        this.handler = method;
        this.priority = priority;
    }

    public void invoke(Object... values) {
        try {
            this.handler.invoke(this.instance, values);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int compareTo(ListenerHandler that) {
        return this.priority.compareTo(that.priority);
    }
}
