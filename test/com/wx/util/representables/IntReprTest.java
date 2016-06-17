/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wx.util.representables;

import com.wx.util.representables.string.IntRepr;
import org.junit.Test;

/**
 *
 * @author Raffaele Canale - raffaelecanale@gmail.com
 */
public class IntReprTest extends GenericCasterTest<String, Integer> {

    @Override
    protected TypeCaster<String, Integer> getCaster() {
        return new IntRepr();
    }

    @Override
    protected void setUpPairs() {
        pair("24", 24);
        pair("0", 0);
        pair("-234", -234);
        
        invalidInput("");
        invalidInput("hello");
        invalidInput("23.2");
        invalidInput("9223372036854775807"); // Long.MAX_VALUE
    }
}
