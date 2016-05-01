package com.metaphore.bmfmetaedit.loadscreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;

import java.io.File;

class InternalDirFileHandleResolver implements FileHandleResolver {
    private final FileHandle rootDir;

    public InternalDirFileHandleResolver(String rootDir) {
        this.rootDir = Gdx.files.internal(rootDir);
    }

    public FileHandle getRootDir() {
        return rootDir;
    }

    @Override
    public FileHandle resolve(String fileName) {
        if (fileName.startsWith("res")) {
            FileHandle file = Gdx.files.internal(fileName);
            if (file.exists()) return file;
        }

        return Gdx.files.internal(rootDir.path() + File.separator + fileName);
    }

    public String unresolve(FileHandle fileHandle) {
        return fileHandle.path().substring((rootDir.path()+File.separator).length());
    }
}
