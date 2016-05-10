package com.wx.properties.structures.list;

import com.wx.util.representables.TypeCaster;
import com.wx.util.representables.string.IntRepr;
import com.wx.util.representables.string.StringRepr;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by canale on 02.05.16.
 */
public class TestList<E> {

    public static TestList<String> stringList(String... elements) {
        return new TestList<>(new StringRepr(), Arrays.asList(elements));
    }

    public static TestList<Integer> integerList(Integer... elements) {
        return new TestList<>(new IntRepr().nullable(), Arrays.asList(elements));
    }

    private final List<E> expectedList;
    private final TypeCaster<String, E> caster;

    public TestList(TypeCaster<String, E> caster, List<E> list) {
        this.caster = caster;
        this.expectedList = list;
    }

    public List<E> getExpectedList() {
        return expectedList;
    }

    public TypeCaster<String, E> getCaster() {
        return caster;
    }

    public TestList<E> mutable() {
        return new TestList<>(caster, new LinkedList<>(expectedList));
    }

    @Override
    public String toString() {
        return caster + ">" + expectedList;
    }
}
