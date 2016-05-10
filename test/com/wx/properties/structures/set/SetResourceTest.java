package com.wx.properties.structures.set;

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

import static com.wx.properties.structures.set.TestSet.integerSet;
import static com.wx.properties.structures.set.TestSet.stringSet;
import static org.junit.Assert.*;

/**
 * Created by canale on 02.05.16.
 */
public abstract class SetResourceTest {

    private static final int MAX_TEST_SET_SIZE = Stream.of(SetResourceTest.TestSets.values()).mapToInt(t -> t.set.getExpectedSet().size() + 1).max().orElse(1);

    private enum TestSets {
        NORMAL_SET(stringSet("el1", "el2", "el3", "e4", "e5")),
        EMPTY_SET(stringSet()),
        SINGLETON_SET(stringSet("el1")),
        SET_WITH_NULL_1(stringSet("el1", null, "el3")),
        SET_WITH_NULL_2(stringSet((String) null)),
        SET_WITH_EMPTY(stringSet("el1", "", "el3")),
        SET_WITH_SPACE_1(stringSet("el1", " ", "el3")),
        SET_WITH_SPACE_2(stringSet("  ", " el2 ", "   ", "    ")),

        INT_SET(integerSet(1, 2, 3, 4)),
        INT_SET_WITH_NULL(integerSet(1, 2, null, 4));

        private final TestSet<?> set;

        TestSets(TestSet<?> testSet) {
            this.set = testSet;
        }

        @Override
        public String toString() {
            return name().toLowerCase();
        }
    }


    private static void forEachSet(BiConsumer<String, TestSet<?>> consumer) {
        for (SetResourceTest.TestSets testSet : SetResourceTest.TestSets.values()) {
            String key = testSet.toString();
            consumer.accept(key, testSet.set.mutable());
        }
    }

    private static void forEachNonEmptySet(BiConsumer<String, TestSet<?>> consumer) {
        for (SetResourceTest.TestSets testSet : SetResourceTest.TestSets.values()) {
            if (!testSet.set.getExpectedSet().isEmpty()) {
                String key = testSet.toString();
                consumer.accept(key, testSet.set.mutable());
            }
        }
    }

    public abstract <E> SetResource<E> createSet(StructureInterface map, TestSet<E> set);

    public abstract <E> SetResource<E> getSet(StructureInterface map, TypeCaster<String, E> caster);

    public abstract <E> void assertMapContains(StructureInterface map, TestSet<E> set);

    private void assertSetEquals(String key, StructureInterface map, TestSet<?> expectedSet, Set<?> actualSet) {
        assertEquals(key, expectedSet.getExpectedSet(), actualSet);
        assertMapContains(map, expectedSet);
    }

    @Test
    public void testCreateSet() {
        forEachSet((key, set) -> {
            StructureInterface map = new StructureInterface();
            SetResource<?> createdSet = createSet(map, set);

            assertSetEquals(key, map, set, createdSet);
        });
    }

    @Test
    public void testReadSet() {
        forEachSet((key, set) -> {
            StructureInterface map = new StructureInterface();
            createSet(map, set);
            SetResource<?> readSet = getSet(map, set.getCaster());

            assertSetEquals(key, map, set, readSet);
        });
    }

    @Test
    public void testReadNonExistingSet() {
        StructureInterface map = new StructureInterface();
        SetResource<String> set = getSet(map, new StringRepr());
        assertNull(set);
    }

