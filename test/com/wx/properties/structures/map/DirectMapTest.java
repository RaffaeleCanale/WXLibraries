package com.wx.properties.structures.map;

import com.wx.properties.StructureInterface;
import com.wx.util.representables.TypeCaster;

import static org.junit.Assert.*;

/**
 * Created by canale on 04.05.16.
 */
public class DirectMapTest extends MapResourceTest {

    @Override
    public <K, V> MapResource<K, V> createMap(StructureInterface map, TestMap<K, V> testMap) {
        return DirectMap.factory().createMap(map, testMap.getKeyCaster(), testMap.getValueCaster(), testMap.getExpectedMap());
    }

    @Override
    public <K, V> MapResource<K, V> getMap(StructureInterface map, TypeCaster<String, K> keyCaster, TypeCaster<String, V> valueCaster) {
        return DirectMap.factory().loadMap(map, keyCaster, valueCaster);
    }

    @Override
    public <K, V> void assertMapContains(StructureInterface map, TestMap<K, V> testMap) {
        assertEquals(testMap.getExpectedMap().size(), map.size());

        testMap.getExpectedMap().forEach((k, v) -> {
            assertTrue(map.containsKey(testMap.getKeyCaster().castIn(k)));
            assertEquals(v, testMap.getValueCaster().castOut(map.get(testMap.getKeyCaster().castIn(k))));
        });
    }
}