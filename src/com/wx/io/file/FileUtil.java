package com.wx.io.file;

import com.wx.console.Console;
import com.wx.console.color.Color;
import com.wx.console.UserConsoleInterface;
import com.wx.io.Accessor;
import com.wx.util.Format;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author canale
 */
public class FileUtil {

    /*
     * Calculate checksum of a File using MD5 algorithm
     */
    public static byte[] checkSum(File file) throws IOException {
        try {
            FileInputStream fis = new FileInputStream(file);
            MessageDigest md = MessageDigest.getInstance("MD5");

            //Using MessageDigest update() method to provide input
            byte[] buffer = new byte[8192];
            int numOfBytesRead;
            while ((numOfBytesRead = fis.read(buffer)) > 0) {
                md.update(buffer, 0, numOfBytesRead);
            }

            return md.digest();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e); // Should not happen with MD5
        }
    }

    /**
     * Copies a file or folder to destination and automatically creates all parents folders if they don't exist.<br><br>
     * If you try to copy a directory <code>A</code> in a destination <code>B</code>, the content of <code>A</code> will
     * be copied in <code>B</code>. If you really want a copy of <code>A</code> within <code>B</code>, then set the
     * destination as <code>B + File.separator + A</code>.
     *
     * @param source      File or directory to copy
     * @param destination Destination
     *
     * @throws IOException
     */
    public static void copyFile(File source, File destination) throws IOException {
        if (source == null || destination == null) {
            throw new NullPointerException();
        }
        if (!source.exists()) {
            throw new FileNotFoundException("Source file does not exits:\n"
                    + source.getAbsolutePath());
        }

        if (source.isDirectory()) {
            if (!destination.exists() && !destination.mkdirs()) {
                throw new IOException("Unable to create the destination "
                        + "directory or one of its parent:\n"
                        + destination.getAbsolutePath());
            }

            for (File child : source.listFiles()) {
                copyFile(child, new File(destination.getAbsolutePath()
                        + File.separator + child.getName()));
            }
        } else {
            // Source is a File
            if (!destination.getParentFile().exists()
                    && !destination.getParentFile().mkdirs()) {
                throw new IOException("Unable to create the destination's "
                        + "parent directory or one of its parent:\n"
                        + destination.getAbsolutePath());
            }

            try (Accessor accessor = new Accessor()) {
                accessor.setIn(source).setOut(destination, false);
                accessor.pourInOut();
            }
        }
    }

    /**
     * Deletes a directory and all its content.
     *
     * @param source Directory to delete
     *
     * @return {@code true} if everything went successfully
     */
    public static boolean deleteDir(File source) {
        boolean success = true;

        for (File file : source.listFiles()) {
            if (file.isDirectory()) {
                if (!deleteDir(file)) {
                    success = false;
                }
            } else {
                if (!file.delete()) {
                    success = false;
                }
            }
        }

        return success && source.delete();
    }

    /**
     * Deletes a directory and all its content.
     *
     * @param source Directory to delete
     *
     * @return {@code true} if everything went successfully
     */
    public static boolean deleteDir(File source, Logger log) {
        boolean success = true;

        for (File file : source.listFiles()) {
            if (file.isDirectory()) {
                if (!deleteDir(file)) {
                    log.log(Level.WARNING, "Couldn''t delete directory:\n{0}",
                            file.getAbsolutePath());
                    success = false;
                } else {
                    log.log(Level.INFO, "Directory deleted:\n{0}",
                            file.getAbsolutePath());
                }
            } else {
                if (!file.delete()) {
                    success = false;
                    log.log(Level.WARNING, "Couldn''t delete file:\n{0}",
                            file.getAbsolutePath());
                } else {
                    log.log(Level.INFO, "File deleted:\n{0}",
                            file.getAbsolutePath());
                }
            }
        }

        return success && source.delete();
    }

    public static File getFreshFile(File directory, String name, String extension) {
        File file;
        if (directory != null) {
            assert directory.isDirectory();
            file = new File(directory, name + extension);
        } else {
            file = new File(name + extension);
        }

        int i = 1;
        while (file.exists()) {
            file = new File(directory, name + "_" + i + extension);
            i++;
        }

        return file;
    }

    public static File autoCreateDirectory(String name) throws IOException {
        File file = new File(name);
        autoCreateDirectory(file);

        return file;
    }

    public static void autoCreateDirectory(File file) throws IOException {
        if (!file.isDirectory() && !file.mkdir()) {
            throw new IOException("Cannot create directory: " + file);
        }
    }

    public static void autoCreateDirectories(File file) throws IOException {
        if (!file.isDirectory() && !file.mkdirs()) {
            throw new IOException("Cannot create directory: " + file);
        }
    }

    public static File getExistingFileOrDir(String name) {
        File file = new File(name);
        if (!file.exists()) {
            throw new IllegalArgumentException(file.getName() + " does not exist");
        }

        return file;
    }

    public static File getExistingDirectory(String name) {
        File file = new File(name);
        if (!file.exists()) {
            throw new IllegalArgumentException(file.getName() + " does not exist");
        }
        if (!file.isDirectory()) {
            throw new IllegalArgumentException(file.getName() + " is not a directory");
        }

        return file;
    }

    public static File getExistingFile(String name) {
        File file = new File(name);
        if (!file.exists()) {
            throw new IllegalArgumentException(file.getName() + " does not exist");
        }
        if (!file.isFile()) {
            throw new IllegalArgumentException(file.getName() + " is not a file");
        }

        return file;
    }

    public static File getNewFile(UserConsoleInterface uci, String name) {
        File file = new File(name);

        if (file.exists()) {
            Console c = uci.getConsole();

            c.print("File ");
            c.setColor(Color.Cyan);
            c.print(file.getName());
            c.resetStyle();
            c.println(" already exists, override it?");

            if (!uci.inputYesNo()) {
                throw new IllegalArgumentException("Canceled by user");
            }
        }

        return file;
    }

    public static File createTmpDirectory() throws IOException {
        return Files.createTempDirectory(null).toFile();
    }

    public static File getTmpFile(String originalName) {
        throw new UnsupportedOperationException("");
//        String realExtenstion = fileExtension(originalName);
//        originalName = originalName.substring(0, originalName.length()
//                - realExtenstion.length());
//
//        File result = new File(originalName + ".tmp" + realExtenstion);
//        int count = 2;
//
//        while (result.exists()) {
//            result = new File(originalName + " (" + count + ").tmp" + realExtenstion);
//            count++;
//        }
//        return result;
    }

    @Deprecated
    public static String nameWithoutExtension(File file) {
        return nameWithoutExtension(file.getName());
    }

    public static String nameWithoutExtension(String fileName) {
        int lastPoint = fileName.lastIndexOf('.');

        if (lastPoint < 0) {
            return fileName;
        } else {
            return fileName.substring(0, lastPoint);
        }
    }

    @Deprecated
    public static String fileExtension(File file) {
        return fileExtension(file.getName());
    }

    public static String fileExtension(String fileName) {
        int lastPoint = fileName.lastIndexOf('.');

        if (lastPoint < 0 || lastPoint < fileName.lastIndexOf(File.separator)) {
            return "";
        } else if (lastPoint == fileName.length() - 1) {
            return ".";
        } else {
            return fileName.substring(lastPoint);
        }
    }

}
