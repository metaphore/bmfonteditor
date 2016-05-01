package com.metaphore.bmfmetaedit.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.metaphore.bmfmetaedit.App;
import com.metaphore.bmfmetaedit.actionresolver.ActionResolver;
import com.metaphore.bmfmetaedit.actionresolver.FileChooserListener;
import com.metaphore.bmfmetaedit.actionresolver.FileChooserParams;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;

public class DesktopLauncher implements ActionResolver {
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "BMFont Editor";
        config.preferencesDirectory = ".bmfontmetaedit/";
        config.width = 1024;
        config.height = 600;

        new LwjglApplication(new App(new DesktopLauncher()), config);
    }

    @Override
    public void fileChooser(FileChooserParams params, FileChooserListener listener) {
        SwingUtilities.invokeLater(() -> {
            JPanel panel = new JPanel(new FlowLayout());

            File rootDir = params.getRootDir();
            if (rootDir == null) {
                rootDir = Gdx.files.internal("./").file();
            }

            JFileChooser fc = new JFileChooser(rootDir);
            fc.setMultiSelectionEnabled(false);
            fc.setDialogType(params.isOpen() ? JFileChooser.OPEN_DIALOG : JFileChooser.SAVE_DIALOG);
            fc.setDialogTitle(params.getTitle());
            for (String ext : params.getExtensions()) {
                fc.addChoosableFileFilter(new FileNameExtensionFilter(ext, ext));
            }
            fc.setAcceptAllFileFilterUsed(false);
            int result = fc.showDialog(panel, "Done");

            if (result != JFileChooser.APPROVE_OPTION) {
                listener.onResult(false, null);
                return;
            }

            listener.onResult(true, Gdx.files.absolute(fc.getSelectedFile().getAbsolutePath()));
        });
    }
}