    @Test
    public void testCreateExistingSet() {
        StructureInterface map = new StructureInterface();

        createSet(map, SetResourceTest.TestSets.NORMAL_SET.set);
        SetResource<?> secondSet = createSet(map, SetResourceTest.TestSets.INT_SET.set);

        assertSetEquals("int_set", map, SetResourceTest.TestSets.INT_SET.set, secondSet);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testCreateImmutable() {
        TestSet<String> testSet = (TestSet<String>) SetResourceTest.TestSets.NORMAL_SET.set.mutable();
        StructureInterface map = new StructureInterface();

        SetResource<String> setResource = createSet(map, testSet);

        testSet.getExpectedSet().add("foo");

        assertSetEquals("normal_set", map,  SetResourceTest.TestSets.NORMAL_SET.set, setResource);
    }

    @Test
    public void testNullArgsCreate() {
        createExpectingNull(null, SetResourceTest.TestSets.NORMAL_SET.set);
        createExpectingNull(new StructureInterface(), new TestSet<>(null, new HashSet<>()));
        createExpectingNull(new StructureInterface(), new TestSet<>(new StringRepr(), null));
    }

    private void createExpectingNull(StructureInterface map, TestSet<?> testSet) {
        try {
            createSet(map, testSet);
            throw new AssertionError("Expected NullPointerException with args: " + map + " / " + testSet);
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
            getSet(map, caster);
            throw new AssertionError("Expected NullPointerException with args: " + map + " / " + caster);
        } catch (NullPointerException e) {
            // Ignore
        }
    }

    @Test
    @SuppressWarnings("unchecked")
    public void addTest() {
        TestSet<String> testSet = (TestSet<String>) SetResourceTest.TestSets.NORMAL_SET.set.mutable();

        StructureInterface map = new StructureInterface();
        SetResource<String> set = createSet(map, testSet);

        assertEquals(testSet.getExpectedSet().add("hello"), set.add("hello"));

        assertSetEquals("normal_set", map, testSet, set);
    }

    @Test
    public void addNullTest() {
        forEachSet((key, set) -> {
            StructureInterface map = new StructureInterface();
            SetResource<?> readSet = createSet(map, set);


            assertEquals(key, set.getExpectedSet().add(null), readSet.add(null));

            assertSetEquals(key, map, set, readSet);
        });
    }

    @Test
    @SuppressWarnings("unchecked")
    public void containsTest() {
        forEachNonEmptySet((key, set) -> {
            SetResource<?> testSet = createSet(new StructureInterface(), set);
            for (Object el : set.getExpectedSet()) {
                assertTrue(key + " with " + el, testSet.contains(el));
            }
            assertTrue(testSet.containsAll(set.getExpectedSet()));
        });

        TestSet<String> expectedSet = (TestSet<String>) SetResourceTest.TestSets.NORMAL_SET.set.mutable();
        SetResource<String> testSet = createSet(new StructureInterface(), expectedSet);
        assertFalse(testSet.contains("foo bar"));
        assertFalse(testSet.contains(null));

        expectedSet.getExpectedSet().remove(expectedSet.getExpectedSet().iterator().next());
        assertTrue(testSet.containsAll(expectedSet.getExpectedSet()));

        expectedSet.getExpectedSet().add("foo bar");
        assertFalse(testSet.containsAll(expectedSet.getExpectedSet()));
    }

    @Test
    public void removeTest2() {
        forEachNonEmptySet((key, set) -> {
            StructureInterface map = new StructureInterface();
            SetResource<?> readSet = createSet(map, set);

            Object objectToRemove = set.getExpectedSet().iterator().next();
            assertEquals(key, set.getExpectedSet().remove(objectToRemove), readSet.remove(objectToRemove));
            assertSetEquals(key, map, set, readSet);
        });
    }

    @Test
    public void clearTest() {
        StructureInterface map = new StructureInterface();

        SetResource<?> set = createSet(map, SetResourceTest.TestSets.NORMAL_SET.set);
        set.clear();

        assertSetEquals("empty_set", map, SetResourceTest.TestSets.EMPTY_SET.set, set);
    }

    @Test
    public void propertyRemoveTest() {
        StructureInterface map = new StructureInterface();
        TestSet<?> testSet = SetResourceTest.TestSets.NORMAL_SET.set;

        SetResource<?> set = createSet(map, testSet);
        Set<?> removed = set.removeStructure();

        assertEquals(testSet.getExpectedSet(), removed);
        assertEquals(0, map.size());
        assertNull(map.getHeader());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void propertyRemoveImmutableTest() {
        StructureInterface map = new StructureInterface();
        TestSet<String> testSet = (TestSet<String>) SetResourceTest.TestSets.NORMAL_SET.set;

        SetResource<String> set = createSet(map, testSet);
        Set<String> removed = set.removeStructure();

        try {
            removed.add("hello");

            assertEquals(0, map.size());
            assertNull(map.getHeader());
        } catch (UnsupportedOperationException e) {
            // Ignore
        }
    }

    @Test
    public void iteratorTest() {
        new SetIteratorTester().test(50, 100);
    }

    @Test
    public void iteratorTest2() {
        forEachNonEmptySet((key, set) -> {
            StructureInterface map = new StructureInterface();
            SetResource<?> actualSet = createSet(map, set);

            Iterator<?> it = actualSet.iterator();

            int i = 0;
            while (it.hasNext() && i < MAX_TEST_SET_SIZE) {
                Object toRemove = it.next();
                it.remove();

                assertTrue(key, set.getExpectedSet().remove(toRemove));
                assertSetEquals(key, map, set, actualSet);
                i++;
            }
            assertFalse(key, it.hasNext());
        });
    }

    private class SetIteratorTester extends IteratorTester<String, Collection<String>, Iterator<String>> {

        private final StructureInterface map = new StructureInterface();

        @Override
        protected void compareCollections(Collection<String> expected, Collection<String> actual) {
            Set<String> expSet = new HashSet<>(expected);
            Set<String> actSet = (Set<String>) actual;

            assertSetEquals("iterator test", map, new TestSet<>(new StringRepr(), expSet), actSet);
        }

        @Override
        public Iterator<String> getIterator(Collection<String> collection) {
            return collection.iterator();
        }

        @Override
        public Collection<String> createExpected(int collectionSize, Supplier<String> supplier) {
            return Stream.generate(supplier).limit(collectionSize).collect(Collectors.toCollection(ArrayList::new));
        }

        @Override
        public Set<String> createActual(Collection<String> expected) {
            SetResource<String> result = createSet(map, new TestSet<>(new StringRepr(), new HashSet<>(expected)));

            expected.clear();
            expected.addAll(result);

            return result;
        }

        @Override
        public boolean contains(Collection<String> expected, String value) {
            return expected.contains(value);
        }
    }

}