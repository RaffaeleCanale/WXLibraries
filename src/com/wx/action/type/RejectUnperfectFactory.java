package com.wx.action.type;

import com.wx.action.util.PropertyContainer;
import com.wx.action.command.Command;

/**
 * Created on 17/03/2015
 *
 * @author Raffaele Canale (raffaelecanale@gmail.com)
 * @version 0.1
 */
public class RejectUnperfectFactory extends CVBFactory {

    private final CVBFactory subFactory;

    public RejectUnperfectFactory(CVBFactory subFactory) {
        this.subFactory = subFactory;
    }


    @Override
    public CVB<Object> getFromType(PropertyContainer info) {
        return subFactory.getFromType(info);
    }

    @Override
    public void notifyStart(Command cmd) {
        subFactory.notifyStart(cmd);
    }

    @Override
    public void notifyEnd(Command cmd) {
        subFactory.notifyEnd(cmd);

        if (cmd.length() > 0) {
            throw new IllegalArgumentException("Unrecognised options: " + cmd.stackFrom(0));
        }
    }
}
