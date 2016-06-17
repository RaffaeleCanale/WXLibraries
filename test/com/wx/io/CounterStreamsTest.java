/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wx.io;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Random;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Raffaele Canale - raffaelecanale@gmail.com
 */
public class CounterStreamsTest extends Assert {    
    private final static long TEST_COUNT = 2123L;
    @Test
    public void test() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        CounterOutputStream cos = new CounterOutputStream(baos);
        
        for (int i = 0; i < TEST_COUNT; i++) {
            cos.write(new Random().nextInt());
        }
        cos.close();
        
        assertEquals(TEST_COUNT, cos.getCount());
        
        CounterInputStream cis = new CounterInputStream(
                new ByteArrayInputStream(baos.toByteArray()));
        while (cis.read() >= 0) {
        }
        
        assertEquals(TEST_COUNT, cis.getCount());
        cis.close();
    }
    
}