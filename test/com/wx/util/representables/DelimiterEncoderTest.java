package com.wx.util.representables;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import static com.wx.util.representables.DelimiterEncoder.*;
import static org.junit.Assert.*;

/**
 * Created by Raffaele Canale (<a href="mailto:raffaelecanale@gmail.com?subject=WXLibraries">raffaelecanale@gmail.com</a>)
 * on 09.06.16.
 */
public class DelimiterEncoderTest {

    private static final List<String> VALUES = Arrays.asList("foo", "bar", "hello", null, "", "world");

    @Test
    public void testIsDelimiterSuitable() {
        assertTrue(isDelimiterSuitable("$", VALUES));
        assertTrue(isDelimiterSuitable("$$$", VALUES));
        assertTrue(isDelimiterSuitable("x", VALUES));
        assertTrue(isDelimiterSuitable("FOO", VALUES));
        assertTrue(isDelimiterSuitable(" ", VALUES));
        assertTrue(isDelimiterSuitable("    ", VALUES));
        assertTrue(isDelimiterSuitable("fb", VALUES));
        assertTrue(isDelimiterSuitable("Hello", VALUES));

        assertFalse(isDelimiterSuitable("foo", VALUES));
        assertFalse(isDelimiterSuitable("ll", VALUES));
        assertFalse(isDelimiterSuitable("", VALUES));
        assertFalse(isDelimiterSuitable("hhhellooo", VALUES));
        assertFalse(isDelimiterSuitable("f", VALUES));
    }

    @Test
    public void testFindSuitableDelimiter1() {
        assertEquals("hey", findSuitableDelimiter(VALUES, new String[]{"hey", "$"}));
        assertEquals("$", findSuitableDelimiter(VALUES, new String[]{"ll", "$"}));
    }

    @Test(expected = NoSuchElementException.class)
    public void testFindSuitableDelimiter2() {
        findSuitableDelimiter(VALUES, new String[]{"hello", "world"});
    }

    @Test(expected = NoSuchElementException.class)
    public void testFindSuitableDelimiter3() {
        findSuitableDelimiter(VALUES, new String[]{});
    }


    @Test
    public void testEncodeDecode() {
        assertEquals(VALUES, decode("$", encode("$", VALUES)));
        assertEquals(VALUES, autoDecode(autoEncode(VALUES)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidEncode() {
        encode("foo", VALUES);
    }
}