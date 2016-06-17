package com.wx.util;

import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by canale on 03.05.16.
 */
public abstract class IteratorTester<T, C, It extends Iterator<T>> {


    public static Supplier<String> stringSupplier() {
        return new Supplier<String>() {
            int i = 0;

            @Override
            public String get() {
                return "value" + (++i);
            }
        };
    }


    private final Supplier<String> supplier = stringSupplier();
    private C expected;
    private C actual;

    public abstract It getIterator(C collection);
    public abstract boolean contains(C expected, T value);

    public abstract C createExpected(int collectionSize, Supplier<String> supplier);
    public abstract C createActual(C expected);

    public void test(int collectionSize, int actionsCount) {
        init(collectionSize);

        testCollect(collectionSize);

        List<Action> actions = randomActions(actionsCount, iteratorActions());
        testActions(actions);
    }

    protected List<Action> iteratorActions() {
        return new ArrayList<>(Arrays.asList(
                createAction("hasNext", It::hasNext),
                createAction("next", It::next),
                createVoidAction("remove", Iterator::remove)
        ));
    }

    protected final Action createAction(String name, Function<It, Object> itFunction) {
        return new Action(name) {
            @Override
            public Object apply(It it) {
                return itFunction.apply(it);
            }
        };
    }

    protected final Action createVoidAction(String name, Consumer<It> itFunction) {
        return new Action(name) {
            @Override
            public Object apply(It it) {
                itFunction.accept(it);
                return null;
            }
        };
    }

    protected final <U> Action createActionWithArg(String name, BiConsumer<It, U> itFunction, Function<Supplier<String>, U> argProvider) {
        return new Action(name) {

            private final U arg = argProvider.apply(supplier);

            @Override
            public Object apply(It it) {
                itFunction.accept(it, arg);
                return null;
            }
        };
    }

    private void init(int collectionSize) {
        expected = createExpected(collectionSize, supplier);
        actual = createActual(expected);
    }

    private void testCollect(int collectionSize) {
        int i = 0;

        Iterator<T> it = getIterator(actual);


        while (it.hasNext() && i < collectionSize) {
            assertTrue(contains(expected, it.next()));
            i++;
        }
        assertFalse(it.hasNext());
    }

    public void testActions(List<Action> actions) {

        It expectedIt = getIterator(expected);
        It actualIt = getIterator(actual);

        List<String> eventsList = new LinkedList<>();
        for (Action action : actions) {
            eventsList.add(action.name);

            try {
                testIteratorAction(expectedIt, actualIt, action);
                compareCollections(expected, actual);
            } catch (Throwable ex) {
                throw new AssertionError("\nExpected: " + expected +
                        "\nActual : " + actual +
                        "\nAction: " + action.name +
                        "\nHistory: " + eventsList + "\n" + ex);
            }
        }
    }

    protected void compareCollections(C expected, C actual) {
        assertEquals(expected, actual);
    }


    private void testIteratorAction(It expectedIt, It actualIt, Action action) {
        Object expectedValue;

        try {
            expectedValue = action.apply(expectedIt);
        } catch (Exception exEx) {
            try {
                action.apply(actualIt);
                throw new AssertionError("Expected exception ", exEx);
            } catch (Exception actualEx) {
                assertEquals("Not same exception thrown", exEx.getClass(), actualEx.getClass());
                return;
            }
        }

        Object actual = action.apply(actualIt);
        assertEquals("Returned value differ", expectedValue, actual);
    }


    private List<Action> randomActions(int count, List<Action> possibleActions) {
        List<Action> selection = new LinkedList<>();
        Random random = new Random();

        for (int i = 0; i < count; i++) {
            selection.add(possibleActions.get(random.nextInt(possibleActions.size())));
        }

        return selection;
    }


    protected abstract class Action {

        private final String name;

        public Action(String name) {
            this.name = name;
        }

        public abstract Object apply(It it);
    }

}
