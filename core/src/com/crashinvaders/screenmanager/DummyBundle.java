package com.crashinvaders.screenmanager;

/**
 * All methods are mocks.
 */
public class DummyBundle implements Bundle {
    @Override
    public void put(String key, Object value) {

    }

    @Override
    public <T> T get(String key) {
        return null;
    }

    @Override
    public <T> T get(String key, T defaultValue) {
        return null;
    }

    @Override
    public <T> T remove(String key) {
        return null;
    }

    @Override
    public <T> T remove(String key, T defaultValue) {
        return null;
    }
}
