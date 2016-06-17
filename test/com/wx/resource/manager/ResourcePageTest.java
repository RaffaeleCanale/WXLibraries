///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package com.wx.resource.manager;
//
//import com.wx.crypto.CryptoException;
//import com.wx.util.representables.string.StringRepr;
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.LinkedList;
//import java.util.List;
//import java.util.Map;
//import org.junit.Assert;
//import static org.junit.Assert.assertTrue;
//import org.junit.Before;
//import org.junit.Test;
//
///**
// *
// * @author Raffaele Canale - raffaelecanale@gmail.com
// */
//public class ResourcePageTest extends Assert {
//
//    private final Map<String, String> testProperties = createTestProperties();
//    private ResourcePage runtime;
//
//    @Before
//    public void setUp() {
//        runtime = ResourcePage.createRuntimePage();
//        addProperties(testProperties, runtime);
//    }
//
//    @Test(expected = IllegalArgumentException.class)
//    public void testRuntimePage() throws IOException, CryptoException {
//        runtime.forceSave();
//    }
//
//    @Test
//    public void containsKeyTest() {
//        for (String key : testProperties.keySet()) {
//            assertTrue(runtime.containsKey(key));
//        }
//    }
//
//    @Test
//    public void containsValueTest() {
//        StringRepr repr = new StringRepr();
//        for (String value : testProperties.values()) {
//            assertTrue(runtime.containsValue(value, repr));
//        }
//    }
//
//    @Test
//    public void setGetValueTest() {
//        StringRepr repr = new StringRepr();
//        for (Map.Entry<String, String> entry : testProperties.entrySet()) {
//            assertEquals(entry.getValue(), runtime.getValue(entry.getKey(), repr));
//        }
//    }
//
//    @Test(expected = IllegalAccessError.class)
//    public void invalidKeyTest1() {
//        String key = runtime.getReservedKey("someKey");
//
//        runtime.setValue(key, "value", new StringRepr());
//    }
//
//    @Test(expected = IllegalAccessError.class)
//    public void invalidKeyTest2() {
//        String key = runtime.getStructKey("someKey");
//
//        runtime.setValue(key, "value", new StringRepr());
//    }
//
//    @Test
//    public void removeTest() {
//        String aKey = testProperties.keySet().iterator().next();
//        assertEquals(runtime.removeStructure(aKey), testProperties.get(aKey));
//        assertFalse(runtime.containsKey(aKey));
//    }
//
//    @Test
//    public void matchAndReplaceTest() {
//        Map<String, String> startingKeys = new HashMap<>();
//        startingKeys.put("keyA1hello", "value");
//        startingKeys.put("keyA2hi", "value");
//        startingKeys.put("keyA3foo", "value");
//        startingKeys.put("keyA4", "value");
//        startingKeys.put("keya1sdf", "value");
//        startingKeys.put("keyAdasdfdsa", "value");
//        startingKeys.put("keyAbsadfadsf", "value");
//        startingKeys.put(runtime.getReservedKey("keyA1goodbye"), "value"); //Should not match
//
//        String testRegex = ".*A\\d(.*)";
//        String replacement = "$1";
//
//        runtime = ResourcePage.createRuntimePage();
//        addProperties(startingKeys, runtime);
//
//        List<String> result = new LinkedList<>();
//        runtime.matchAndReplaceKeys(testRegex, replacement, result);
//
//        assertEquals(3, result.size());
//        assertTrue(result.contains("hello"));
//        assertTrue(result.contains("hi"));
//        assertTrue(result.contains("foo"));
//
//        for (String key : startingKeys.keySet()) {
//            assertTrue(runtime.containsKey(key)); // Ensure nothing was modified
//        }
//    }
//
//    @Test
//    public void testMatchKeys() {
//        Map<String, String> startingKeys = new HashMap<>();
//        startingKeys.put("keyA1hello", "value");
//        startingKeys.put("keyA2hi", "value");
//        startingKeys.put("keyA3foo", "value");
//        startingKeys.put("keyA4", "value");
//        startingKeys.put("keya1sdf", "value");
//        startingKeys.put("keyAdasdfdsa", "value");
//        startingKeys.put("keyAbsadfadsf", "value");
//        startingKeys.put(runtime.getReservedKey("keyA1goodbye"), "value"); //Should not match
//
//        String testRegex = ".*[Aa][123].*";
//
//        runtime = ResourcePage.createRuntimePage();
//        addProperties(startingKeys, runtime);
//
//        List<String> result = new LinkedList<>();
//        runtime.matchKeys(testRegex, result);
//
//        assertEquals(4, result.size());
//        assertTrue(result.contains("keyA1hello"));
//        assertTrue(result.contains("keyA2hi"));
//        assertTrue(result.contains("keyA3foo"));
//        assertTrue(result.contains("keya1sdf"));
//    }
//
//    // TODO Test structures!!
//
//    private Map<String, String> createTestProperties() {
//        Map<String, String> result = new HashMap<>();
//        result.put("key1", "value1");
//        result.put("key2", "value2");
//        result.put("key3", "value3");
//        result.put("key4", "value4");
//
//        return result;
//    }
//
//    private void addProperties(Map<String, String> props, ResourcePage page) {
//        page.propertyFile.putAll(props);
//    }
//
//}