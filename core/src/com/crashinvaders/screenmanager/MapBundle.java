package com.crashinvaders.screenmanager;

import com.badlogic.gdx.utils.ObjectMap;

public class MapBundle implements Bundle {
    private final ObjectMap<String, Object> map;

    public MapBundle() {
        this.map = new ObjectMap<>();
    }

    @Override
    public void put(String key, Object value) {
        map.put(key, value);
    }

    @Override
    public <T> T get(String key) {
        return get(key, null);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T get(String key, T defaultValue) {
        Object value = map.get(key);
        if (value != null) {
            try {
                return (T)value;
            } catch (ClassCastException e) {
                throw new RuntimeException("Can't cast value of type " + value.getClass().getSimpleName() + " for key " + key + "to something else you trying...", e);
            }
        } else {
            return defaultValue;
        }
    }

    @Override
    public <T> T remove(String key) {
        return remove(key, null);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T remove(String key, T defaultValue) {
        Object value = map.remove(key);
        if (value != null) {
            try {
                return (T)value;
            } catch (ClassCastException e) {
                throw new RuntimeException("Can't cast value of type " + value.getClass().getSimpleName() + " for key " + key + "to something else you trying...", e);
            }
        } else {
            return defaultValue;
        }
    }
}
