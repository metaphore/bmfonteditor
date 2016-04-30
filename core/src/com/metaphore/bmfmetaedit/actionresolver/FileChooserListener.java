package com.metaphore.bmfmetaedit.actionresolver;

import com.badlogic.gdx.files.FileHandle;

public interface FileChooserListener {
    void onResult(boolean success, FileHandle fileHandle);
}
