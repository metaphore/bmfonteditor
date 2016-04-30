package com.crashinvaders.common.timer;

public class EmptyTimeUpListener implements TimeUpListener{
    public final static EmptyTimeUpListener inst = new EmptyTimeUpListener();

    private EmptyTimeUpListener() {}

    @Override
    public void onTimeUp() {

    }
}
