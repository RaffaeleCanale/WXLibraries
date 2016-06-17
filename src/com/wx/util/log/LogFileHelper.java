package com.wx.util.log;

import com.wx.util.representables.string.IntRepr;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.IOException;

/**
 * Created on 09/02/2015
 *
 * @author Raffaele Canale (raffaelecanale@gmail.com)
 * @version 0.1
 */
public class LogFileHelper {

    private static final int MAX_LOG_FILES = 10;

    static String getLoggerPath(File loggerDir, String logFileName, int sessionNumber) {
        String fileName = logFileName + sessionNumber + ".html";
        File logFile = new File(loggerDir, fileName);

        return logFile.getAbsolutePath();
    }

    static int getSessionNumber(File dir, String logFileName) {
        int maxNumber = 0;

        assert dir.isDirectory();
        for (File file : dir.listFiles()) {
            String name = file.getName();
            int startIndex = logFileName.length();
            int endIndex = name.lastIndexOf('.');

            if (endIndex > 0) {
                String session = name.substring(startIndex, endIndex);

                try {
                    int sessionNumb = new IntRepr().castOut(session);
                    if (sessionNumb > maxNumber) {
                        maxNumber = sessionNumb;
                    }
                } catch (ClassCastException ignored) {}
            }
        }
        return maxNumber + 1;
    }

    static void checkLogFilesCount(File loggerDir, String logFileName) throws IOException {
        assert loggerDir.isDirectory();
        final File[] logFiles = listLogFiles(loggerDir, logFileName);

        if (logFiles != null && logFiles.length > MAX_LOG_FILES) {
//            sLog.log(Level.INFO, "Max number of logs reached ({0}/{1})",
//                    new Object[]{fileCount, MAX_LOG_FILES});
            deleteOldestFile(logFiles);
        }
    }

    private static void deleteOldestFile(File[] logFiles) throws IOException {
        long minDate = Long.MAX_VALUE;

        assert logFiles.length > 0;
        File foundFile = logFiles[0];

        for (File file : logFiles) {
            long date = file.lastModified();

            if (date < minDate) {
                foundFile = file;
                minDate = date;
            }
        }

//        sLog.log(Level.INFO, "Deleting oldest log:\nPath: {0}",
//                foundFile.getAbsolutePath());
        if (!foundFile.delete()) {
            throw new IOException("Failed to remove: " + foundFile);
        }
    }

    private static File[] listLogFiles(File dir, String logFileName) {
        assert dir.isDirectory();
        return dir.listFiles(pathname -> {
            return pathname.getName().matches(logFileName + "\\d+\\.html");
        });
    }
}
