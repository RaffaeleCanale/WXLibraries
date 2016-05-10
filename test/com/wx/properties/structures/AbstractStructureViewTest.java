package com.wx.properties.structures;

import com.wx.properties.structures.view.AbstractStructureView;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

/**
 * Created by canale on 04.05.16.
 */
public class AbstractStructureViewTest {

    private static final String VIEW_PREFIX = "view.";
    private static final String HEADER_KEY = "view_header";

    private static Map<String, String> exampleMap() {
        Map<String, String> underlyingMap = new HashMap<>();
        underlyingMap.put("key1", "value1");
        underlyingMap.put("key2", "value2");
        underlyingMap.put("key3", "value3");
        underlyingMap.put("key4", "value4");
        return underlyingMap;
    }

    private static ExampleImplementation createView(Map<String, String> underlyingMap) {
        return new ExampleImplementation(HEADER_KEY, underlyingMap);
    }

    private static Map<String, String> fillView(ExampleImplementation view) {
        Map<String, String> expectedMap = new HashMap<>();

        fill(view);
        fill(expectedMap);

        return expectedMap;
    }

    private static void fill(Map<String, String> underlyingMap) {
        underlyingMap.put("key3", "value3");
        underlyingMap.put("key4", "value4");
        underlyingMap.put("key5", "value5");
        underlyingMap.put("key6", "value6");
    }


    @Test(expected = IllegalArgumentException.class)
    public void headerInViewTest() {
        new ExampleImplementation(VIEW_PREFIX + HEADER_KEY, new HashMap<>());
    }

    @Test(expected = NullPointerException.class)
    public void nullConstructor1() {
        new ExampleImplementation(null, new HashMap<>());
    }

    @Test(expected = NullPointerException.class)
    public void nullConstructor2() {
        new ExampleImplementation(HEADER_KEY, null);
    }

    @Test
    public void testEmpty() {
        ExampleImplementation view = createView(exampleMap());
        assertTrue(view.isEmpty());
    }

    @Test
    public void testAdd() {
        Map<String, String> map = exampleMap();
        int originalSize = map.size();

        ExampleImplementation view = createView(map);
        HashMap<String, String> expectedMap = new HashMap<>();

        assertEquals(view.put("view1", "value1"), expectedMap.put("view1", "value1"));

        assertEquals(expectedMap, view);
        assertEquals(1 + originalSize, map.size());

        assertEquals(view.put("key1", "foo"), expectedMap.put("key1", "foo"));

        assertEquals(expectedMap, view);
        assertEquals(2 + originalSize, map.size());
    }

    @Test
    public void testAddUnderlying() {
        Map<String, String> map = exampleMap();
        ExampleImplementation view = createView(map);

        assertTrue(view.isEmpty());
        map.put(VIEW_PREFIX + "test", "test");

        assertEquals(1, view.size());
        assertTrue(view.containsKey("test"));
        assertEquals("test", view.get("test"));
    }

    @Test
    public void clearTest() {
        Map<String, String> map = exampleMap();
        ExampleImplementation view = createView(map);
        fillView(view);

        view.clear();

        assertEquals(Collections.<String,String>emptyMap(), view);
        assertEquals(exampleMap(), map);
    }

    @Test
    public void removeTest() {
        Map<String, String> map = exampleMap();
        ExampleImplementation view = createView(map);
        Map<String, String> expectedView = fillView(view);

        assertEquals(expectedView, view);

        String keyToRemove = expectedView.keySet().iterator().next();
        assertEquals(expectedView.remove(keyToRemove), view.remove(keyToRemove));

        assertEquals(expectedView, view);
    }

    @Test
    public void underlyingRemoveTest() {
        Map<String, String> map = exampleMap();
        ExampleImplementation view = createView(map);
        Map<String, String> expectedView = fillView(view);

        assertEquals(expectedView, view);

        String keyToRemove = expectedView.keySet().iterator().next();
        expectedView.remove(keyToRemove);
        map.remove(VIEW_PREFIX + keyToRemove);

        assertEquals(expectedView, view);
    }

    @Test
    public void keySetTest() {
        ExampleImplementation view = createView(exampleMap());
        Map<String, String> expectedView = fillView(view);

        assertEquals(expectedView.keySet(), view.keySet());
    }

    @Test
    public void keySetClearTest() {
        Map<String, String> map = exampleMap();
        ExampleImplementation view = createView(map);
        fillView(view);

        view.keySet().clear();

        assertEquals(Collections.<String,String>emptyMap(), view);
        assertEquals(exampleMap(), map);
    }

