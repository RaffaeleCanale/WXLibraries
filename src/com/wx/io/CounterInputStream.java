/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.wx.io;

import java.io.IOException;
import java.io.InputStream;

/**
 * This input stream wrapper allows to count the number of bytes read.
 *
 * @author Raffaele Canale - raffaelecanale@gmail.com
 * @version 1.0
 *          <p>
 *          Date: Jul 13, 2013
 */
public class CounterInputStream extends InputStream {

    private final InputStream subStream;
    private long count;

    /**
     * Create a new stream that will count the number of bytes read.
     *
     * @param subStream Underlying stream
     */
    public CounterInputStream(InputStream subStream) {
        this.subStream = subStream;
        this.count = 0;
    }

    @Override
    public int read() throws IOException {
        int read = subStream.read();
        if (read >= 0) {
            count++;
        }
        return read;
    }

    /**
     *
     * @return The number of bytes read
     */
    public long getCount() {
        return count;
    }
}
