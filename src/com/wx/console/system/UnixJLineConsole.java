package com.wx.console.system;

import com.wx.console.color.Color;
import jline.console.ConsoleReader;
import jline.console.completer.Completer;

import java.io.*;

/**
 * Created on 18/03/2015
 *
 * @author Raffaele Canale (raffaelecanale@gmail.com)
 * @version 0.1
 */
public class UnixJLineConsole extends JLineConsole {

    public UnixJLineConsole() throws IOException {
        this(System.in, System.out);
    }

    public UnixJLineConsole(InputStream in, OutputStream out) throws IOException {
        super(in, out);
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

}
