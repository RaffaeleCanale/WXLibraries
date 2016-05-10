package com.wx.properties.structures.list;

import com.wx.properties.StructureInterface;
import com.wx.util.representables.TypeCaster;

import java.util.List;
import java.util.Objects;

import static org.junit.Assert.*;

/**
 * Created by canale on 02.05.16.
 */
public class IndexedListTest extends ListResourceTest {


    @Override
    public <E> ListResource<E> createList(StructureInterface map, TestList<E> list) {
        return IndexedList.factory().createList(map, list.getCaster(), list.getExpectedList());
    }

    @Override
    public <E> ListResource<E> getList(StructureInterface map, TypeCaster<String, E> caster) {
        return IndexedList.factory().loadList(map, caster);
    }

    @Override
    public <E> void assertMapContains(StructureInterface map, TestList<E> list) {
        assertNotNull("Header does not exist", map.getHeader());
        assertEquals("Value of header is wrong", String.valueOf(list.getExpectedList().size()), map.getHeader());

        assertEquals("Number of non-null fields is wrong", nonNullElementsCount(list.getExpectedList()), map.size());
        for (int i = 0; i < list.getExpectedList().size(); i++) {
            String key = "[" + i + "]";
            E expectedValue = list.getExpectedList().get(i);

            if (expectedValue == null) {
                assertFalse(map.get(key), map.containsKey(key));

            } else {

                assertTrue(map.containsKey(key));

                E actualValue = list.getCaster().castOut(map.get(key));
                assertEquals(expectedValue, actualValue);
            }
        }
    }

    private int nonNullElementsCount(List<?> list) {
        return (int) list.stream().filter(Objects::nonNull).count();
    }
}