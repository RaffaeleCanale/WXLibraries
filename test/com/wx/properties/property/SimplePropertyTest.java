package com.wx.properties.property;

/**
 * Created by canale on 28.04.16.
 */
public class SimplePropertyTest extends PropertyTest {

    @Override
    public <T> Property<T> createProperty(T value) {
        return new SimpleProperty<>(value);
    }
}