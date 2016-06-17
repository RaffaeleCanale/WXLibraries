package com.wx.io.file;

import java.io.IOException;

/**
 * Function to apply (line per line) to the text file.
 *
 * @see FileUpdater
 *
 * @author Canale
 */
@FunctionalInterface
public interface ReaderFunction {

    /**
     * Process the current read line. This function can return:
     * <br>
     * <ul>
     * <li>A new line that will be affected to the file</li>
     * <li>The same line, so no changes are done</li>
     * <li>{@code null} and this line will be omitted</li>
     * </ul>
     *
     * @param line   Read line
     * @param lineNo Line number
     *
     * @return Replacement for that line, or null to stop reading
     *
     * @throws IOException
     */
    public String update(final String line, final int lineNo) throws IOException;
}
