package com.codequest23.events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class EventOrchestrator {

    @SuppressWarnings("rawtypes")
    private final Map<Class, EventHandlers> handlerMap = new HashMap<>();

    @SuppressWarnings("unchecked")
    public <T extends GameEvent> void registerListener(Class<T> clazz, Consumer<T> listener) {
        this.handlerMap.computeIfAbsent(clazz, x -> new EventHandlers<T>()).addListener(listener);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public <T extends GameEvent> void callEvent(T event) {
        Class clazz = event.getClass();
        EventHandlers handlers = handlerMap.get(clazz);
        if (handlers == null) {
            return;
        }
        ((EventHandlers<T>) handlers).callEvent(event);
    }


    private static class EventHandlers<T> {
        private final List<Consumer<T>> listeners = new ArrayList<>();

        public void addListener(Consumer<T> listener) {
            this.listeners.add(listener);
        }

        public void removeListener(Consumer<T> listener) {
            this.listeners.remove(listener);
        }

        public void callEvent(T event) {
            this.listeners.forEach(listener -> listener.accept(event));
        }
    }

}
