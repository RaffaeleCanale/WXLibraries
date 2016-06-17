package com.wx.action.cmd;

import com.wx.action.arg.ArgumentsSupplier;
import com.wx.action.util.CommandContainer;

/**
 * Created on 31/03/2015
 *
 * @author Raffaele Canale (raffaelecanale@gmail.com)
 * @version 0.1
 */
public interface GenericCommand {

    public String getIdentifier();

    public ArgumentsSupplier getArguments(CommandContainer commandDescriptor);

}
