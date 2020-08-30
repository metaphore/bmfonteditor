package com.metaphore.bmfmetaedit.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
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
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setTitle("BMFont Editor");
        config.setWindowIcon(Files.FileType.Classpath, "icon128.png", "icon32.png", "icon16.png");
        config.setPreferencesConfig(".prefs/bmfontmetaedit", Files.FileType.External);
        config.setWindowedMode(1024, 600);

        new Lwjgl3Application(new App(new DesktopLauncher()), config);
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
