package com.metaphore.bmfmetaedit.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.metaphore.bmfmetaedit.App;
import com.metaphore.bmfmetaedit.actionresolver.ActionResolver;
import com.metaphore.bmfmetaedit.actionresolver.FileChooserListener;
import com.metaphore.bmfmetaedit.actionresolver.FileChooserParams;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.io.File;
import java.util.stream.Stream;

public class DesktopLauncher implements ActionResolver {
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = 1024;
        config.height = 600;

        new LwjglApplication(new App(new DesktopLauncher()), config);
    }

    @Override
    public void fileChooser(FileChooserParams params, FileChooserListener listener) {
        SwingUtilities.invokeLater(() -> {
            JPanel panel = new JPanel(new FlowLayout());

            JFileChooser fc = new JFileChooser(Gdx.files.internal("./").file());
            fc.setMultiSelectionEnabled(false);
            fc.setDialogType(params.isOpen() ? JFileChooser.OPEN_DIALOG : JFileChooser.SAVE_DIALOG);
            fc.setDialogTitle(params.getTitle());
            for (String ext : params.getExtensions()) {
                fc.addChoosableFileFilter(new FileNameExtensionFilter(ext, ext));
            }
            fc.setAcceptAllFileFilterUsed(false);
            int result = fc.showDialog(panel, "Done");

            if (result != JFileChooser.APPROVE_OPTION) {
                listener.onCanceled();
                return;
            }

            listener.onSuccess(Gdx.files.absolute(fc.getSelectedFile().getAbsolutePath()));
        });
    }
}
