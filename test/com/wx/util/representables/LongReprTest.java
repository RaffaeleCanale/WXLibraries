/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wx.util.representables;

import com.wx.util.representables.string.LongRepr;
import org.junit.Test;

/**
 *
 * @author Raffaele Canale - raffaelecanale@gmail.com
 */
public class LongReprTest extends GenericCasterTest<String, Long> {

    @Override
    protected TypeCaster<String, Long> getCaster() {
        return new LongRepr();
    }

    @Override
    protected void setUpPairs() {
        pair("24", (long) 24);
        pair("9223372036854775807", Long.MAX_VALUE);
        pair("-11", (long) -11);
        
        invalidInput("test");
    }
    
    @Test(expected=ClassCastException.class)
    public void testEmpty() {
        castOut("");
    }
    
    @Test(expected=ClassCastException.class)
    public void testInvalid() {
        castOut("hello");
    }
}
