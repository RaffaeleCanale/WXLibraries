package com.wx.util.log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 * Created on 09/02/2015
 *
 * @author Raffaele Canale (raffaelecanale@gmail.com)
 * @version 0.1
 */
public class HtmlFormatter extends Formatter {

    private String mCellStyleOpener;
    private String mCellStyleCloser;
    private Map<String, Color> mClassColorMap;
    private String mLastClass;
    private String mLastMethod;
    private long mLastMarkedTime;
    private Color mDefaultBackgroundColor;

    private String mNextLogName;
    private String mPreviousLogName;

    public HtmlFormatter(String nextLogName, String previousLogName) {
        mClassColorMap = new HashMap<>();
        mDefaultBackgroundColor = new Color(255, 255, 255);
        mLastMarkedTime = -1;
        mNextLogName = nextLogName;
        mPreviousLogName = previousLogName;
    }

    @Override
    public String format(LogRecord rec) {

        if (mLastClass != null &&
                !rec.getSourceClassName().endsWith(mLastClass)) {
            switchDefaultBackgroundColor();
        }


        StringBuilder buffer = new StringBuilder();

        buffer.append("<tr>");

        setCellStyle(mDefaultBackgroundColor);

        addCell(buffer, formatDate(rec.getMillis()));
        addCell(buffer, formatLevel(rec));
        addCell(buffer, formatMessage(rec));
        addCell(buffer, formatClassName(rec));
        addCell(buffer, formatMethodName(rec));

        buffer.append("</tr>\n");

        return buffer.toString();
    }

    @Override
    public synchronized String formatMessage(LogRecord record) {
        setCellStyle(mDefaultBackgroundColor);

        Object[] parameters = record.getParameters();
        if (parameters != null) {
            for (int i = 0; i < parameters.length; i++) {
                if (parameters[i] == null) {
                    parameters[i] = "<b><font color='RED'>null</font></b>";
                } else {
                    parameters[i] = "<b><font color='blue'>" +
                            parameters[i].toString().replaceAll(" ", "&nbsp;")
                            + "</font></b>";
                }
            }
        }
        String message = super.formatMessage(record).replaceAll("\n", "<br>");

        Throwable exception = record.getThrown();
        if (exception != null) {
            message += "<br><b><font color='RED'>" + exception.toString()
                    + "</font></b>";
        }
        return message;
    }

    @Override
    public String getHead(Handler h) {
        return "<HTML>\n<HEAD>\n" + (new Date())
                + "\n</HEAD>\n<BODY>\n<PRE>\n"
                + "<table width=\"100%\" border>\n  "
                + "<tr><th>Time</th>"
                + "<th>Level</th>"
                + "<th>Message</th>"
                + "<th>Class</th>"
                + "<th>Method</th>"
                + "</tr>\n";
    }
    // This method is called just after the handler using this
    // formatter is closed

    @Override
    public String getTail(Handler h) {
        String result = "</table>\n  </PRE>";

        if (mPreviousLogName != null) {
            result+= "<a href='" + mPreviousLogName + "'>Previous log</a>";
        }
        if (mNextLogName != null) {
            result+= "&nbsp;&nbsp;&nbsp;<a href='"+mNextLogName+"'>Next log</a>";
        }

        result+= "</BODY>\n</HTML>\n";
        return result;
    }

    private String formatClassName(LogRecord rec) {
        String fullClassName = rec.getSourceClassName();

        int lastPointIndex = fullClassName.lastIndexOf('.');
        String className = fullClassName.substring(lastPointIndex + 1);

        Color classColor = mClassColorMap.get(className);
        if (classColor == null) {
            classColor = loadNewColor();
            mClassColorMap.put(className, classColor);
        }
        setCellStyle(classColor);

        if (className.equals(mLastClass)) {
            return "";
        } else {
            mLastClass = className;
            return className;
        }
    }

