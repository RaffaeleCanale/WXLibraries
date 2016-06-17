//package com.wx.console.file;
//
//import com.wx.action.command.Command;
//import com.wx.console.Console;
//
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.PrintWriter;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//
///**
// *
// * @author Raffaele
// */
//public class LogConsole extends Console {
//
//    private boolean logInput;
//    private SimpleDateFormat sdf;
//    private Console console;
//
//    private StringBuffer outBuffer;
//    private StringBuffer errBuffer;
//    private StringBuffer inBuffer;
//
//    private PrintWriter fileWriter;
//    private boolean isOutClosed;
//    private boolean isErrClosed;
//
//    public LogConsole(String file, Console console, boolean logInput, boolean append) throws FileNotFoundException {
//
//        this.console = console;
//        this.logInput = logInput;
//        this.outBuffer = new StringBuffer("");
//        this.errBuffer = new StringBuffer("");
//        this.inBuffer = new StringBuffer("");
//        this.fileWriter = new PrintWriter(new FileOutputStream(file, append));
//
//        sdf = new SimpleDateFormat("EEEE, dd MMMM yyyy - HH:mm:ss");
//        write(null, "\n--------------------------------------------------------------------\n", outBuffer);
//        write(null, "-------- NEW SESSION " + sdf.format(new Date()) + " ----------\n", outBuffer);
//        write(null, "--------------------------------------------------------------------\n\n", outBuffer);
//        sdf = new SimpleDateFormat("HH:mm:ss");
//    }
//
//    public LogConsole(String file, Console console, boolean logInput, boolean append, String[] args) throws FileNotFoundException {
//        this(file, console, logInput, append);
//        String line = "";
//        for(String arg : args) {
//            line+= arg + " ";
//        }
//        write("ARGS:", line + "\n", outBuffer);
//    }
//
//    public LogConsole(String file, Console console, boolean logInput, boolean append, Command args) throws FileNotFoundException {
//        this(file, console, logInput, append);
//        write("ARGS:", args + "\n", outBuffer);
//    }
//
//
//    public void closeLog() {
//        if(errBuffer.length() != 0) {
//            flushLine(errBuffer);
//        }
//        if(outBuffer.length() != 0) {
//            flushLine(outBuffer);
//        }
//        if(inBuffer.length() != 0) {
//            flushLine(inBuffer);
//        }
//
//        isErrClosed = true;
//        isOutClosed = true;
//        fileWriter.close();
//    }
//
//    private String performNewLinePrefix(String prefix) {
//        return prefix == null ? "" : sdf.format(new Date()) + " " + prefix + " ";
//    }
//    private void write(String prefix, String value, StringBuffer lineBuffer) {
//        String[] lines = value.split("\n");
//        boolean endsWithNewLine = value.endsWith("\n");
//
//        int i = 0;
//        for (String line : lines) {
//            if (i == 0) {
//                if (lineBuffer.length() == 0) {
//                    lineBuffer.append(performNewLinePrefix(prefix));
//                }
//            }
//            lineBuffer.append(line);
//            if(i < lines.length - 1 || endsWithNewLine) {
//                flushLine(lineBuffer);
//            }
//            i++;
//        }
//
//        if(lineBuffer.length() == 0) {
//            lineBuffer.append(performNewLinePrefix(prefix));
//        }
//    }
//    /*
//    private void write2(String prefix, String value) {
//        String blank = prefix == null ? "" : "               ";
//
//        if(!lineBuffer.isEmpty()) {
//            prefix = lineBuffer;
//        } else {
//            prefix = prefix == null ? "" : sdf.format(new Date()) + " " + prefix + " ";
//        }
//
//        int i = 0;
//        String[] lines = value.split("\n");
//        boolean endsWithNewLine = value.endsWith("\n");
//
//        for (String line : lines) {
//            if (i == lines.length - 1 && !endsWithNewLine) {
//                // LAST LINE && UNFINISHED LINE
//                lineBuffer = (i == 0 ? prefix : blank) + line;
//            } else {
//                // ANY FINISHED LINE
//                line = (i == 0 ? prefix : blank) + line;
//                writeLine(line);
//            }
//            i++;
//        }
//
//    }//*/
//
//    private void flushLine(StringBuffer lineBuffer) {
//        fileWriter.println(lineBuffer.toString());
//        lineBuffer.setLength(0);
//    }
//    /*
//    private void write(String prefix, String value) {
//        prefix = prefix == null ? "" : sdf.format(new Date()) + " " + prefix + " ";
//        String blank = "                ";
//        try {
//            String[] lines = value.split("\n");
//            int i = 0;
//            for(String line : lines) {
//                if(isNewLine) {
//                    if(i == 0) line = prefix + line;
//                    else line = blank + line;
//                    isNewLine = false;
//                }
//                writer.write(line, 0, line.length());
//                if(i < line.length() - 1 || line.endsWith("\n")) {
//                    writer.newLine();
//                    isNewLine = true;
//                }
//                i++;
//            }
//        } catch (IOException ex) {
//            console.errln("Log failure: " + ex);
//        }
//    }//*/
//
//    @Override
//    protected void writeOut0(String s) {
//        write("INPT:", s, outBuffer);
//        console.writeOut0(s);
//    }
//    @Override
//    protected void newLineOut0() {
//        flushLine(outBuffer);
//        console.newLineOut0();
//    }
//
//    @Override
//    public void closeOut() {
//        isOutClosed = true;
//        if(isErrClosed) {
//            closeLog();
//        }
//
//        console.closeOut();
//    }
//
//    @Override
//    public void flushOut() {
//        console.flushOut();
//    }
//
//    @Override
//    protected void writeErr(String s) {
//        write("ERR :", s, errBuffer);
//        console.writeErr(s);
//    }
//
//    @Override
//    protected void newLineErr() {
//        flushLine(errBuffer);
//        console.newLineErr();
//    }
//
//
//    @Override
//    public void closeErr() {
//        isErrClosed = true;
//        if(isOutClosed) {
//            closeLog();
//        }
//
//        console.closeErr();
//    }
//
//    @Override
//    public void flushErr() {
//        console.flushErr();
//    }
//
//    @Override
//    public String nextLine() {
//        String input = console.nextLine();
//        if(logInput) {
//            write("INPT:", input + "\n", inBuffer);
//        }
//        return input;
//    }
//
//    @Override
//    public boolean nextBoolean() {
//        boolean input = console.nextBoolean();
//        if(logInput) {
//            write("INPT:", input + "\n", inBuffer);
//        }
//        return input;
//    }
//
//    @Override
//    public char nextChar() {
//        char input = console.nextChar();
//        if(logInput) {
//            write("INPT:", input + "\n", inBuffer);
//        }
//        return input;
//    }
//
//    @Override
//    public long nextLong() {
//        long input = console.nextLong();
//        if(logInput) {
//            write("INPT:", input + "\n", inBuffer);
//        }
//        return input;
//    }
//
//    @Override
//    public float nextFloat() {
//        float input = console.nextFloat();
//        if(logInput) {
//            write("INPT:", input + "\n", inBuffer);
//        }
//        return input;
//    }
//
//    @Override
//    public int nextInt() {
//        int input = console.nextInt();
//        if(logInput) {
//            write("INPT:", input + "\n", inBuffer);
//        }
//        return input;
//    }
//
//    @Override
//    public double nextDouble() {
//        double input = console.nextDouble();
//        if(logInput) {
//            write("INPT:", input + "\n", inBuffer);
//        }
//        return input;
//    }
//
//    @Override
//    public void closeInput() {
//        console.closeInput();
//    }
//
//}
