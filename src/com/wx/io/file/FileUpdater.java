package com.wx.io.file;

import com.wx.io.Accessor;
import com.wx.io.TextAccessor;

import java.io.*;

/**
 * Simple file text reader that allows to read (and modify) text files line per line.
 * <p>
 * Updated on 19/11/14
 *
 * @author Raffaele Canale (raffaelecanale@gmail.com)
 * @version 1.0
 */

public class FileUpdater {

    private static final long FILE_SIZE_LIMIT = 10 << 20;
    private final ReaderFunction function;

    /**
     * Construct a new file reader
     *
     * @param function Function to apply
     *
     * @see ReaderFunction
     */
    public FileUpdater(ReaderFunction function) {
        this.function = function;
    }

    /**
     * Read (apply function) to the given file.
     *
     * @param fileName File to read
     *
     * @return {@code true} if the file has been changed
     *
     * @throws FileNotFoundException
     * @throws IOException
     */
    public boolean update(String fileName) throws FileNotFoundException, IOException {
        return update(new File(fileName));
    }

    /**
     * Read (apply function) to the given file.
     *
     * @param file File to read
     *
     * @return {@code true} if the file has been changed
     *
     * @throws FileNotFoundException
     * @throws IOException
     */
    public boolean update(File file) throws FileNotFoundException, IOException {
        if (file.length() > FILE_SIZE_LIMIT) {
            throw new IOException("File too big, function not implemented yet");
        }

        return updateUsingMemory(file);
    }

    private boolean updateUsingMemory(File file) throws FileNotFoundException, IOException {
        boolean changed = false;
        ByteArrayOutputStream tmpBytes = new ByteArrayOutputStream();
        try (TextAccessor accessor = new TextAccessor().setIn(file).setOut(tmpBytes)) {

            String line;
            String newLine;
            int lineNo = 0;
            while ((line = accessor.readLine()) != null) {
                newLine = function.update(line, ++lineNo);

                if (newLine == null) {
                    changed = true;
                } else {
                    if (!newLine.equals(line)) {
                        changed = true;
                    }

                    accessor.write(newLine);
                }
            }
        }

        if (changed) {
            ByteArrayInputStream bais = new ByteArrayInputStream(tmpBytes.toByteArray());
            try (Accessor accessor = new Accessor().setIn(bais).setOut(file, false)) {
                accessor.pourInOut();
            }
        }

        return changed;
    }
}
