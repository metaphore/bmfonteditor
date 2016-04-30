package com.crashinvaders.screenmanager;

public interface ScreenManager {
    /**
     * Shows new screen.
     * @param screen Screen to show.
     * @param preserveCurrent Determines whenever current screen will be placed to screen stack after been closed.
     */
    void showScreen(Screen screen, boolean preserveCurrent);

    //TODO add bundle as parameter
    /**
     * Close current screen. In case screen stack not empty, previous screen will shown, otherwise app closes.
     */
    void closeCurrentScreen();
}
