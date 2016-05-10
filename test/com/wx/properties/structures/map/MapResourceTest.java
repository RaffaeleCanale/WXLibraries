package com.wx.properties.structures.map;

import com.wx.properties.StructureInterface;
import com.wx.util.representables.TypeCaster;
import com.wx.util.representables.string.StringRepr;
import org.junit.Test;

import java.util.*;
import java.util.function.BiConsumer;

import static com.wx.properties.structures.map.TestMap.integerMap;
import static com.wx.properties.structures.map.TestMap.stringMap;
import static org.junit.Assert.*;

/**
 * Created by canale on 03.05.16.
 */
public abstract class MapResourceTest {

    private enum TestMaps {
        NORMAL_MAP(stringMap("k1", "v1", "k2", "v2", "k3", "v3", "k4", "v4", "k5", "v5")),
        EMPTY_MAP(stringMap()),
        SINGLETON_MAP(stringMap("k1", "v1")),
        MAP_WITH_NULL_1(stringMap("k1", null, "k2", "v2")),
        MAP_WITH_NULL_2(stringMap("k1", "v2", null, "v2")),
        MAP_WITH_NULL_3(stringMap(null, null, "k2", "v2")),
        MAP_WITH_NULL_4(stringMap(null, null, "null", "v2", "k3", "null", "null", "null")),
        MAP_WITH_EMPTY_1(stringMap("", "v1", "k2", "v2")),
        MAP_WITH_EMPTY_2(stringMap("k1", "", "", "")),
        MAP_WITH_SPACE_1(stringMap(" ", "v1", "k2", "v2")),
        MAP_WITH_SPACE_2(stringMap("  ", " el2 ", "   ", "    ")),

        INT_MAP(integerMap(1, 2, 3, 2, 5, 6, 7, 8, 9, 10, 11, 12)),
        INT_MAP_NULL(integerMap(1, 2, 3, null, 5, 6, null, 8, 9, 10, 11, 12));

        private final TestMap<?, ?> map;

        TestMaps(TestMap<?, ?> testMap) {
            this.map = testMap;
        }

        @Override
        public String toString() {
            return name().toLowerCase();
        }
    }


    private static void forEachMap(BiConsumer<String, TestMap<?, ?>> consumer) {
        for (MapResourceTest.TestMaps testMap : MapResourceTest.TestMaps.values()) {
            String key = testMap.toString();
            consumer.accept(key, testMap.map.mutable());
        }
    }

    private static void forEachNonEmptyMap(BiConsumer<String, TestMap<?, ?>> consumer) {
        for (MapResourceTest.TestMaps testMap : MapResourceTest.TestMaps.values()) {
            if (!testMap.map.getExpectedMap().isEmpty()) {
                String key = testMap.toString();
                consumer.accept(key, testMap.map.mutable());
            }
        }
    }

    public abstract <K, V> MapResource<K, V> createMap(StructureInterface map, TestMap<K, V> testMap);

    public abstract <K, V> MapResource<K, V> getMap(StructureInterface map, TypeCaster<String, K> keyCaster, TypeCaster<String, V> valueCaster);

    public abstract <K, V> void assertMapContains(StructureInterface map, TestMap<K, V> testMap);

    private void assertMapEquals(String key, StructureInterface map, TestMap<?, ?> expectedMap, Map<?, ?> actualMap) {
//        Map<?, ?> eMap = expectedMap.getExpectedMap();
//
//        assertEquals(eMap.size(), actualMap.size());
//        eMap.forEach((k,v) -> {
//            assertTrue(key, actualMap.containsKey(k));
//            assertEquals(key, v, actualMap.get(k));
//        });
//        actualMap.forEach((k,v) -> {
//            assertTrue(key, eMap.containsKey(k));
//            assertEquals(key, v, eMap.get(k));
//        });

        assertEquals(key, expectedMap.getExpectedMap(), actualMap);
        assertMapContains(map, expectedMap);
    }

    @Test
    public void testCreateMap() {
        forEachMap((key, testMap) -> {
            StructureInterface map = new StructureInterface();
            MapResource<?, ?> createdMap = createMap(map, testMap);

            assertMapEquals(key, map, testMap, createdMap);
        });
    }

    @Test
    public void testReadMap() {
        forEachMap((key, testMap) -> {
            StructureInterface map = new StructureInterface();
            createMap(map, testMap);
            MapResource<?, ?> readMap = getMap(map, testMap.getKeyCaster(), testMap.getValueCaster());

            assertMapEquals(key, map, testMap, readMap);
        });
    }

    @Test
    public void testReadNonExistingMap() {
        StructureInterface map = new StructureInterface();
        MapResource<String, String> testMap = getMap(map, new StringRepr(), new StringRepr());
        assertNull(testMap);
    }

