package com.dubhacks.maps_mmo.event;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;

public class EventManager {
    private final Multimap<List<Class<?>>, ListenerHandler> listeners = LinkedHashMultimap.create();

    public void addListener(Listener listener) {
        for (Method method : listener.getClass().getMethods()) {
            EventHandler annotation = method.getAnnotation(EventHandler.class);
            if (annotation != null) {
                List<Class<?>> types = new ArrayList<>();
                for (Type type : method.getTypeParameters()) {
                    types.add(type.getClass());
                }
                this.listeners.put(types, new ListenerHandler(listener, method, annotation.priority()));
            }
        }
    }

    public void dispatch(Object... params) {
        List<Class<?>> types = new ArrayList<>();
        for (Object obj : params) {
            types.add(obj.getClass());
        }
        for (ListenerHandler handler : this.listeners.get(types)) {
            handler.invoke(params);
        }
    }
}
