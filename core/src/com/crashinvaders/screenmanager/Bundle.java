package com.crashinvaders.screenmanager;

/**
 * Same usual map, but it with some extra convenient methods.
 */
public interface Bundle {
    void put(String key, Object value);
    <T> T get(String key);
    <T> T get(String key, T defaultValue);
    <T> T remove(String key);
    <T> T remove(String key, T defaultValue);
}