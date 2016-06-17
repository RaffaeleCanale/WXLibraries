package com.wx.util.representables;


import com.wx.util.representables.string.IntRepr;
import com.wx.util.representables.string.MapRepr;
import com.wx.util.representables.string.StringRepr;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import static com.wx.util.representables.DelimiterEncoder.EMPTY_TAG;

///**
// *
// * @author Raffaele Canale - raffaelecanale@gmail.com
// * @version 0.1
// *
// * Project: WXLibraries.ANT
// * File: MapReprTest.java (UTF-8)
// * Date: Oct 8, 2013 
// */
public class MapReprTest extends Assert {

    private static final String KEY_VALUE_SEPARATOR = ":";
    private static final String ELEMENT_SEPARATOR = " && ";
    
    private Map<Integer, String> testMap;
    private String testCast;    
    private final MapRepr<Integer, String> repr = new MapRepr<>(KEY_VALUE_SEPARATOR, ELEMENT_SEPARATOR, new IntRepr(), new StringRepr());
    
    @Before
    public void setUp() {
        testMap = new TreeMap<>();        
        testMap.put(-23, "test");
        testMap.put(0, "");
        testMap.put(24, "hello"); // Need to keep order!!
        
        testCast = "24" + KEY_VALUE_SEPARATOR + "hello" + ELEMENT_SEPARATOR
                + "-23" + KEY_VALUE_SEPARATOR + "test" + ELEMENT_SEPARATOR
                + "0" + KEY_VALUE_SEPARATOR + KEY_VALUE_SEPARATOR + EMPTY_TAG;
        
    }

    @Test
    public void simpleTest() {
        assertOutEquals(testMap, repr.castOut(repr.castIn(testMap)));

        HashMap<Integer, String> map = new HashMap<>();
        assertOutEquals(map, repr.castOut(repr.castIn(map)));

        MapRepr<Integer, String> r =
                new MapRepr<>(null, null, new IntRepr(), new StringRepr());
        assertOutEquals(map, r.castOut(r.castIn(map)));
    }
    
//    @Test
//    public void castInTest() {
//        String value = repr.castIn(testMap);
//        Map<Integer, String> result = repr.castOut(value);
//        assertOutEquals(result, testMap); // No other choice!
//    }
//
//    @Test
//    public void castOutTest() {
//        Map<Integer, String> result = repr.castOut(testCast);
//        assertOutEquals(result, testMap);
//    }

    protected void assertOutEquals(Map<Integer, String> a, Map<Integer, String> b) {
        assertEquals(a.size(), b.size());
        for (Map.Entry<Integer, String> aEntry : a.entrySet()) {
            assertTrue(b.containsKey(aEntry.getKey()));
            assertEquals(aEntry.getValue(), b.get(aEntry.getKey()));
        }
    }
    
    
}


