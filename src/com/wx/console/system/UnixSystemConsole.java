/*
 * Copyright (C) 2012 Raffaele Canale - raffaelecanale@gmail.com
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.wx.console.system;

import com.wx.console.color.Color;
import com.wx.console.system.SystemConsole;

import java.io.*;

/**
 *
 * @author Raffaele Canale - raffaelecanale@gmail.com
 * @version 0.1
 *
 * Project: WXLibraries
 * File: SystemConsole2.java (UTF-8)
 * Date: 14 oct. 2012
 */
public class UnixSystemConsole extends SystemConsole {

    public UnixSystemConsole() {
        super();
    }

    public UnixSystemConsole(OutputStream out, OutputStream err, InputStream in) {
        super(out, err, in);
    }

    public UnixSystemConsole(OutputStream singleOutput, InputStream in) {
        super(singleOutput, in);
    }

    @Override
    public void setBold() {
        writeOut0("\u001B[1m");
    }

    @Override
    public void resetBold() {
        writeOut0("\u001B[22m");
    }

    @Override
    public void setBackgroundColor(Color color) {
        writeOut0("\u001B[4" + color.ordinal() + "m");
    }

    @Override
    public void setBackgroundCustomColor(int r, int g, int b) {
        writeOut0("\u001B[48;2;" + r + ";" + g + ";" + b  + "m");
    }

    @Override
    public void resetBackgroundColor() {
        writeOut0("\u001B[49m");
    }

    @Override
    public void setColor(Color color) {
        writeOut0("\u001B[3" + color.ordinal() + "m");
    }

    @Override
    public void resetColor() {
        writeOut0("\u001B[39m");
    }

    @Override
    public void setItalic() {
        writeOut0("\u001B[3m");
    }

    @Override
    public void resetItalic() {
        writeOut0("\u001B[23m");
    }

    @Override
    public void setUnderlined() {
        writeOut0("\u001B[4m");
    }

    @Override
    public void resetUnderlined() {
        writeOut0("\u001B[24m");
    }

    @Override
    public void resetStyle() {
        writeOut0("\u001B[0m");
        flushOut();
    }

    @Override
    public boolean carriageReturnSupported() {
        return true;
    }

    @Override
    public void carriageReturn() {
        writeOut0("\r\033[K");
    }

    @Override
    public void probeWidthBuffer() {
        int width = -1;

        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("nix") || os.contains("nux") || os.contains("aix")
                || os.contains("mac")) {
            try {
                Process process = Runtime.getRuntime().exec(new String[]{
                    "bash", "-c", "tput cols 2> /dev/tty"});
                int result = process.waitFor();
                if (result == 0) {
                    try (BufferedReader reader = 
                            new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                        String value = reader.readLine();
                        
                        width = Integer.parseInt(value);
                    }
                }
            } catch (IOException | NumberFormatException | InterruptedException ex) {
                // Ignored
            }
        }

        setWidthBuffer(width);
    }

}
