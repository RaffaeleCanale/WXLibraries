package com.wx.console;

import com.wx.console.color.Color;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.Collection;

/**
 *
 * @author Raffaele Canale - raffaelecanale@gmail.com
 * @version 0.1
 *
 * Project: WXLibraries File: Console.java (UTF-8) Date: 13 oct. 2012
 */
public abstract class Console  {

    /*
     * NOTE:
     * Uncommented
     * Untested
     */
    private int lineSize;
    private StringBuilder spacePrefix = new StringBuilder();
    private boolean prefixFinished;
    private Integer widthBuffer;

    //<editor-fold defaultstate="collapsed" desc="Out methods">
    public void print(boolean b) {
        writeOut(b ? "true" : "false");
    }

    public void print(char c) {
        writeOut(String.valueOf(c));
    }

    public void print(int i) {
        writeOut(String.valueOf(i));
    }

    public void print(long l) {
        writeOut(String.valueOf(l));
    }

    public void print(float f) {
        writeOut(String.valueOf(f));
    }

    public void print(double d) {
        writeOut(String.valueOf(d));
    }

    public void print(char s[]) {
        writeOut(String.valueOf(s));
    }

    public void print(String s) {
        if (s == null) {
            s = "null";
        }
        writeOut(s);
    }

    public void print(Object obj) {
        writeOut(String.valueOf(obj));
    }

    public void print(Object[] objs) {
        writeOut(Arrays.toString(objs));
    }

    public void println() {
        newLineOut();
    }

    public synchronized void println(boolean x) {
        print(x);
        newLineOut();
    }

    public synchronized void println(char x) {
        print(x);
        newLineOut();
    }

    public synchronized void println(int x) {
        print(x);
        newLineOut();
    }

    public synchronized void println(long x) {
        print(x);
        newLineOut();
    }

    public synchronized void println(float x) {
        print(x);
        newLineOut();
    }

    public synchronized void println(double x) {
        print(x);
        newLineOut();
    }

    public synchronized void println(char x[]) {
        print(x);
        newLineOut();
    }

    public synchronized void println(String x) {
        print(x);
        newLineOut();
    }

    public synchronized <E> void println(Collection<E> collection) {
        if (collection == null) {
            print("null");
            newLineOut();
        } else {
            for (E e : collection) {
                print(e);
                newLineOut();
            }
        }
    }

    public synchronized void println(Object x) {
        print(String.valueOf(x));
        newLineOut();
    }

    public synchronized void println(Object[] objs) {
        print(objs);
        newLineOut();
    }

    public synchronized void println(Object[] objs, String prefix) {
        if (objs == null) {
            writeOut("null");
        } else {
            for (Object obj : objs) {
                writeOut(prefix + String.valueOf(obj));
                newLineOut();
            }
        }
    }

    protected void writeOut(String s) {
        int consoleWidth = getConsoleWidth();
        if (consoleWidth < 0) {
            writeOut0(s);
            return;
        }
        
        if (!prefixFinished) {
            prefixFinished = extractPrefixSpaces(s);            
        }
        
        incrementLineSize(s);

        if (lineSize >= consoleWidth) {
            if (!prefixFinished) { // No text
                writeOut0(s.substring(0, consoleWidth));
                newLineOut();
                s = s.substring(consoleWidth, lineSize);
                this.writeOut(s);
                return;
            }
            
            int cutIndex = findCut(consoleWidth, s);
            String prefix = spacePrefix.toString();
//            System.out.println("P " + prefix.length());
//            System.out.println("CUT " + lineSize + "/" + consoleWidth + " AT " + cutIndex + " from " + s);
            writeOut0(s.substring(0, cutIndex));
            newLineOut0();
            writeOut0(prefix);
            
//            spacePrefix.append(prefix);
            lineSize = 0;
            incrementLineSize(prefix);
            assert prefixFinished;

            this.writeOut(trimLeft(s.substring(cutIndex, s.length())));
            
        } else {
//            System.out.println("NO CUT (" + lineSize + "/" + consoleWidth + "): " + s.length());
            writeOut0(s);
        }
    }
    
    private void incrementLineSize(String value) {
//        for (char c : value.toCharArray()) {
//            if (c == '\t') {
//                lineSize += 4;
//            } else {
//                lineSize += 1;
//            }
//        }
        lineSize += value.length();
    }
    
    private String trimLeft(String value) {
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            if (!isSpace(c)) {
                return value.substring(i);
            }
        }
        