    @Test
    public void testCreateExistingMap() {
        StructureInterface map = new StructureInterface();

        createMap(map, MapResourceTest.TestMaps.NORMAL_MAP.map);
        MapResource<?, ?> secondMap = createMap(map, MapResourceTest.TestMaps.INT_MAP.map);

        assertMapEquals("int_map", map, MapResourceTest.TestMaps.INT_MAP.map, secondMap);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testCreateImmutable() {
        TestMap<String, String> testMap = (TestMap<String, String>) MapResourceTest.TestMaps.NORMAL_MAP.map.mutable();
        StructureInterface map = new StructureInterface();

        MapResource<String, String> mapResource = createMap(map, testMap);

        testMap.getExpectedMap().put("foo", "bar");

        assertMapEquals("normal_map", map, MapResourceTest.TestMaps.NORMAL_MAP.map, mapResource);
    }

    @Test
    public void testNullArgsCreate() {
        createExpectingNull(null, MapResourceTest.TestMaps.NORMAL_MAP.map);
        createExpectingNull(new StructureInterface(), new TestMap<>(null, new StringRepr(), new StringRepr()));
        createExpectingNull(new StructureInterface(), new TestMap<>(new HashMap<>(), null, new StringRepr()));
        createExpectingNull(new StructureInterface(), new TestMap<>(new HashMap<>(), new StringRepr(), null));
    }

    private void createExpectingNull(StructureInterface map, TestMap<?, ?> testMap) {
        try {
            createMap(map, testMap);
            throw new AssertionError("Expected NullPointerException with args: " + map + " / " + testMap);
        } catch (NullPointerException e) {
            // Ignore
        }
    }

    @Test
    public void testNullArgsGet() {
        getExpectingNull(null, new StringRepr(), new StringRepr());
        getExpectingNull(new StructureInterface(), new StringRepr(), null);
        getExpectingNull(new StructureInterface(), null, new StringRepr());
    }

    private void getExpectingNull(StructureInterface map, TypeCaster<String, ?> keyCaster, TypeCaster<String, ?> valueCaster) {
        try {
            getMap(map, keyCaster, valueCaster);
            throw new AssertionError("Expected NullPointerException with args: " + map + " / " + keyCaster + "/" + valueCaster);
        } catch (NullPointerException e) {
            // Ignore
        }
    }

    @Test
    @SuppressWarnings("unchecked")
    public void addTest() {
        TestMap<String, String> testMap = (TestMap<String, String>) TestMaps.NORMAL_MAP.map.mutable();

        StructureInterface in = new StructureInterface();
        MapResource<String, String> map = createMap(in, testMap);

        assertEquals(map.put("k3", "world"), testMap.getExpectedMap().put("k3", "world"));

        assertMapEquals("normal_map", in, testMap, map);
    }

    @Test
    public void keySetTest() {
        forEachMap((key, expectedMap) -> {
            MapResource<?, ?> actualMap = createMap(new StructureInterface(), expectedMap);

            assertEquals(key, expectedMap.getExpectedMap().keySet(), actualMap.keySet());
        });
    }

    @Test
    public void valuesTest() {
        forEachMap((key, expectedMap) -> {
            MapResource<?, ?> actualMap = createMap(new StructureInterface(), expectedMap);

            assertTrue(actualMap.values().containsAll(expectedMap.getExpectedMap().values()));
            assertTrue(expectedMap.getExpectedMap().values().containsAll(actualMap.values()));
        });
    }

    @Test
    public void entrySetTest() {
        forEachMap((key, expectedMap) -> {
            MapResource<?, ?> actualMap = createMap(new StructureInterface(), expectedMap);

            assertEquals(key, expectedMap.getExpectedMap().entrySet(), actualMap.entrySet());
        });
    }

    @Test(expected = UnsupportedOperationException.class)
    @SuppressWarnings("unchecked")
    public void keySetAddTest() {
        ((Collection<String>) createMap(new StructureInterface(), TestMaps.NORMAL_MAP.map).keySet()).add("foo bar");
    }

    @Test(expected = UnsupportedOperationException.class)
    @SuppressWarnings("unchecked")
    public void valuesAddTest() {
        ((Collection<String>) createMap(new StructureInterface(), TestMaps.NORMAL_MAP.map).values()).add("foo bar");
    }

    @Test(expected = UnsupportedOperationException.class)
    @SuppressWarnings("unchecked")
    public void entrySetAddTest() {
        ((Map<String, String>) createMap(new StructureInterface(), TestMaps.NORMAL_MAP.map)).entrySet().add(new AbstractMap.SimpleEntry<>("foo", "bar"));
    }


    @Test
    @SuppressWarnings("unchecked")
    public void containsKeyTest() {
        forEachNonEmptyMap((key, expectedMap) -> {
            MapResource<?, ?> testMap = createMap(new StructureInterface(), expectedMap);
            for (Object mapKey : expectedMap.getExpectedMap().keySet()) {
                assertTrue(testMap.containsKey(mapKey));
            }
        });

        TestMap<String, String> expectedMap = (TestMap<String, String>) MapResourceTest.TestMaps.NORMAL_MAP.map.mutable();
        MapResource<String, String> testMap = createMap(new StructureInterface(), expectedMap);
        assertFalse(testMap.containsKey("foo bar"));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void containsValueTest() {
        forEachNonEmptyMap((key, expectedMap) -> {
            MapResource<?, ?> testMap = createMap(new StructureInterface(), expectedMap);
            for (Object mapValue : expectedMap.getExpectedMap().values()) {
                assertTrue(testMap.containsValue(mapValue));
            }
        });

        TestMap<String, String> expectedMap = (TestMap<String, String>) MapResourceTest.TestMaps.NORMAL_MAP.map.mutable();
        MapResource<String, String> testMap = createMap(new StructureInterface(), expectedMap);
        assertFalse(testMap.containsValue("foo bar"));
    }


    @Test
    public void removeTest() {
        forEachNonEmptyMap((key, expectedMap) -> {
            StructureInterface in = new StructureInterface();
            MapResource<?, ?> actualMap = createMap(in, expectedMap);

            Object keyToRemove = expectedMap.getExpectedMap().keySet().iterator().next();
            assertEquals(key, expectedMap.getExpectedMap().remove(keyToRemove), actualMap.remove(keyToRemove));
            assertMapEquals(key, in, expectedMap, actualMap);
        });
    }

    @Test
    public void clearTest() {
        StructureInterface in = new StructureInterface();

        MapResource<?, ?> map = createMap(in, MapResourceTest.TestMaps.NORMAL_MAP.map);
        map.clear();

        assertMapEquals("empty_map", in, MapResourceTest.TestMaps.EMPTY_MAP.map, map);
    }

    @Test
    public void propertyRemoveTest() {
        StructureInterface in = new StructureInterface();
        TestMap<?, ?> testMap = MapResourceTest.TestMaps.NORMAL_MAP.map;

        MapResource<?, ?> actualMap = createMap(in, testMap);
        Map<?, ?> removed = actualMap.removeStructure();

        assertEquals(testMap.getExpectedMap(), removed);
        assertEquals(0, in.size());
        assertNull(in.getHeader());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void propertyRemoveImmutableTest() {
        StructureInterface in = new StructureInterface();
        TestMap<String, String> testMap = (TestMap<String, String>) MapResourceTest.TestMaps.NORMAL_MAP.map;

        MapResource<String, String> actualMap = createMap(in, testMap);
        Map<String, String> removed = actualMap.removeStructure();

        try {
            removed.put("hello", "world");

            assertEquals(0, in.size());
            assertNull(in.getHeader());
        } catch (UnsupportedOperationException e) {
            // Ignore
        }
    }

    @Test
    public void iteratorTest1() {
        StructureInterface map = new StructureInterface();
        TestMap<?, ?> testMap = TestMaps.NORMAL_MAP.map.mutable();
        MapResource<?, ?> actualMap = createMap(map, testMap);

        Iterator<? extends Map.Entry<?, ?>> it = actualMap.entrySet().iterator();
        Map.Entry<?, ?> next = it.next();
        it.remove();

        testMap.getExpectedMap().remove(next.getKey());
        assertMapEquals("normal_map", map, testMap, actualMap);
    }

    @Test
    public void iteratorTest2() {
        StructureInterface map = new StructureInterface();
        TestMap<?, ?> testMap = TestMaps.NORMAL_MAP.map.mutable();
        MapResource<?, ?> actualMap = createMap(map, testMap);

        Iterator<?> it = actualMap.keySet().iterator();
        Object next = it.next();
        it.remove();

        assertNotNull(testMap.getExpectedMap().remove(next));
        assertMapEquals("normal_map", map, testMap, actualMap);
    }

    @Test
    public void iteratorTest3() {
        StructureInterface map = new StructureInterface();
        TestMap<?, ?> testMap = TestMaps.NORMAL_MAP.map.mutable();
        MapResource<?, ?> actualMap = createMap(map, testMap);

        Iterator<?> it = actualMap.values().iterator();
        Object next = it.next();
        it.remove();

        assertTrue(testMap.getExpectedMap().containsValue(next));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void iteratorTest4() {
        StructureInterface map = new StructureInterface();
        TestMap<String, String> testMap = (TestMap<String, String>) TestMaps.NORMAL_MAP.map.mutable();
        MapResource<String, String> actualMap = createMap(map, testMap);

        Iterator<? extends Map.Entry<String, String>> it = actualMap.entrySet().iterator();
        Map.Entry<String, String> next = it.next();
        next.setValue("baar");

        testMap.getExpectedMap().put(next.getKey(), "baar");
        assertMapEquals("normal_map", map, testMap, actualMap);
    }
}