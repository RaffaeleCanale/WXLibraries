package com.wx.util.representables.string;

import com.wx.io.file.FileUtil;
import com.wx.util.representables.wrapper.FileRepr;

import java.io.File;
import java.io.IOException;

/**
 * Cast a file from a string (and vice-versa) ensuring that the file is a directory and (possibly) automatically creates
 * the directory.
 * <p>
 * If a path does not denote a directory or a directory cannot be created, a {@code ClassCastException} is thrown.
 * <p>
 * Created on 13/04/2015
 *
 * @author Raffaele Canale (raffaelecanale@gmail.com)
 * @version 0.1
 */
public class DirectoryRepr extends FileRepr {

    private final boolean autoCreate;

    /**
     * Initialize a new {@code DirectoryRepr}.
     *
     * @param autoCreate If {@code true}, any directory returned by {@link #castOut(String)} will be automatically
     *                   created if it doesn't exist
     */
    public DirectoryRepr(boolean autoCreate) {
        this.autoCreate = autoCreate;
    }

    @Override
    public String castIn(File value) throws ClassCastException {
        if (!value.isDirectory()) {
            throw new IllegalArgumentException("Not a directory");
        }

        return super.castIn(value);
    }

    @Override
    public File castOut(String value) throws ClassCastException {
        try {
            return autoCreate ? FileUtil.autoCreateDirectory(value) : FileUtil.getExistingDirectory(value);
        } catch (IOException e) {
            throw new ClassCastException(e.getMessage());
        }
    }
}
