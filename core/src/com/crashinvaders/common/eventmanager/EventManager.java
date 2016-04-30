package com.crashinvaders.common.eventmanager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.SnapshotArray;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class EventManager {
    public static final String LOG_TAG = "EventManager";
    private Map<Class<? extends EventInfo>, SnapshotArray<EventHandler>> handlers;

    public EventManager() {
        handlers = new HashMap<>();
    }

    public void dispatchEvent(EventInfo event) {
        Class infoClass = event.getClass();
        dispatchToHandlers(event, infoClass);
    }

    private void dispatchToHandlers(EventInfo event, Class<? extends EventInfo> eventClass) {
        if (handlers.containsKey(eventClass)) {
            SnapshotArray<EventHandler> handlers = this.handlers.get(eventClass);
            EventHandler[] handlersArray = handlers.begin();
            for (int i = 0, n = handlers.size; i < n; i++) {
                handlersArray[i].handle(event);
            }
            handlers.end();
        }
    }

    // Optimize by avoiding of array creation here (create few reloaded methods with multiple event params).
    public void addHandler(EventHandler handler, Class<? extends EventInfo>... classes) {
        for (int i = 0; i < classes.length; i++) {
            Class<? extends EventInfo> eventClass = classes[i];
            if (!handlers.containsKey(eventClass)) {
                handlers.put(eventClass, new SnapshotArray<EventHandler>(EventHandler.class));
            }

            SnapshotArray<EventHandler> handlers = this.handlers.get(eventClass);
            if (handlers.contains(handler, true)) {
                Gdx.app.error("EventManager", "Duplicated handler spotted for event: " + eventClass.getSimpleName() + ", handler: " + handler);
            } else {
                handlers.add(handler);
            }
        }
    }

    public void removeHandler(EventHandler handler) {
        Iterator<Map.Entry<Class<? extends EventInfo>, SnapshotArray<EventHandler>>> iterator = handlers.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Class<? extends EventInfo>, SnapshotArray<EventHandler>> entry = iterator.next();
            SnapshotArray<EventHandler> classEventHandlers = entry.getValue();
            classEventHandlers.removeValue(handler, true);
            if (classEventHandlers.size == 0) {
                iterator.remove();
            }
        }
    }

    public void removeHandler(EventHandler handler, Class<? extends EventInfo>... classes) {
        for (int i = 0; i < classes.length; i++) {
            Class<? extends EventInfo> eventClass = classes[i];
            if (!handlers.containsKey(eventClass)) {
                throw new RuntimeException("Handlers don't exist for class " + eventClass);
            }
            SnapshotArray<EventHandler> classEventHandlers = handlers.get(eventClass);
            if (!classEventHandlers.removeValue(handler, true)) {
                throw new RuntimeException("Handlers don't contain " + handler);
            }
            if (classEventHandlers.size == 0) {
                handlers.remove(eventClass);
            }
        }
    }

    public void clear() {
        handlers.clear();
    }

    public void logHandlers() {
        Gdx.app.log(LOG_TAG, "Common: " + handlers.size());
    }
}
