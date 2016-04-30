package com.metaphore.bmfmetaedit.actionresolver;

import com.badlogic.gdx.files.FileHandle;

public interface FileChooserListener {
    void onSuccess(FileHandle fileHandle);
    void onCanceled();
}
