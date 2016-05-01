package com.metaphore.bmfmetaedit.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.crashinvaders.common.eventmanager.EventManager;
import com.metaphore.bmfmetaedit.model.events.PageTextureUpdatedEvent;
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;

import java.io.File;

public class PageModel {
    private final EventManager eventManager;
    private final FileHandle sourceFile;
    private final FileAlterationMonitor fileAlterationMonitor;
    private Texture pageTexture;

    public PageModel(EventManager eventManager, FileHandle pageTexture) {
        this.eventManager = eventManager;
        sourceFile = pageTexture;
        reloadPageTexture();

        // Initialize file change monitor
        {
            try {
                final String sourceFilePath = sourceFile.file().getAbsolutePath();
                FileAlterationObserver fileAlterationObserver = new FileAlterationObserver(sourceFile.parent().file(), file -> {
                    return file.getAbsolutePath().equals(sourceFilePath);
                });
                fileAlterationObserver.addListener(new FileAlterationListenerAdaptor() {
                    @Override
                    public void onFileChange(File file) {
                        Gdx.app.postRunnable(() -> reloadPageTexture());
                    }
                });
                fileAlterationMonitor = new FileAlterationMonitor(1000, fileAlterationObserver);
                fileAlterationMonitor.start();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void dispose() {
        pageTexture.dispose();

        try {
            fileAlterationMonitor.stop();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Texture getPageTexture() {
        return pageTexture;
    }

    private void reloadPageTexture() {
        Gdx.app.log("PageModel", "Page texture updated");

        if (pageTexture != null) {
            pageTexture.dispose();
        }
        pageTexture = new Texture(sourceFile);

        eventManager.dispatchEvent(new PageTextureUpdatedEvent(this));
    }
}
