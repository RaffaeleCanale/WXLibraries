package com.wx.util.representables.string;

import com.wx.util.representables.GenericCasterTest;
import com.wx.util.representables.TypeCaster;
import com.wx.util.representables.wrapper.FileRepr;

import java.io.File;
import java.io.IOException;

public class FileReprTest extends GenericCasterTest<String, File> {

    private final File tmpFile;

    public FileReprTest() throws IOException {
        tmpFile = File.createTempFile("test", ".tmp");
        tmpFile.deleteOnExit();
    }

    @Override
    protected TypeCaster<String, File> getCaster() {
        return new FileRepr();
    }

    @Override
    protected void setUpPairs() {
        String path = tmpFile.getAbsolutePath();

        pair(path, tmpFile);
    }

}