package com.metaphore.bmfmetaedit.mainscreen;

import com.crashinvaders.common.eventmanager.EventManager;
import com.metaphore.bmfmetaedit.mainscreen.selection.GlyphSelectionManager;

public interface MainScreenContext {
    GlyphSelectionManager getSelectionManager();
    MainResources getResources();
    EventManager getEvents();
}
