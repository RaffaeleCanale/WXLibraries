package com.wx.console.file;

import com.wx.console.system.SystemConsole;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 *
 * @author Canale
 */
public class FileConsole extends SystemConsole {

    public FileConsole(File file) throws FileNotFoundException {
        super(new FileOutputStream(file), null);
    }

    
    
}
