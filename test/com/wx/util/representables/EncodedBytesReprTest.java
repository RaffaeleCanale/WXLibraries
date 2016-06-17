/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wx.util.representables;

import com.wx.util.pair.Pair;
import com.wx.util.representables.string.EncodedBytesRepr;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNull;

/**
 *
 * @author Raffaele Canale - raffaelecanale@gmail.com
 */
public class EncodedBytesReprTest extends GenericCasterTest<String, byte[]> {

    @Override
    protected TypeCaster<String, byte[]> getCaster() {
        return new EncodedBytesRepr();
    }

    @Override
    protected void setUpPairs() {
        pair("", toByteArray());
        pair("ff", toByteArray(0xff));
        pair("abcf29", toByteArray(0xAB, 0xCF, 0x29));
        
        invalidInput("a");
        invalidInput("agLo");
    }
    
    private byte[] toByteArray(int... values) {
        byte[] result = new byte[values.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = (byte) values[i];
        }
        return result;
    }


    @Override
    protected void assertOutEquals(byte[] expected, byte[] actual) {
        assertArrayEquals(expected, actual);
    }
    
}
