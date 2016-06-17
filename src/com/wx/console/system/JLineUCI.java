package com.wx.console.system;

import com.wx.console.UserConsoleInterface;

import java.io.IOException;

/**
 * Created on 18/03/2015
 *
 * @author Raffaele Canale (raffaelecanale@gmail.com)
 * @version 0.1
 */
public class JLineUCI extends UserConsoleInterface {

    public JLineUCI(UnixJLineConsole console, String mPrefix, String mSep) throws IOException {
        super(console, mPrefix, mSep);
    }

    public JLineUCI(String mPrefix, String mSep) throws IOException {
        super(new UnixJLineConsole(), mPrefix, mSep);
    }

    @Override
    protected void printPrefix(String prefix) {
        ((UnixJLineConsole) getConsole()).reader.setPrompt(prefix);
    }
}
