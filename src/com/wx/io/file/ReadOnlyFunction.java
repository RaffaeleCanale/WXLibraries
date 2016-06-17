package com.wx.io.file;

import java.io.IOException;

/**
 * Simple ReaderFunction that is read-only
 *
 * @author Canale
 */
public abstract class ReadOnlyFunction implements ReaderFunction {

    @Override
    public final String update(String line, int lineNo) throws IOException {
        read(line, lineNo);
        return line;
    }

    /**
     * Process read line.
     *
     * @param line   Read line
     * @param lineno Line number
     *
     * @throws IOException
     */
    public abstract void read(String line, int lineno) throws IOException;
}
