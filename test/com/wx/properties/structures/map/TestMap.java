package com.wx.properties.structures.map;

import com.wx.util.representables.TypeCaster;
import com.wx.util.representables.string.IntRepr;
import com.wx.util.representables.string.StringRepr;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by canale on 02.05.16.
 */
public class TestMap<K, V> {

    public static TestMap<String, String> stringMap(String... elements) {
        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < elements.length; i+=2) {
            map.put(elements[i], elements[i+1]);
        }
        return new TestMap<>(map, new StringRepr().nullable(), new StringRepr().nullable());
    }

    public static TestMap<Integer, Integer> integerMap(Integer... elements) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < elements.length; i+=2) {
            map.put(elements[i], elements[i+1]);
        }
        return new TestMap<>(map, new IntRepr().nullable(), new IntRepr().nullable());
    }

    private final Map<K, V> expectedMap;
    private final TypeCaster<String, K> keyCaster;
    private final TypeCaster<String, V> valueCaster;

    public TestMap(Map<K, V> expectedMap, TypeCaster<String, K> keyCaster, TypeCaster<String, V> valueCaster) {
        this.expectedMap = expectedMap;
        this.keyCaster = keyCaster;
        this.valueCaster = valueCaster;
    }

    public Map<K, V> getExpectedMap() {
        return expectedMap;
    }

    public TypeCaster<String, K> getKeyCaster() {
        return keyCaster;
    }

    public TypeCaster<String, V> getValueCaster() {
        return valueCaster;
    }

    public TestMap<K,V> mutable() {
        return new TestMap<>(new HashMap<>(expectedMap), keyCaster, valueCaster);
    }

    @Override
    public String toString() {
        return keyCaster + "/" + valueCaster + ">" + expectedMap;
    }
}
