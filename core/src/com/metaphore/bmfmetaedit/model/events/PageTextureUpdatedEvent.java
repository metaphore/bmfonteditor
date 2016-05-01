package com.metaphore.bmfmetaedit.model.events;

import com.crashinvaders.common.eventmanager.EventInfo;
import com.metaphore.bmfmetaedit.model.PageModel;

public class PageTextureUpdatedEvent implements EventInfo {
    private final PageModel model;

    public PageTextureUpdatedEvent(PageModel model) {
        this.model = model;
    }

    public PageModel getPageModel() {
        return model;
    }
}
