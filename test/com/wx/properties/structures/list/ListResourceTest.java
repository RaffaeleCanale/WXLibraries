package com.wx.properties.structures.list;

import com.wx.properties.StructureInterface;
import com.wx.util.IteratorTester;
import com.wx.util.representables.TypeCaster;
import com.wx.util.representables.string.StringRepr;
import org.junit.Test;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.wx.properties.structures.list.TestList.integerList;
import static com.wx.properties.structures.list.TestList.stringList;
import static org.junit.Assert.*;

/**
 * Created by canale on 01.05.16.
 */
public abstract class ListResourceTest {

    private enum TestLists {
        NORMAL_LIST(stringList("el1", "el2", "el3", "e4", "e5")),
        EMPTY_LIST(stringList()),
        SINGLETON_LIST(stringList("el1")),
        LIST_WITH_NULL_1(stringList("el1", null, "el3")),
        LIST_WITH_NULL_2(stringList(null, null, "el3")),
        LIST_WITH_EMPTY(stringList("el1", "", "el3")),
        LIST_WITH_SPACE_1(stringList("el1", " ", "el3")),
        LIST_WITH_SPACE_2(stringList("  ", " el2 ", "   ", "    ")),

        INT_LIST(integerList(1, 2, 3, 4)),
        INT_LIST_WITH_NULL(integerList(1, 2, null, 4));

        private final TestList<?> list;

        TestLists(TestList<?> testList) {
            this.list = testList;
        }

        @Override
        public String toString() {
            return name().toLowerCase();
        }
    }


    private static void forEachList(BiConsumer<String, TestList<?>> consumer) {
        for (TestLists testList : TestLists.values()) {
            String key = testList.toString();
            consumer.accept(key, testList.list.mutable());
        }
    }

    private static void forEachNonEmptyList(BiConsumer<String, TestList<?>> consumer) {
        for (TestLists testList : TestLists.values()) {
            if (!testList.list.getExpectedList().isEmpty()) {
                String key = testList.toString();
                consumer.accept(key, testList.list.mutable());
            }
        }
    }

    public abstract <E> ListResource<E> createList(StructureInterface map, TestList<E> list);

    public abstract <E> ListResource<E> getList(StructureInterface map, TypeCaster<String, E> caster);

    public abstract <E> void assertMapContains(StructureInterface map, TestList<E> list);

    private void assertListEquals(String key, StructureInterface map, TestList<?> expectedList, List<?> actualList) {
        assertEquals(key, expectedList.getExpectedList(), actualList);
        assertMapContains(map, expectedList);
    }

    @Test
    public void testCreateList() {
        forEachList((key, list) -> {
            StructureInterface map = new StructureInterface();
            ListResource<?> createdList = createList(map, list);

            assertListEquals(key, map, list, createdList);
        });
    }

    @Test
    public void testReadList() {
        forEachList((key, list) -> {
            StructureInterface map = new StructureInterface();
            createList(map, list);
            ListResource<?> readList = getList(map, list.getCaster());

            assertListEquals(key, map, list, readList);
        });
    }

    @Test
    public void testReadNonExistingList() {
        StructureInterface map = new StructureInterface();
        ListResource<String> list = getList(map, new StringRepr());
        assertNull(list);
    }

