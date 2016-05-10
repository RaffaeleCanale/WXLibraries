package com.wx.properties.structures.set;

import com.wx.properties.StructureInterface;
import com.wx.util.representables.TypeCaster;
import com.wx.util.representables.string.SetRepr;

import java.util.Set;

import static org.junit.Assert.*;

/**
 * Created by canale on 02.05.16.
 */
public class RefSetTest extends SetResourceTest {

    private final static String SEPARATOR = "/";

    @Override
    public <E> SetResource<E> createSet(StructureInterface map, TestSet<E> set) {
        return RefSet.factory().createSet(map, set.getCaster(), set.getExpectedSet(), SEPARATOR);
    }

    @Override
    public <E> SetResource<E> getSet(StructureInterface map, TypeCaster<String, E> caster) {
        return RefSet.factory().loadSet(map, caster, SEPARATOR);
    }

    @Override
    public <E> void assertMapContains(StructureInterface map, TestSet<E> set) {
        assertNotNull(map.getHeader());
        assertEquals(String.valueOf(set.getExpectedSet().size()), map.getHeader());

        SetRepr<E> setRepr = new SetRepr<>(set.getCaster(), SEPARATOR);
        int count = 0;
        for (String key : map.keySet()) {
            String value = map.get(key);
            assertFalse(value.isEmpty());
            Set<E> values = setRepr.castOut(value);

            for (E e : values) {
                assertTrue(set.getExpectedSet().contains(e));
                count++;
            }
        }

        assertEquals(set.getExpectedSet().size(), count);
    }
}