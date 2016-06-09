package com.wx.properties;

import com.wx.util.representables.TypeCaster;
import com.wx.util.representables.string.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Created by canale on 28.04.16.
 */
public abstract class PropertyValue<T> {

    static PropertyValue intProperty(int expected) {
        return new CasterPropertyValue<>(expected, new IntRepr());
    }

    static PropertyValue<String> stringProperty(String expected) {
        return new CasterPropertyValue<>(expected, new StringRepr());
    }

    static PropertyValue<Double> doubleProperty(double expected) {
        return new CasterPropertyValue<>(expected, new DoubleRepr());
    }

    static PropertyValue<Long> longProperty(long expected) {
        return new CasterPropertyValue<>(expected, new LongRepr());
    }

    static PropertyValue<List<String>> listProperty(String... expected) {
        return new PropertyValue<List<String>>(Arrays.asList(expected)) {
            @Override
            Optional<List<String>> getValue(PropertiesManager m, String key, Object... args) {
                return Optional.of(m.getList(key, new StringRepr()));
            }
        };
    }

    private final T expectedValue;

    private PropertyValue(T expectedValue) {
        this.expectedValue = expectedValue;
    }

    boolean getAndCompare(PropertiesManager m, String key, Object... args) {
        return getValue(m, key, args).get().equals(expectedValue);
    }

    T getExpectedValue() {
        return expectedValue;
    }

    abstract Optional<T> getValue(PropertiesManager m, String key, Object... args);

    @Override
    public String toString() {
        return String.valueOf(expectedValue);
    }

    private static class CasterPropertyValue<T> extends PropertyValue<T> {

        private final TypeCaster<String, T> caster;

        CasterPropertyValue(T expectedValue, TypeCaster<String, T> caster) {
            super(expectedValue);
            this.caster = caster;
        }

        @Override
        Optional<T> getValue(PropertiesManager m, String key, Object... args) {
            return m.getValue(key, caster::castOut, args);
        }

    }
}
