/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wx.util.representables;

import com.wx.util.pair.Pair;
import com.wx.util.representables.string.DoubleRepr;
import java.util.List;
import org.junit.Test;

/**
 *
 * @author Raffaele Canale - raffaelecanale@gmail.com
 */
public class DoubleReprTest extends GenericCasterTest<String, Double> {

    @Override
    protected TypeCaster<String, Double> getCaster() {
        return new DoubleRepr();
    }

    @Override
    protected void setUpPairs() {
        pair("22.0", 22.0);
        pair("9.0", 9.0);
        pair("11.3", 11.3);
        pair("-11.3", -11.3);
        pair("0.943543245564156", 0.943543245564156);
        
        invalidInput("");
        invalidInput("hello");
        invalidInput("0,12");
    }
}