    @Test
    public void keySetRemoveTest() {
        Map<String, String> map = exampleMap();
        ExampleImplementation view = createView(map);
        Map<String, String> expectedView = fillView(view);

        assertEquals(expectedView, view);

        String keyToRemove = expectedView.keySet().iterator().next();
        assertEquals(expectedView.keySet().remove(keyToRemove), view.keySet().remove(keyToRemove));

        assertEquals(expectedView, view);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void keySetAddTest() {
        createView(exampleMap()).keySet().add("test");
    }

    @Test
    public void keySetIteratorRemove() {
        Map<String, String> map = exampleMap();
        ExampleImplementation view = createView(map);
        Map<String, String> expectedView = fillView(view);

        assertEquals(expectedView, view);

        Iterator<String> it = view.keySet().iterator();
        String keyToRemove = it.next();
        it.remove();
        expectedView.remove(keyToRemove);

        assertEquals(expectedView, view);
    }

    @Test
    public void valuesTest() {
        ExampleImplementation view = createView(exampleMap());
        Map<String, String> expectedView = fillView(view);

        assertTrue(expectedView.values().containsAll(view.values()));
        assertTrue(view.values().containsAll(expectedView.values()));
    }

    @Test
    public void valuesClearTest() {
        Map<String, String> map = exampleMap();
        ExampleImplementation view = createView(map);
        fillView(view);

        view.values().clear();

        assertEquals(Collections.<String,String>emptyMap(), view);
        assertEquals(exampleMap(), map);
    }

    @Test
    public void valuesRemoveTest() {
        Map<String, String> map = exampleMap();
        ExampleImplementation view = createView(map);
        Map<String, String> expectedView = fillView(view);

        assertEquals(expectedView, view);

        String valueToRemove = expectedView.values().iterator().next();
        assertEquals(expectedView.values().remove(valueToRemove), view.values().remove(valueToRemove));

        assertEquals(expectedView, view);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void valuesAddTest() {
        createView(exampleMap()).values().add("test");
    }

    @Test
    public void valuesIteratorRemove() {
        Map<String, String> map = exampleMap();
        ExampleImplementation view = createView(map);
        Map<String, String> expectedView = fillView(view);

        assertEquals(expectedView, view);

        Iterator<String> it = view.values().iterator();
        String valueToRemove = it.next();
        it.remove();
        expectedView.values().remove(valueToRemove);

        assertEquals(expectedView, view);
    }

    @Test
    public void entrySetTest() {
        ExampleImplementation view = createView(exampleMap());
        Map<String, String> expectedView = fillView(view);

        assertEquals(expectedView.entrySet(), view.entrySet());
    }

    @Test
    public void entrySetClearTest() {
        Map<String, String> map = exampleMap();
        ExampleImplementation view = createView(map);
        fillView(view);

        view.entrySet().clear();

        assertEquals(Collections.<String,String>emptyMap(), view);
        assertEquals(exampleMap(), map);
    }

    @Test
    public void entrySetRemoveTest() {
        Map<String, String> map = exampleMap();
        ExampleImplementation view = createView(map);
        Map<String, String> expectedView = fillView(view);

        assertEquals(expectedView, view);

        Map.Entry<String, String> entry = expectedView.entrySet().iterator().next();
        assertEquals(expectedView.entrySet().remove(entry), view.entrySet().remove(entry));

        assertEquals(expectedView, view);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void entrySetAddTest() {
        createView(exampleMap()).entrySet().add(new AbstractMap.SimpleEntry<>("foo", "bar"));
    }

    @Test
    public void entrySetIteratorRemove() {
        Map<String, String> map = exampleMap();
        ExampleImplementation view = createView(map);
        Map<String, String> expectedView = fillView(view);

        assertEquals(expectedView, view);

        Iterator<Map.Entry<String, String>> it = view.entrySet().iterator();
        Map.Entry<String, String> entry = it.next();
        it.remove();
        assertNotNull(expectedView.remove(entry.getKey()));

        assertEquals(expectedView, view);
    }

    @Test
    public void entrySetSetValue() {
        Map<String, String> map = exampleMap();
        ExampleImplementation view = createView(map);
        Map<String, String> expectedView = fillView(view);

        assertEquals(expectedView, view);

        Iterator<Map.Entry<String, String>> it = view.entrySet().iterator();
        Map.Entry<String, String> entry = it.next();
        entry.setValue("bar");
        expectedView.put(entry.getKey(), "bar");

        assertEquals(expectedView, view);
    }

    private static class ExampleImplementation extends AbstractStructureView {

        public ExampleImplementation(String headerKey, Map<String, String> map) {
            super(headerKey, map);
        }

        @Override
        protected boolean isKeyInView(String realKey) {
            return realKey.startsWith(VIEW_PREFIX);
        }

        @Override
        protected String realKeyToView(String realKey) {
            assertTrue(realKey + " is not a view key", isKeyInView(realKey));
            return realKey.substring(VIEW_PREFIX.length());
        }

        @Override
        protected String viewKeyToReal(String viewKey) {
            return VIEW_PREFIX + viewKey;
        }


    }
}