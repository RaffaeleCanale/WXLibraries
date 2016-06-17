package com.wx.action.type;

import com.wx.action.command.Command;

import java.util.List;

/**
 * Created on 17/03/2015
 *
 * @author Raffaele Canale (raffaelecanale@gmail.com)
 * @version 0.1
 */
public interface CVB<Type> {

    public Command registerFrom(Command cmd);

    public boolean isList();

    public Type getValue();

    public List<Type> getValues();


}
