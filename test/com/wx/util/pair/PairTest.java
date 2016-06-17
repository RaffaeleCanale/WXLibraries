package com.wx.util.pair;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Assert;
import org.junit.Test;

import java.util.Objects;

/**
 *
 * @author Raffaele Canale - raffaelecanale@gmail.com
 */
public class PairTest extends Assert {

    @Test
    public void testApply() {
        Pair<String, String> pair = Pair.of("hello", "world");

        String result = pair.apply((k, v) -> k + v);
        assertEquals("helloworld", result);
    }

    @Test
    public void testGet() {
        Pair<String, String> pair = Pair.of("hello", "hi");

        assertEquals("hello", pair.get1());
        assertEquals("hi", pair.get2());

        pair.set1("foo");
        pair.set2("bar");

        assertEquals("foo", pair.get1());
        assertEquals("bar", pair.get2());
    }

    @Test
    public void testContainsNull() {
        Pair<String, String> testPair = Pair.of("", "hello");
        
        assertFalse(testPair.containsNull());
        
        testPair.set1(null);
        assertTrue(testPair.containsNull());
        
        testPair.set1("");
        testPair.set2(null);
        assertTrue(testPair.containsNull());
        
        testPair.set1(null);
        assertTrue(testPair.containsNull());
    }

    @Test
    public void testEquals() {
        Pair<String, String> pair1 = Pair.of("hello", "hi");
        Pair<String, String> pair2 = Pair.of("hello", "foo");
        Pair<String, String> pair3 = Pair.of("foo", "hi");
        Pair<String, String> pair4 = Pair.of("hello", "hi");

        testNotEquals(pair1, pair2);
        testNotEquals(pair1, pair3);
        testEquals(pair1, pair4);
        testNotEquals(pair2, pair3);
        testNotEquals(pair2, pair4);
        testNotEquals(pair3, pair4);

        testNotEquals(pair1, null);
        testNotEquals(pair1, "Hello");
    }

    @Test
    public void testToString() {
        Pair<String, String> pair1 = Pair.of("hello", "hi");
        assertTrue(pair1.toString().contains("hello"));
        assertTrue(pair1.toString().contains("hi"));
    }

    private void testEquals(Object p1, Object p2) {
        assertEquals(p1, p2);
        assertEquals(p1.hashCode(), p2.hashCode());
    }

    private void testNotEquals(Object p1, Object p2) {
        assertNotEquals(p1, p2);
    }
    
}