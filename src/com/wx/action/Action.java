package com.wx.action;

import com.wx.action.arg.ArgumentsSupplier;
import com.wx.console.UserConsoleInterface;

/**
 * Created by canale on 11/6/14.
 */
public interface Action {
    void execute(UserConsoleInterface in, ArgumentsSupplier args);
}
