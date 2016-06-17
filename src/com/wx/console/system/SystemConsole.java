package com.wx.console.system;

import com.wx.console.Console;

import java.io.*;
import java.util.NoSuchElementException;

/**
 *
 * @author Raffaele Canale - raffaelecanale@gmail.com
 * @version 0.1
 *
 * Project: WXLibraries
 * File: GenericConsole.java (UTF-8)
 * Date: 13 oct. 2012 
 */
public class SystemConsole extends Console {
    
    private final PrintWriter outWriter;
    private final PrintWriter errWriter;
    private final DataInputStream dis;

    public SystemConsole() {
        this(System.out, System.err, System.in);
    }

    public SystemConsole(OutputStream out, OutputStream err, InputStream in) {
        outWriter = new PrintWriter(out, true);
        errWriter = new PrintWriter(err, true);
//        scanner = in == null ? null : new Scanner(in);
        dis = in == null ? null : new DataInputStream(in);
    }

    public SystemConsole(OutputStream singleOutput, InputStream in) {
        this(singleOutput, singleOutput, in);
    }

    @Override
    protected void writeOut(String s) {
        s = s.replaceAll("\t", "    ");
        String[] lines = s.split("\n");

        super.writeOut(lines[0]);
        for (int i = 1; i < lines.length; i++) {
            newLineOut();
            super.writeOut(lines[i]);
        }
    }
    
    @Override
    protected void writeOut0(String s) {
        outWriter.write(s);
//        outWriter.write(s);
//        String[] lines = s.split("\n");
//        
//        outWriter.write(lines[0]);
//        for (int i = 1; i < lines.length; i++) {
//            newLineOut();
//            outWriter.write(lines[i]);
//        }
    }

    @Override
    public char[] readPassword() {
        final java.io.Console c = System.console(); // TODO There is a glitch since this refers to System.in and SystemConsole is not generally the case
        if (c == null) {
            print("**VISIBLE**");
            flush();
            return nextLine().toCharArray();
        }

        return c.readPassword();
    }

    @Override
    protected void newLineOut0() {
        outWriter.println();
    }

    @Override
    public void closeOut() {
        outWriter.close();
    }

    @Override
    public void flushOut() {
        outWriter.flush();
    }

    @Override
    protected void writeErr(String s) {
        errWriter.write(s);
    }

    @Override
    protected void newLineErr() {
        errWriter.println();
    }

    @Override
    public void closeErr() {
        errWriter.close();
    }

    @Override
    public void flushErr() {
        errWriter.flush();
    }

    @Override
    public String nextLine() {
        StringBuilder result = new StringBuilder();

//        try {
//            InputStreamReader r = new InputStreamReader(dis, "UTF-8");
//
//
//            System.out.println("Waiting for line...");
//            char lastChar = (char) dis.read();
//            System.out.println("First read: " + lastChar);
//            while (lastChar != '\n' && lastChar != '\r') {
//                System.out.println("DECODED " + ((int) lastChar));
//                result.append(lastChar);
//                lastChar = (char) r.read();
//            }
//
//            return result.toString();
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        try {
            char lastChar = (char) dis.readByte();
            while (lastChar != '\n' && lastChar != '\r') {
                result.append(lastChar);
                lastChar = (char) dis.readByte();
            }

            return result.toString();

//            System.out.println("Reading line...");
//            String s = dis.readLine();
//            System.out.println("Line "+ s + " has been returned, not my fault...");
//            return s;
        } catch (EOFException e) {
            if (result.length() == 0) {
                throw new NoSuchElementException();
            }

            return result.toString();
        } catch (IOException e) {
            throw new NoSuchElementException();
        }
    }

    @Override
    public char nextChar() {
        String s = nextLine();
        while (s.isEmpty()) {
            err("Please enter at least 1 character: ");
            flushErr();
            s = nextLine();
        }
        
        return s.charAt(0);
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
        if (dis != null) {
            try {
                dis.close();
            } catch (IOException e) {
                // Ignored
            }
        }
    }    
    
}