    private String formatMethodName(LogRecord rec) {
        String methodName = rec.getSourceMethodName();
        if (methodName.equals(mLastMethod)) {
            return "";
        } else {
            mLastMethod = methodName;
            return methodName;
        }
    }

    private void addCell(StringBuilder buffer, String content) {
        buffer.append(mCellStyleOpener).append(content).append(
                mCellStyleCloser);
    }

    private String formatLevel(LogRecord rec) {
        Level level = rec.getLevel();
        if (level.equals(Level.INFO)) {
            setCellStyle(new Color(100, 255, 100), false,
                    new Color(255, 255, 255));

        } else if (level.equals(Level.WARNING)) {
            setCellStyle(new Color(255, 160, 0));

        } else if (level.equals(Level.SEVERE)) {
            setCellStyle(new Color(200, 30, 30), true, null);

        } else {
            setCellStyle(mDefaultBackgroundColor);
        }

        return level.getName();
    }

    private String formatDate(long millisecs) {
        if (millisecs - mLastMarkedTime > 1000) {
            SimpleDateFormat date_format = new SimpleDateFormat("HH:mm:ss");
            Date resultDate = new Date(millisecs);

            mLastMarkedTime = millisecs;
            return date_format.format(resultDate);
        } else {
            return "";
        }
    }

    private void setCellStyle(Color bgcolor) {
        setCellStyle(bgcolor, false, null);
    }

    private void setCellStyle(Color bgcolor, boolean bold, Color fontColor) {
        Color color;
        if (bgcolor == mDefaultBackgroundColor) {
            color = mDefaultBackgroundColor;
        } else {
            //color = bgcolor.substract(mDefaultBackgroundColor);
            color = bgcolor;
        }

        mCellStyleOpener = "<td bgcolor='" + color + "'>";
        mCellStyleCloser = "</td>";


        if (bold) {
            mCellStyleOpener += "<b>";
            mCellStyleCloser = "</b>" + mCellStyleCloser;
        }
        if (fontColor != null) {
            mCellStyleOpener += "<font color='" + fontColor + "'>";
            mCellStyleCloser = "</font>" + mCellStyleCloser;
        }
    }

    private void switchDefaultBackgroundColor() {
        if (mDefaultBackgroundColor.isWhite()) {
            mDefaultBackgroundColor = new Color(240, 240, 240);
        } else {
            mDefaultBackgroundColor = new Color(255, 255, 255);
        }
    }

    private Color loadNewColor() {
        int limit = 180;

        return new Color(generateColorHigherThan(limit),
                generateColorHigherThan(limit),
                generateColorHigherThan(limit));
    }

    private int generateColorHigherThan(int value) {
        return (new Random()).nextInt(255 - value) + value;
    }

    private class Color {

        private int[] mColors;

        private Color(int r, int g, int b) {
            mColors = new int[3];

            mColors[0] = checkColor(r);
            mColors[1] = checkColor(g);
            mColors[2] = checkColor(b);
        }

        private Color(int... colors) {
            assert colors.length == 3;
            this.mColors = colors;
        }

        @Override
        public String toString() {
            String value = "#";

            for (int color : mColors) {
                value += ensureLengthOf(Integer.toHexString(color), 2);
            }
            return value;
        }

        public Color substract(Color c) {
            int[] newColors = new int[3];
            for (int i = 0; i < 3; i++) {
                newColors[i] = mColors[i] - (255 - c.mColors[i]);
            }

            return new Color(newColors);
        }

        public boolean isWhite() {
            for (int color : mColors) {
                if (color != 255) {
                    return false;
                }
            }

            return true;
        }

        private String ensureLengthOf(String content, int size) {
            while (content.length() < size) {
                content = "0" + content;
            }

            return content.substring(0, size);
        }

        private int checkColor(int color) {
            if (color < 0) {
                return 0;
            } else if (color > 255) {
                return 255;
            }
            return color;
        }
    }
}

