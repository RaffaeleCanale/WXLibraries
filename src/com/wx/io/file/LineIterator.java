package com.wx.io.file;

import com.wx.io.Accessor;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;

/**
 *
 * @author Raffaele Canale (raffaelecanale@gmail.com)
 * @version 0.1
 *
 * @deprecated
 */
@Deprecated
public class LineIterator {

    private final Accessor accessor;
    private int lineCount;
    private String nextLine;

    public LineIterator(String file) throws FileNotFoundException, IOException {
        this(new File(file));
    }

    public LineIterator(File file) throws FileNotFoundException, IOException {
        this.accessor = new Accessor().setIn(file);
        nextLine = accessor.readLine();
        lineCount = 0;
    }

    public String peek() {
        return nextLine;
    }

    public boolean hasNext() {
        return nextLine != null;
    }

    public String next() throws IOException {
        if (!hasNext()) {
            throw new IllegalStateException("Does not have next");
        }
        lineCount++;
        String result = nextLine;
        nextLine = accessor.readLine();
        return result;
    }

    public int getLineCount() {
        return lineCount;
    }
}
