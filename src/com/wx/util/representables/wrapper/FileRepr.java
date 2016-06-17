package com.wx.util.representables.wrapper;


import java.io.File;
import java.util.Objects;
import com.wx.util.representables.TypeCaster;

/**
 * Cast from a file to string and vice versa.
 *
 * @author Canale
 */
public class FileRepr implements TypeCaster<String, File> {

    @Override
    public String castIn(File value) throws ClassCastException {
        return Objects.requireNonNull(value).getAbsolutePath();
    }

    @Override
    public File castOut(String value) throws ClassCastException {
        return new File(Objects.requireNonNull(value));
    }

}
