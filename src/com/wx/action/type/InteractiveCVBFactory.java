package com.wx.action.type;

import com.wx.action.util.PropertyContainer;
import com.wx.action.command.Command;
import com.wx.console.UserConsoleInterface;
import com.wx.util.representables.TypeCaster;

/**
 * Created on 17/03/2015
 *
 * @author Raffaele Canale (raffaelecanale@gmail.com)
 * @version 0.1
 */
public class InteractiveCVBFactory extends CVBFactory {

    private final CasterCVBFactory casterCVBFactory;
    private final UserConsoleInterface in;
    private boolean currentlyUseInteractive;

    public InteractiveCVBFactory(CasterCVBFactory casterCVBFactory, UserConsoleInterface in) {
        this.casterCVBFactory = casterCVBFactory;
        this.in = in;
    }

    @Override
    public void notifyStart(Command cmd) {
        casterCVBFactory.notifyStart(cmd);

        currentlyUseInteractive = cmd.length() == 0;
    }

    @Override
    public void notifyEnd(Command cmd) {
        casterCVBFactory.notifyEnd(cmd);
    }

    @Override
    public CVB<Object> getFromType(PropertyContainer info) {
        if (currentlyUseInteractive) {
            TypeCaster<String, ?> caster = casterCVBFactory.getCasterForProperty(info);
            return new InteractiveCVB<>(info, caster, in);

        } else {
            return casterCVBFactory.getFromType(info);
        }
    }
}
