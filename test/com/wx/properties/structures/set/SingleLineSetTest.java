package com.wx.properties.structures.set;

import com.wx.properties.StructureInterface;
import com.wx.util.representables.DelimiterEncoder;
import com.wx.util.representables.TypeCaster;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

/**
 * Created by canale on 02.05.16.
 */
public class SingleLineSetTest extends SetResourceTest {

    private static final String SEPARATOR = "/";
    private static final String[] EXAMPLE_SEPARATORS = {SEPARATOR, "\\", "_"};


    @Test(expected = ClassCastException.class)
    public void separatorTest1() {
        TestSet<String> testSet = TestSet.stringSet("el1", "el2" + SEPARATOR, "el3");

        createSet(new StructureInterface(), testSet);
    }

    @Test(expected = ClassCastException.class)
    public void separatorTest2() {
        TestSet<String> testSet = TestSet.stringSet("el1", "el2", "el3");

        SetResource<String> list = createSet(new StructureInterface(), testSet);

        list.add(SEPARATOR);
    }

    @Test
    public void separatorTest3() {
        TestSet<String> testSet = TestSet.stringSet("el1", "el2", "el3").mutable();

        SetResource<String> list = SingleLineSet.factory().createSet(new StructureInterface(), testSet.getCaster(), testSet.getExpectedSet(), null);
        assertEquals(testSet.getExpectedSet(), list);

        list.add(SEPARATOR);
        testSet.getExpectedSet().add(SEPARATOR);

        assertEquals(testSet.getExpectedSet(), list);
    }

    @Override
    public <E> SetResource<E> createSet(StructureInterface map, TestSet<E> set) {
        return SingleLineSet.factory().createSet(map, set.getCaster(), set.getExpectedSet(), SEPARATOR);
    }

    @Override
    public <E> SetResource<E> getSet(StructureInterface map, TypeCaster<String, E> caster) {
        return SingleLineSet.factory().loadSet(map, caster, SEPARATOR);
    }

    @Override
    public <E> void assertMapContains(StructureInterface map, TestSet<E> set) {
        assertEquals(0, map.size());

        Set<E> decoded = DelimiterEncoder.decode(SEPARATOR, map.getHeader())
                .stream().map(set.getCaster()::castOut).collect(Collectors.toSet());
        assertEquals(set.getExpectedSet(), decoded);
    }
}