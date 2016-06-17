package com.wx.util.log;


import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * Created on 09/02/2015
 *
 * @author Raffaele Canale (raffaelecanale@gmail.com)
 * @version 0.1
 */
public class ShortConsoleFormatter extends Formatter {

    @Override
    public String format(LogRecord lr) {
        String exceptionLine = "";
        Throwable exception = lr.getThrown();

        if (exception != null) {
            exceptionLine = exception.toString() + "\n";
        }

        return "[" + lr.getLevel() + "] " + formatMessage(lr) + "\n" + exceptionLine;
    }

}

