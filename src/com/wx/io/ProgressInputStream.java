package com.wx.io;

import java.io.IOException;
import java.io.InputStream;
import java.util.function.Consumer;

/**
 * This stream will notify a consumer of the number of read bytes at every given interval.
 * <p>
 * Created on 13/08/2015
 *
 * @author Raffaele Canale (raffaelecanale@gmail.com)
 * @version 0.1
 */
public class ProgressInputStream extends InputStream {

    private final CounterInputStream subStream;
    private final Consumer<Long> incrementFunction;
    private final long interval;

    private long nextInterval;

    /**
     * Create a stream that will notify to the incrementFunction the number of bytes read.
     *
     * @param subStream         Underlying stream
     * @param incrementFunction Consumer to notify (the argument of this function will be the number of bytes actually
     *                          read since the start).
     * @param interval          Interval (in number of bytes read) rate at which the incrementFunction will be called
     */
    public ProgressInputStream(InputStream subStream, Consumer<Long> incrementFunction, long interval) {
        if (interval <= 0) {
            throw new IllegalArgumentException("Interval must be > 0");
        }

        this.subStream = new CounterInputStream(subStream);
        this.incrementFunction = incrementFunction;
        this.interval = interval;

        this.nextInterval = interval;
    }

    @Override
    public int read() throws IOException {
        long count = subStream.getCount();
        if (count == nextInterval) {
            incrementFunction.accept(count);
            nextInterval += interval;
        }

        return subStream.read();
    }

    @Override
    public void close() throws IOException {
        long count = subStream.getCount();
        if (count != nextInterval - interval) {
            incrementFunction.accept(count);
        }
        super.close();
    }
}
