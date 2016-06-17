package com.wx.console.system;

import com.wx.console.Console;
import com.wx.console.color.Color;
import jline.console.ConsoleReader;
import jline.console.completer.Completer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created on 30/03/2015
 *
 * @author Raffaele Canale (raffaelecanale@gmail.com)
 * @version 0.1
 */
public class JLineConsole extends Console {

    final ConsoleReader reader;
    private boolean outClosed;
    private boolean inClosed;
    private boolean errClosed;

    public JLineConsole() throws IOException {
        this(System.in, System.out);
    }

    public JLineConsole(InputStream in, OutputStream out) throws IOException {
        this.reader = new ConsoleReader(in, out);
    }

    public boolean addCompleter(Completer completer) {
        return reader.addCompleter(completer);
    }

    @Override
    public void probeWidthBuffer() {
        setWidthBuffer(reader.getTerminal().getWidth());
    }

    @Override
    public char[] readPassword() {
        try {
            return reader.readLine((char) 0).toCharArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void writeOut0(String s) {
        try {
            reader.print(s);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void newLineOut0() {
        try {
            reader.println();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void closeOut() {
        outClosed = true;
        tryClose();
    }

    @Override
    public void flushOut() {
        try {
            reader.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void writeErr(String s) {
        writeOut0(s);
    }

    @Override
    protected void newLineErr() {
        newLineOut0();
    }

    @Override
    public void closeErr() {
        errClosed = true;
        tryClose();
    }

    @Override
    public void flushErr() {
        flushOut();
    }

    @Override
    public String nextLine() {
        try {
            return reader.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public char nextChar() {
        try {
            print(reader.getPrompt());
            flushOut();
            char c = (char) reader.readCharacter();
            println();
            return c;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int nextInt() {
        return Integer.parseInt(nextLine());
    }

    @Override
    public double nextDouble() {
        return Double.parseDouble(nextLine());
    }

    @Override
    public void closeInput() {
        inClosed = true;
        tryClose();
    }

    private void tryClose() {
        if (inClosed && errClosed && outClosed) {
            reader.shutdown();
        }
    }

}
