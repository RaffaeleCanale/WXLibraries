package com.wx.util;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created on 01/10/2015
 *
 * @author Raffaele Canale (raffaelecanale@gmail.com)
 * @version 0.1
 */
public class StubbornBoolTest {

    @Test
    public void completeTest() {
        StubbornBool bool = new StubbornBool(true);
        bool.set(true);

        assertTrue(bool.get());

        bool.set(false);
        assertFalse(bool.get());

        bool.set(false);
        assertFalse(bool.get());

        bool.set(true);
        assertFalse(bool.get());
    }

}