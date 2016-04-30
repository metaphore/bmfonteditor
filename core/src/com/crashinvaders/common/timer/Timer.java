package com.crashinvaders.common.timer;

import com.badlogic.gdx.utils.Pool;

//TODO дописать возможность зацикоиваия
public class Timer implements Pool.Poolable {
    private float timeElapsed;
    private float totalDuration;
    private TimeUpListener listener;
    private boolean running;

    // No-args constructor for pooling
    public Timer() {
    }

    public void start(float duration, TimeUpListener listener) {
        this.totalDuration = duration;
        this.listener = listener;
        this.running = true;
        this.timeElapsed = 0;
    }

    public void start() {
        listener = null;
        totalDuration = Float.MAX_VALUE;
        timeElapsed = 0;
        running = true;
    }

    public void add(float duration) {
        if (!running) {
            throw new RuntimeException("Timer isn't running. Trying to add time to inactive timer.");
        }
        totalDuration += duration;
    }

    public void update(float delta) {
        if (!running) return;

        timeElapsed += delta;
        if (timeElapsed >= totalDuration) {
            running = false;
            if (listener != null) {
                listener.onTimeUp();
            }
        }
    }

    public boolean isRunning() {
        return running;
    }

    public float getTimeLeft() {
        if (!running) {
            return 0;
        }
        return totalDuration - timeElapsed;
    }

    public float getTimeElapsed() {
        if (!running) { throw new RuntimeException("Timer is not started"); }
        return timeElapsed;
    }

    public float getTotalDuration() {
        if (!running) { throw new RuntimeException("Timer is not started"); }
        return totalDuration;
    }

    public float getTimeLeftPercentage() {
        if (!running) { throw new RuntimeException("Timer is not started"); }
        return (totalDuration - timeElapsed) / totalDuration;
    }

    public void restart() {
        timeElapsed = 0;
        running = true;
    }

    @Override
    public void reset() {
        timeElapsed = 0f;
        totalDuration = 0f;
        running = false;
        listener = null;
    }
}
