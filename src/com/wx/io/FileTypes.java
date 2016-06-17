/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.wx.io;

import com.wx.io.file.FileUtil;
import com.wx.util.Format;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 *
 * @author Raffaele Canale - raffaelecanale@gmail.com
 * @version 0.1
 *
 * Project: WXLibraries.ANT
 * File: FileTypes.java (UTF-8)
 * Date: Jul 18, 2013 
 */
public class FileTypes {
    
    public static boolean isSubtitle(File file) throws IOException {
        String type = Files.probeContentType(file.toPath());
//        return type != null && type.endsWith("video/");
        throw new UnsupportedOperationException("code this");
    }
    
    public static boolean isVideo(File file) throws IOException {
        String type = Files.probeContentType(file.toPath());
        return type != null && type.startsWith("video/");
    }
    
//    private static boolean isType(File file, Enum[] types) {
//        if (file.isDirectory()) {
//            return false;
//        }
//        String extension = FileUtil.fileExtension(file);
//        if (extension == null) {
//            return false;
//        }
//
//        for (Enum<?> type : types) {
//            if (extension.equals(type.name())) {
//                return true;
//            }
//        }
//
//        return false;
//    }
//
//
//
//    public enum VideoExtensions {
//        mp4,
//        mkv,
//        avi,
//        flv
//    }
//
//    private enum Subs {
//        srt,
//        ass,
//        sub
//    }


    private FileTypes() {
    }

}
