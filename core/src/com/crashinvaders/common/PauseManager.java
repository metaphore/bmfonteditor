package com.crashinvaders.common;

import com.badlogic.gdx.Gdx;

import java.util.HashSet;
import java.util.Set;

public class PauseManager {
    private static final String TAG = PauseManager.class.getSimpleName();

    protected final Set<String> pauseHolders = new HashSet<>();

    public float affect(float delta) {
        if (pauseHolders.size() > 0) {
            return 0f;
        } else {
            return delta;
        }
    }

    public void holdPause(String key) {
        if (!pauseHolders.add(key)) {
            Gdx.app.error(TAG, key + " is already in use", new IllegalArgumentException());
        }
    }

    public void releasePause(String key) {
        if (!pauseHolders.remove(key)) {
            Gdx.app.error(TAG, key + " is not in pause holders", new IllegalArgumentException());
        }
    }

    public void releaseAll() {
        pauseHolders.clear();
    }
}
