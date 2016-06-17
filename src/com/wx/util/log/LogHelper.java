package com.wx.util.log;

import java.io.File;
import java.io.IOException;
import java.util.logging.*;
import static com.wx.util.log.LogFileHelper.*;

/**
 * Simple class to help logger setup.
 * @author Canale
 */
public class LogHelper {

    private static Logger uniqueLogger;
    private static Handler[] sLogHandler = {};
//    private static Level sLevel = Level.INFO;

    /**
     * Set the handlers that will be attached to every logger taken from this
     * factory.
     *
     * @param handlers Handlers to be given to the loggers
     */
    public static void setupLogger(Handler... handlers) {
        if (handlers != null) {
            sLogHandler = new Handler[handlers.length];
            System.arraycopy(handlers, 0, sLogHandler, 0, sLogHandler.length);
        }

        uniqueLogger = getLogger(LogHelper.class);
    }

    public static Handler consoleHandler(Level level) {
        Handler consoleHandler = new ConsoleHandler();
        consoleHandler.setFormatter(new SimpleConsoleFormatter());
        consoleHandler.setLevel(level);

        return consoleHandler;
    }

    public static Handler consoleHandlerShort(Level level) {
        Handler consoleHandler = new ConsoleHandler();
        consoleHandler.setFormatter(new ShortConsoleFormatter());
        consoleHandler.setLevel(level);

        return consoleHandler;
    }

    public static Handler fileLogger(String filePath, Level level) throws IOException {
        final FileHandler fileHandler = new FileHandler(filePath);
        fileHandler.setFormatter(new SimpleConsoleFormatter());
        fileHandler.setLevel(level);

        return fileHandler;
    }

    public static Handler htmlLogger(String loggerDirPath,
                                     String logFileName,
                                     Level level) throws IOException {
        File loggerDir = new File(loggerDirPath).getAbsoluteFile();
        if (!loggerDir.exists() && !loggerDir.mkdir()) {
            throw new IOException("Impossible to create the following directory: " + loggerDir.getAbsolutePath());
        }

        int sessionNumber = getSessionNumber(loggerDir, logFileName);

        Handler fileHandler = new FileHandler(
                getLoggerPath(loggerDir, logFileName, sessionNumber));
        fileHandler.setLevel(level);

        String lastSession = null;
        String nextSession = getLoggerPath(loggerDir, logFileName, sessionNumber + 1);
        if (sessionNumber > 1) {
            lastSession = getLoggerPath(loggerDir, logFileName, sessionNumber - 1);
        }
        Formatter htmlFormatter = new HtmlFormatter(nextSession, lastSession);
        fileHandler.setFormatter(htmlFormatter);

        checkLogFilesCount(loggerDir, logFileName);
        return fileHandler;
    }

    private static Logger instance() {
        return uniqueLogger;
    }

    public static <C> Logger getLogger(Class<C> loggerClass) {
        return getLogger(loggerClass.getSimpleName());
    }

    /**
     * Get a new {@code Logger}. It will have the level and handlers as defined
     * by the user when using {@link #setupLogger(java.util.logging.Handler[])}
     *
     * @param name Name of the logger
     *
     * @return A new ready to use logger
     */
    public static Logger getLogger(String name) {
        Logger logger = Logger.getLogger(name);

        logger.setUseParentHandlers(false);
        logger.setLevel(Level.ALL);
        for (Handler handler : sLogHandler) {
            logger.addHandler(handler);
        }
        return logger;
    }

    private LogHelper() {
    }
}
