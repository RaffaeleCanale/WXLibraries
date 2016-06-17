package com.wx.action.type;

import com.wx.action.util.PropertyContainer;
import com.wx.util.representables.TypeCaster;
import com.wx.util.representables.string.*;
import com.wx.util.representables.string.DirectoryRepr;
import com.wx.util.representables.wrapper.FileRepr;

/**
 * Created on 10/02/2015
 *
 * @author Raffaele Canale (raffaelecanale@gmail.com)
 * @version 0.1
 */
public class CasterCVBFactory extends CVBFactory {

    public TypeCaster<String, ?> getCasterForProperty(PropertyContainer info) {
        switch (info.getType().toLowerCase()) {
            case "enum":
            case "string":
                return new StringRepr();
            case "int":
            case "integer":
                return new IntRepr();
            case "double":
                return new DoubleRepr();
            case "bool":
            case "boolean":
                return new BooleanRepr();
            case "file":
                return new FileRepr();
            case "directory":
                return new DirectoryRepr(true);
            default:
                throw new UnsupportedOperationException("No caster found for " + info.getType());
        }
    }

    @Override
    public DefaultCVB<Object> getFromType(PropertyContainer info) {
        return new DefaultCVB<>(info, getCasterForProperty(info));
    }

}
