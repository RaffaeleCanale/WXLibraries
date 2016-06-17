/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wx.util.representables;

import com.wx.util.pair.Pair;
import com.wx.util.representables.string.CharRepr;
import java.util.List;
import org.junit.Test;

/**
 *
 * @author Raffaele Canale - raffaelecanale@gmail.com
 */
public class CharReprTest extends GenericCasterTest<String, Character> {

    @Override
    protected TypeCaster<String, Character> getCaster() {
        return new CharRepr();
    }

    @Override
    protected void setUpPairs() {
        pair("c", 'c');
        pair("r", 'r');
        pair("7", '7');
        pair("?", '?');
        pair("!", '!');
        pair("/", '/');
        pair(" ", ' ');
        
        invalidInput("");
        invalidInput("tt");
    }    
    
}
