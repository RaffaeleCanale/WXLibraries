package com.wx.properties.structures.map;

import com.wx.properties.StructureInterface;
import com.wx.util.pair.Pair;
import com.wx.util.representables.TypeCaster;
import com.wx.util.representables.string.PairRepr;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by canale on 03.05.16.
 */
public class RefHashMapTest extends MapResourceTest {

    private static final String SEPARATOR = "/";

    @Override
    public <K, V> MapResource<K, V> createMap(StructureInterface map, TestMap<K, V> testMap) {
        return RefHashMap.factory().createMap(map, testMap.getKeyCaster(), testMap.getValueCaster(), testMap.getExpectedMap(), SEPARATOR);
    }

    @Override
    public <K, V> MapResource<K, V> getMap(StructureInterface map, TypeCaster<String, K> keyCaster, TypeCaster<String, V> valueCaster) {
        return RefHashMap.factory().loadMap(map, keyCaster, valueCaster, SEPARATOR);
    }

    @Override
    public <K, V> void assertMapContains(StructureInterface map, TestMap<K, V> testMap) {
        assertNotNull(map.getHeader());
        assertEquals(String.valueOf(testMap.getExpectedMap().size()), map.getHeader());

        PairRepr<K, V> caster = new PairRepr<>(testMap.getKeyCaster(), testMap.getValueCaster(), SEPARATOR);
        int count = 0;
        for (String key : map.keySet()) {
            Pair<K, V> kvPair = caster.castOut(map.get(key));

            assertTrue(testMap.getExpectedMap().containsKey(kvPair.get1()));
            assertEquals(testMap.getExpectedMap().get(kvPair.get1()), kvPair.get2());
            count++;
        }

        assertEquals(testMap.getExpectedMap().size(), count);
    }
}