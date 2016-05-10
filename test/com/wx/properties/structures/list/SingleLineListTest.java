package com.wx.properties.structures.list;

import com.wx.properties.StructureInterface;
import com.wx.util.representables.DelimiterEncoder;
import com.wx.util.representables.TypeCaster;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;

/**
 * Created by canale on 02.05.16.
 */
public class SingleLineListTest extends ListResourceTest {

    private static final String SEPARATOR = "/";
    private static final String[] EXAMPLE_SEPARATORS = {SEPARATOR, "\\", "_"};

    @Test(expected = ClassCastException.class)
    public void separatorTest1() {
        TestList<String> testList = TestList.stringList("el1", "el2" + SEPARATOR, "el3");

        createList(new StructureInterface(), testList);
    }

    @Test(expected = ClassCastException.class)
    public void separatorTest2() {
        TestList<String> testList = TestList.stringList("el1", "el2", "el3");

        ListResource<String> list = createList(new StructureInterface(), testList);

        list.add(SEPARATOR);
    }

    @Test
    public void separatorTest3() {
        TestList<String> testList = TestList.stringList("el1", "el2", "el3").mutable();

        ListResource<String> list = SingleLineList.factory().createList(new StructureInterface(), testList.getCaster(), testList.getExpectedList());
        assertEquals(testList.getExpectedList(), list);

        list.add(SEPARATOR);
        testList.getExpectedList().add(SEPARATOR);

        assertEquals(testList.getExpectedList(), list);
    }

    @Override
    public <E> ListResource<E> createList(StructureInterface map, TestList<E> list) {
        return SingleLineList.factory().createList(map, list.getCaster(), list.getExpectedList(), SEPARATOR);
    }

    @Override
    public <E> ListResource<E> getList(StructureInterface map, TypeCaster<String, E> caster) {
        return SingleLineList.factory().loadList(map, caster, SEPARATOR);
    }

    @Override
    public <E> void assertMapContains(StructureInterface map, TestList<E> list) {
        assertEquals(0, map.size());

        Stream<String> castedList = list.getExpectedList().stream()
                .map(list.getCaster()::castIn);
        assertEquals(DelimiterEncoder.encode(SEPARATOR, castedList), map.getHeader());
    }
}