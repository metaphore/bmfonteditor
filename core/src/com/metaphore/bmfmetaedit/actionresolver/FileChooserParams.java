package com.metaphore.bmfmetaedit.actionresolver;

import com.badlogic.gdx.utils.Array;

public class FileChooserParams {
    private final Array<String> extensions = new Array<>();
    private boolean open = true;
    private String title;

    public boolean isOpen() {
        return open;
    }

    /** Open dialog */
    public FileChooserParams open() {
        this.open = true;
        return this;
    }

    /** Save dialog */
    public FileChooserParams save() {
        this.open = false;
        return this;
    }

    public FileChooserParams title(String title) {
        this.title = title;
        return this;
    }

    public Array<String> getExtensions() {
        return extensions;
    }

    public FileChooserParams extensions(String... extensions) {
        this.extensions.addAll(extensions);
        return this;
    }

    public String getTitle() {
        return title;
    }
}
