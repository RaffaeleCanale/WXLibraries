package com.wx.console.file;

import com.wx.console.color.Color;
import com.wx.console.system.SystemConsole;

import java.io.*;
import java.util.Stack;

/**
 *
 * @author Canale
 */
public class HtmlConsole extends SystemConsole {
    
    private final Stack<String> tags = new Stack<>();

    public HtmlConsole(OutputStream out) {
        super(out, null);
        writeOut0("<html>");
    }

    public HtmlConsole(File file) throws FileNotFoundException {
        super(new FileOutputStream(file), null);
        writeOut0("<html>");
    }

    @Override
    public void resetBold() {
        String tag = tags.pop();
        assert "</b>".equals(tag);
        writeOut0("</b>");
    }
    
    @Override
    public void setBold() {
        writeOut0("<b>");
        tags.push("</b>");
    }
    
    @Override
    public void setBackgroundColor(Color color) {
    }
    
    @Override
    public void setColor(Color color) {
        if (!tags.empty() && tags.peek().equals("</font>")) {
            writeOut0(tags.pop());
        } else {
            emptyTagsStack();
        }
        
        writeOut0("<font color=" + color.name() + ">");
        tags.push("</font>");
    }
    
    @Override
    public void resetStyle() {
        emptyTagsStack();
    }
    
    @Override
    public void carriageReturn() {
        newLineOut0();
    }

    @Override
    protected void newLineOut0() {
        writeOut0("<br>");
    }
    @Override
    public String nextLine() {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public char nextChar() {
        throw new UnsupportedOperationException("Not supported.");
    }


    @Override
    public int nextInt() {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public double nextDouble() {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public void closeOut() {
        emptyTagsStack();
        writeOut0("</html>");
        super.closeOut();
    }


    private void emptyTagsStack() {
        while (!tags.empty()) {
            writeOut0(tags.pop());
        }
    }
}
