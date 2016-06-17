package com.wx.action.type;

import com.wx.action.util.PropertyContainer;
import com.wx.action.command.Command;

/**
 * Created on 10/02/2015
 *
 * @author Raffaele Canale (raffaelecanale@gmail.com)
 * @version 0.1
 */
public abstract class CVBFactory {

    private static CVBFactory solver;

    public static CVBFactory instance() {
        if (solver == null) {
            setInstance(new CasterCVBFactory());
        }

        return solver;
    }

    public static void setInstance(CVBFactory solver) {
        CVBFactory.solver = solver;
    }

    public void notifyStart(Command cmd) {}

    public void notifyEnd(Command cmd) {}

    public abstract CVB<Object> getFromType(PropertyContainer info);


}
