/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wx.util.representables;

import com.wx.util.representables.string.CharRepr;
import com.wx.util.representables.string.NullSafeCaster;
import org.junit.Test;

import static org.junit.Assert.assertNull;

/**
 *
 * @author Raffaele Canale - raffaelecanale@gmail.com
 */
public class NullSafeCasterTest  {
    
    private final TypeCaster<String, Character> subCaster = new CharRepr();
    private final TypeCaster<String, Character> nullSafeCaster = 
            new NullSafeCaster<>(subCaster);
    
    
    @Test(expected = NullPointerException.class)
    public void testSubNullIn() {
        subCaster.castIn(null);
    }
    
    @Test(expected = NullPointerException.class)
    public void testSubNullOut() {
        subCaster.castOut(null);
    }
    
    @Test
    public void testNullSafe() {
        assertNull(nullSafeCaster.castIn(null));
        assertNull(nullSafeCaster.castOut(null));
    }
    
}