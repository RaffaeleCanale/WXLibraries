package com.wx.action;

import com.wx.action.arg.ArgumentsSupplier;
import com.wx.console.UserConsoleInterface;

import java.io.IOException;

/**
 * Created on 16/03/2015
 *
 * @author Raffaele Canale (raffaelecanale@gmail.com)
 * @version 0.1
 */
public interface IoAction extends Action {

    @Override
    default void execute(UserConsoleInterface in, ArgumentsSupplier args) {
        try {
            executeIo(in, args);
        } catch (IOException e) {
            in.getConsole().errln("An IO exception occurred: [" + e.getClass().getSimpleName() + "] " + e.getMessage());
        }
    }

    void executeIo(UserConsoleInterface in, ArgumentsSupplier args) throws IOException;


}
