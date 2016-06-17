/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wx.util.representables;

import com.wx.util.representables.string.BooleanRepr;

/**
 *
 * @author Raffaele Canale - raffaelecanale@gmail.com
 */
public class BooleanReprTest extends GenericCasterTest<String, Boolean> {

    @Override
    protected TypeCaster<String, Boolean> getCaster() {
        return new BooleanRepr();
    }

    @Override
    protected void setUpPairs() {
        pair("true", true);
        pair("false", false);

        outPair("TRUE", true);
        outPair("TrUe", true);
        outPair("FALSE", false);
        outPair("FaLsE", false);
        outPair("default value", false);
    }
}