        return value;
    }

    private static boolean isSpace(char c) {
        return c == ' ';
    }
    
    private int findCut(int consoleWidth, String value) {
        int lastPossibleChar = value.length() - (lineSize - consoleWidth) - 1;
        assert lastPossibleChar >= 0 && lastPossibleChar < value.length();
        
        for (int i = lastPossibleChar; i > 0; i--) {
            char c = value.charAt(i);
            if (isSpace(c)) {
//                System.out.println("FOUND CUT at " + value.substring(i - 1, i + 2));
                return i;
            }
        }
        
        return lastPossibleChar;
    }
    
    private boolean extractPrefixSpaces(String value) {
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            if (isSpace(c)) {
                spacePrefix.append(c);
            } else if (c == '-') {
                spacePrefix.append(' ');
            } else {
                return true;
            }
        }
        return false;
    }

    protected abstract void writeOut0(String s);

    protected void newLineOut() {
        spacePrefix = new StringBuilder();
        prefixFinished = false;
        lineSize = 0;
        newLineOut0();
    }
    
    protected abstract void newLineOut0();

    public abstract void closeOut();

    public abstract void flushOut();
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Err methods">
    public void err(boolean b) {
        writeErr(b ? "true" : "false");
    }

    public void err(char c) {
        writeErr(String.valueOf(c));
    }

    public void err(int i) {
        writeErr(String.valueOf(i));
    }

    public void err(long l) {
        writeErr(String.valueOf(l));
    }

    public void err(float f) {
        writeErr(String.valueOf(f));
    }

    public void err(double d) {
        writeErr(String.valueOf(d));
    }

    public void err(char s[]) {
        writeErr(String.valueOf(s));
    }

    public void err(String s) {
        if (s == null) {
            s = "null";
        }
        writeErr(s);
    }

    public void err(Object obj) {
        writeErr(String.valueOf(obj));
    }

    public void err(Object[] objs) {
        if (objs == null) {
            writeErr("null");
        } else {
            boolean first = true;
            for (Object obj : objs) {
                if (first) {
                    writeErr(String.valueOf(obj));
                } else {
                    writeErr(", " + String.valueOf(obj));
                }
            }
        }
    }

    public void errln() {
        newLineErr();
    }

    public synchronized void errln(boolean x) {
        err(x);
        newLineErr();
    }

    public synchronized void errln(char x) {
        err(x);
        newLineErr();
    }

    public synchronized void errln(int x) {
        err(x);
        newLineErr();
    }

    public synchronized void errln(long x) {
        err(x);
        newLineErr();
    }

    public synchronized void errln(float x) {
        err(x);
        newLineErr();
    }

    public synchronized void errln(double x) {
        err(x);
        newLineErr();
    }

    public synchronized void errln(char x[]) {
        err(x);
        newLineErr();
    }

    public synchronized void errln(String x) {
        err(x);
        newLineErr();
    }

    public synchronized void errln(Object x) {
        err(String.valueOf(x));
        newLineErr();
    }

    public synchronized void errln(Object[] objs) {
        err(objs);
        newLineErr();
    }

    public synchronized void errln(Object[] objs, String prefix) {
        if (objs == null) {
            writeErr("null");
        } else {
            for (Object obj : objs) {
                writeErr(prefix + String.valueOf(obj));
                newLineErr();
            }
        }
    }

    protected abstract void writeErr(String s);

    protected abstract void newLineErr();

    public abstract void closeErr();

    public abstract void flushErr();
    //</editor-fold>

    public void flush() {
        flushOut();
        flushErr();
    }

    public void close() {
        closeOut();
        closeErr();
        closeInput();
    }

    public abstract String nextLine();

    public abstract char nextChar();

    public abstract int nextInt();

    public abstract double nextDouble();

    public abstract void closeInput();

    public void resetBold() {}

    public void setBold() {}

    public void setBackgroundColor(Color color) {}

    public void setBackgroundCustomColor(int r, int g, int b) {}

    public void resetBackgroundColor() {}

    public void setColor(Color color) {}

    public void resetColor() {}

    public void resetStyle() {}

    public void setItalic() {}

    public void resetItalic() {}

    public void setUnderlined() {}

    public void resetUnderlined() {}

    public boolean carriageReturnSupported() {
        return false;
    }

    public void carriageReturn() {}

    public char[] readPassword() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void probeWidthBuffer() {
        setWidthBuffer(-1);
    }
    
    public int getConsoleWidth() {
        if (widthBuffer == null) {
            probeWidthBuffer();
        }
        return widthBuffer;
    }

    protected void setWidthBuffer(int widthBuffer) {
        this.widthBuffer = widthBuffer;
    }
}
