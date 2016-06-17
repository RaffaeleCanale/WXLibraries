package com.wx.util.representables.string;

import com.wx.util.representables.TypeCaster;

/**
 *
 * @author Raffaele
 */
public class StringRepr implements TypeCaster<String, String> {

    @Override
    public String castIn(String e) {
        return e;
    }

    @Override
    public String castOut(String value) {
        return value;
    }

    @Override
    public TypeCaster<String, String> nullable() {
        return this;
    }
}
