/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wx.io;

import java.io.IOException;
import java.io.OutputStream;

/**
 * This output stream wrapper allows to count the number of bytes written.
 *
 * @author Raffaele Canale - raffaelecanale@gmail.com
 * @version 1.0
 *          <p>
 *          Date: Jul 13, 2013
 */
public class CounterOutputStream extends OutputStream {

    private final OutputStream subStream;
    private long count;

    /**
     * Create a new stream that will count the number of bytes written.
     *
     * @param subStream Underlying stream
     */
    public CounterOutputStream(OutputStream subStream) {
        this.subStream = subStream;
        this.count = 0;
    }

    @Override
    public void write(int b) throws IOException {
        count++;
        subStream.write(b);
    }

    @Override
    public void flush() throws IOException {
        subStream.flush();
    }

    @Override
    public void close() throws IOException {
        subStream.close();
    }

    /**
     *
     * @return The number of bytes written to this stream
     */
    public long getCount() {
        return count;
    }
}
