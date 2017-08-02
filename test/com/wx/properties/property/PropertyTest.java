package com.wx.properties.property;

import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.*;

/**
 * Created by canale on 28.04.16.
 */
public abstract class PropertyTest {

    public abstract <T> Property<T> createProperty(T value);


    @Test
    public void testConstructor() {
        Property<Integer> prop = createProperty(20);

        assertEquals(20, prop.get().intValue());
    }

    @Test
    public void testEmptyConstructor() {
        Property<Object> prop = createProperty(null);
        assertFalse(prop.exists());
    }

    @Test
    public void testSetter() {
        Property<Integer> prop = createProperty(null);

        prop.set(23);
        assertEquals(23, prop.get().intValue());

        prop.set(24);
        assertEquals(24, prop.get().intValue());
    }

    @Test(expected = NullPointerException.class)
    public void testNullSetter() {
        Property<Integer> prop = createProperty(null);

        assertFalse(prop.exists());

        prop.set(null);
    }

    @Test
    public void testClear() {
        Property<Integer> prop = createProperty(24);

        assertTrue(prop.exists());

        Integer oldValue = prop.clear();
        assertFalse(prop.exists());
        assertEquals(24, oldValue.intValue());
    }

}