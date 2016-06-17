package com.wx.util;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

/**
 * Utility methods to format in a human readable way several units
 *
 * @author Raffaele Canale
 */
public class Format {

    private static final long MILLIS_IN_DAY = 86400000;
    private static final long MILLIS_IN_HOUR = 3600000;
    private static final long MILLIS_IN_MINUTE = 60000;

    /**
     * Format in a human readable way a file size.
     *
     * @param size The size (in bytes) to format
     *
     * @return A formatted representation of the given size
     */
    public static String formatSize(final long size) {
        if (size < 0) {
            throw new IllegalArgumentException("Negative value");
        }
        if (size == 0) {
            return "0 B";
        }


        final String[] units = new String[]{"B", "KiB", "MiB", "GiB", "TiB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));

        if (digitGroups >= units.length) {
            throw new IllegalArgumentException("Size too big (bigger than 1000 TB): " + size);
        }
        return new DecimalFormat("0.##").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

    /**
     * Format in a human readable way a date using a common predefined pattern (including the date and hour).
     *
     * @param date Date to format
     *
     * @return The formatted date
     *
     * @see #formatDate(long, String)
     */
    public static String formatDate(long date) {
        // TODO Should we use constants?
        return formatDate(date, "dd/MM/yy HH:mm:ss");
    }

    /**
     * Format in a human readable way an hour using a common predefined pattern.
     *
     * @param date Hour to format
     *
     * @return The formatted hour
     *
     * @see #formatDate(long, String)
     */
    public static String formatHour(long date) {
        return formatDate(date, "HH:mm:ss");
    }

    /**
     * Format in a human readable way a date using the given pattern.
     *
     * @param date Date to format
     *
     * @return The formatted date
     *
     * @see SimpleDateFormat
     */
    public static String formatDate(long date, String pattern) {
        return new SimpleDateFormat(pattern).format(date);
    }

    /**
     * Format a double using a common format (displaying at most 2 decimals, if necessary).
     *
     * @param value Value to format
     *
     * @return Formatted value
     */
    public static String formatDouble(double value) {
        return new DecimalFormat("#0.00").format(value);
    }

    /**
     * Format a ratio (between 0 and 1) into a percentage (between 0% and 100%).
     *
     * @param ratio Ratio to format
     *
     * @return Percentage corresponding to the given ratio
     */
    public static String formatPercentage(double ratio) {
        return new DecimalFormat("#,##0.#").format(ratio * 100.0) + "%";
    }

    /**
     * Format in a human readable way a quantity of time.
     *
     * @param time Time (in milliseconds) to format
     *
     * @return The formatted time
     */
    public static String formatTime(long time) {
        return formatTime(time, true);
    }

    /**
     * Format in a human readable way a quantity of time.
     * <p>
     * Optionally, it can be chosen if the units are displayed using their full names (eg. seconds, minutes,...) or
     * using diminutives (eg. s, m, ...).
     *
     * @param time  Time (in milliseconds) to format
     * @param brief If {@code true}, uses the diminutives for displaying the units
     *
     * @return The formatted time
     */
    public static String formatTime(long time, boolean brief) {
        if (time == 0) {
            return brief ? "0s" : "0 seconds";
        }
        String[][] valuesNames;
        if (brief) {
            valuesNames = new String[][]{
                    new String[]{"d ", "d "},
                    new String[]{"h ", "h "},
                    new String[]{"m ", "m "},
                    new String[]{"s ", "s "},
                    new String[]{"ms ", "ms "}
            };
        } else {
            valuesNames = new String[][]{
                    new String[]{" day ", " days "},
                    new String[]{" hour ", " hours "},
                    new String[]{" minute ", " minutes "},
                    new String[]{" second ", " seconds "},
                    new String[]{" millisecond ", " milliseconds "},
            };
        }

        long[] values = {MILLIS_IN_DAY,
                MILLIS_IN_HOUR,
                MILLIS_IN_MINUTE,
                1000,
                1};

        StringBuilder result = new StringBuilder();
        for (int i = 0; i < values.length; i++) {
            if (valuesNames[i] != null) {
                long millis_in_x = values[i];

                long x = 0;
                if (time >= millis_in_x) {
                    x = Math.floorDiv(time, millis_in_x);
                    time -= x * millis_in_x;
                }

                if (x > 0) {
                    String name = x > 1 ? valuesNames[i][1] : valuesNames[i][0];
                    result.append(x).append(name);
                }
            }
        }

        return result.toString();
    }

    /**
     * Format in a human readable way a transfer speed.
     * <p>
     * This method will calculate the speed to display according the given size transferred within the given duration.
     *
     * @param duration Duration (in milliseconds) of transfer
     * @param size     Size (in bytes) of the transfer
     *
     * @return The formatted resulting speed
     *
     * @see #formatSpeed(double)
     */
    public static String formatSpeed(long duration, long size) {
        if (duration == 0.0) {
            throw new IllegalArgumentException("Duration is 0.0");
        }
        double s_duration = (double) duration / 1000.0;
        double b_size = (double) size;
        return formatSpeed(b_size / s_duration);
    }

    /**
     * Format in a human readable way a transfer speed.
     *
     * @param speed Speed to format (in bytes per second)
     *
     * @return The formatted resulting speed
     */
    public static String formatSpeed(double speed) {
        //Speed is b/s
        if (speed < 0.0) {
            throw new IllegalArgumentException("Negative speed");
        }
        if (speed == 0.0) {
            return "0 B/s";
        }

        final String[] units = new String[]{"B/s", "KiB/s", "MiB/s", "GiB/s", "TiB/s"};
        int digitGroups = (int) (Math.log10(speed) / Math.log10(1024));
        if (digitGroups >= units.length) {
            throw new IllegalArgumentException("Speed too big (bigger than 1024 TiB/s): " + speed);
        }
        return new DecimalFormat("0.##").format(speed / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

    /**
     * Format a number ensuring that it always containing a minimal amount of digits (by prefixing with zeros if
     * necessary).
     *
     * @param number      Number to format
     * @param digitsCount Minimal number of digits
     *
     * @return The formatted number
     */
    public static String formatNumPrefix(int number, int digitsCount) {
        String result = String.valueOf(number);

        while (result.length() < digitsCount) {
            result = "0" + result;
        }

        return result;
    }

}