    @Test
    public void testCreateExistingList() {
        StructureInterface map = new StructureInterface();

        createList(map, TestLists.NORMAL_LIST.list);
        ListResource<?> secondList = createList(map, TestLists.INT_LIST.list);

        assertListEquals("int_list", map, TestLists.INT_LIST.list, secondList);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testCreateImmutable() {
        TestList<String> testList = (TestList<String>) TestLists.NORMAL_LIST.list.mutable();
        StructureInterface map = new StructureInterface();

        ListResource<String> listResource = createList(map, testList);

        testList.getExpectedList().add("foo");

        assertListEquals("normal_list", map,  TestLists.NORMAL_LIST.list, listResource);
    }

    @Test
    public void testNullArgsCreate() {
        createExpectingNull(null, TestLists.NORMAL_LIST.list);
        createExpectingNull(new StructureInterface(), new TestList<>(null, new LinkedList<>()));
        createExpectingNull(new StructureInterface(), new TestList<>(new StringRepr(), null));
    }

    private void createExpectingNull(StructureInterface map, TestList<?> testList) {
        try {
            createList(map, testList);
            throw new AssertionError("Expected NullPointerException with args: " + map + " / " + testList);
        } catch (NullPointerException e) {
            // Ignore
        }
    }

    @Test
    public void testNullArgsGet() {
        getExpectingNull(null, new StringRepr());
        getExpectingNull(new StructureInterface(), null);
    }

    private void getExpectingNull(StructureInterface map, TypeCaster<String, ?> caster) {
        try {
            getList(map, caster);
            throw new AssertionError("Expected NullPointerException with args: " + map + " / " + caster);
        } catch (NullPointerException e) {
            // Ignore
        }
    }

    @Test
    @SuppressWarnings("unchecked")
    public void addTest() {
        TestList<String> testList = (TestList<String>) TestLists.NORMAL_LIST.list.mutable();

        StructureInterface map = new StructureInterface();
        ListResource<String> list = createList(map, testList);

        list.add("hello");
        testList.getExpectedList().add("hello");

        assertListEquals("normal_list", map, testList, list);
    }

    @Test
    public void addNullTest() {
        forEachList((key, list) -> {
            StructureInterface map = new StructureInterface();
            ListResource<?> readList = createList(map, list);

            readList.add(0, null);
            list.getExpectedList().add(0, null);

            assertListEquals(key, map, list, readList);
        });
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void outOfBoundsTest1() {
        TestList<?> testList = TestLists.NORMAL_LIST.list;
        ListResource<?> actualList = createList(new StructureInterface(), testList);

        actualList.get(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void outOfBoundsTest2() {
        TestList<?> testList = TestLists.NORMAL_LIST.list;
        ListResource<?> actualList = createList(new StructureInterface(), testList);

        actualList.get(testList.getExpectedList().size());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void containsTest() {
        forEachNonEmptyList((key, list) -> {
            ListResource<?> testList = createList(new StructureInterface(), list);
            for (Object el : list.getExpectedList()) {
                assertTrue(testList.contains(el));
            }
            assertTrue(testList.containsAll(list.getExpectedList()));
        });

        TestList<String> expectedList = (TestList<String>) TestLists.NORMAL_LIST.list.mutable();
        ListResource<String> testList = createList(new StructureInterface(), expectedList);
        assertFalse(testList.contains("foo bar"));
        assertFalse(testList.contains(null));

        expectedList.getExpectedList().remove(0);
        assertTrue(testList.containsAll(expectedList.getExpectedList()));

        expectedList.getExpectedList().add("foo bar");
        assertFalse(testList.containsAll(expectedList.getExpectedList()));
    }

    @Test
    public void removeTest1() {
        forEachNonEmptyList((key, list) -> {
            StructureInterface map = new StructureInterface();
            ListResource<?> readList = createList(map, list);

            assertEquals(key, list.getExpectedList().remove(0), readList.remove(0));
            assertListEquals(key, map, list, readList);
        });
    }

    @Test
    public void removeTest2() {
        forEachNonEmptyList((key, list) -> {
            StructureInterface map = new StructureInterface();
            ListResource<?> readList = createList(map, list);

            Object objectToRemove = list.getExpectedList().get(0);
            assertEquals(key, list.getExpectedList().remove(objectToRemove), readList.remove(objectToRemove));
            assertListEquals(key, map, list, readList);
        });
    }

    @Test
    public void clearTest() {
        StructureInterface map = new StructureInterface();

        ListResource<?> list = createList(map, TestLists.NORMAL_LIST.list);
        list.clear();

        assertListEquals("empty_list", map, TestLists.EMPTY_LIST.list, list);
    }

    @Test
    public void propertyRemoveTest() {
        StructureInterface map = new StructureInterface();
        TestList<?> testList = TestLists.NORMAL_LIST.list;

        ListResource<?> list = createList(map, testList);
        List<?> removed = list.removeStructure();

        assertEquals(testList.getExpectedList(), removed);
        assertEquals(0, map.size());
        assertNull(map.getHeader());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void propertyRemoveImmutableTest() {
        StructureInterface map = new StructureInterface();
        TestList<String> testList = (TestList<String>) TestLists.NORMAL_LIST.list;

        ListResource<String> list = createList(map, testList);
        List<String> removed = list.removeStructure();

        try {
            removed.add("hello");

            assertEquals(0, map.size());
            assertNull(map.getHeader());
        } catch (UnsupportedOperationException e) {
            // Ignore
        }
    }

    @Test
    public void subListTest() {
        forEachNonEmptyList((key, list) -> {
            StructureInterface map = new StructureInterface();
            ListResource<?> readList = createList(map, list);

            List<?> readSubList = readList.subList(0, readList.size() - 1);
            List<?> expectedSubList = list.getExpectedList().subList(0, readList.size() - 1);

            assertEquals(key, expectedSubList, readSubList);

            readSubList.clear();
            expectedSubList.clear();
            assertEquals(key, expectedSubList, readSubList);
            assertListEquals(key, map, list, readList);

            readSubList.add(null);
            expectedSubList.add(null);
            assertEquals(key, expectedSubList, readSubList);
            assertListEquals(key, map, list, readList);
        });
    }
//
//    @Test
//    public void iteratorTest1() {
//        forEachNonEmptyList((key, list) -> {
//            StructureInterface map = new StructureInterface();
//            ListResource<?> readList = createList(map, list);
//
//            ListIterator<?> it = readList.listIterator();
//            ListIterator<?> expectedIt = list.getExpectedList().listIterator();
//
//            List<?> collected = collect(it, key);
//
//            assertEquals(key, list.getExpectedList(), collected);
//
//            returnToStart(it, key);
//
//            assertEquals(expectedIt.next(), it.next());
//
//            expectedIt.set(null);
//            it.set(null);
//            assertListEquals(key, map, list, readList);
//
//            expectedIt.remove();
//            it.remove();
//            assertListEquals(key, map, list, readList);
//        });
//    }

    @Test
    public void iteratorTest2() {
        forEachNonEmptyList((key, list) -> {
            StructureInterface map = new StructureInterface();
            ListResource<?> readList = createList(map, list);

            ListIterator<?> it = readList.listIterator();
            ListIterator<?> expectedIt = list.getExpectedList().listIterator();

            while (it.hasNext() && expectedIt.hasNext()) {
                assertEquals(expectedIt.next(), it.next());

                expectedIt.remove();
                it.remove();
                assertListEquals(key, map, list, readList);
            }
            assertFalse(key, it.hasNext());
            assertFalse(key, expectedIt.hasNext());
        });
    }

    @Test
    public void randomIteratorTest() {
        ListIteratorTester tester = new ListIteratorTester();
        tester.test(50, 100);
    }


    private class ListIteratorTester extends IteratorTester<String, List<String>, ListIterator<String>> {

        private final StructureInterface map = new StructureInterface();

        @Override
        protected void compareCollections(List<String> expected, List<String> actual) {
            assertListEquals("iterator test", map, new TestList<>(new StringRepr(), expected), actual);
        }

        @Override
        protected List<Action> iteratorActions() {
            List<Action> actions = super.iteratorActions();

            actions.addAll(Arrays.asList(
                    createAction("hasPrevious", ListIterator::hasPrevious),
                    createAction("previous", ListIterator::previous),
                    createActionWithArg("add", ListIterator::add, Supplier::get),
                    createActionWithArg("set", ListIterator::set, Supplier::get),
                    createAction("nextIndex", ListIterator::nextIndex),
                    createAction("previousIndex", ListIterator::previousIndex)
            ));

            return actions;
        }

        @Override
        public ListIterator<String> getIterator(List<String> collection) {
            return collection.listIterator();
        }

        @Override
        public boolean contains(List<String> expected, String value) {
            return expected.contains(value);
        }

        @Override
        public List<String> createExpected(int collectionSize, Supplier<String> supplier) {
            return Stream.generate(supplier).limit(collectionSize).collect(Collectors.toCollection(() -> new ArrayList<>(collectionSize)));
        }

        @Override
        public List<String> createActual(List<String> expected) {
            return createList(map, new TestList<>(new StringRepr(), expected));
        }
    }


}