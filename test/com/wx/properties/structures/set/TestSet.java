package com.wx.properties.structures.set;

import com.wx.util.representables.TypeCaster;
import com.wx.util.representables.string.IntRepr;
import com.wx.util.representables.string.StringRepr;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by canale on 02.05.16.
 */
public class TestSet<E> {

    public static TestSet<String> stringSet(String... elements) {
        return new TestSet<>(new StringRepr(), elements);
    }

    public static TestSet<Integer> integerSet(Integer... elements) {
        return new TestSet<>(new IntRepr().nullable(), elements);
    }

    private final Set<E> expectedSet;
    private final TypeCaster<String, E> caster;

    private TestSet(TypeCaster<String, E> caster, E... elements) {
        this(caster, Collections.unmodifiableSet(new HashSet<>(Arrays.asList(elements))));
    }

    public TestSet(TypeCaster<String, E> caster, Set<E> list) {
        this.caster = caster;
        this.expectedSet = list;
    }

    public Set<E> getExpectedSet() {
        return expectedSet;
    }

    public TypeCaster<String, E> getCaster() {
        return caster;
    }

    public TestSet<E> mutable() {
        return new TestSet<>(caster, new HashSet<>(expectedSet));
    }

}
