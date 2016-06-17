package com.wx.action.cmd;

import com.wx.console.UserConsoleInterface;

/**
 * Created on 31/03/2015
 *
 * @author Raffaele Canale (raffaelecanale@gmail.com)
 * @version 0.1
 */
public interface CommandLoader {

    public GenericCommand inputCommand(UserConsoleInterface in);

}
