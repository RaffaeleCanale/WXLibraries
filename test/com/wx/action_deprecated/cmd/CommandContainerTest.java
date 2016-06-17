package com.wx.action_deprecated.cmd;


import com.wx.action.arg.ArgumentsSupplier;
import com.wx.action.util.CommandContainer;
import com.wx.action.command.Command;
import com.wx.action.command.DefaultCommandBuilder;
import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class CommandContainerTest {

    @Test
    public void testToArguments() {
        runner("foo bar")
                .property(1, 1, "String")
                .property(1, 1, "String")
                .expectArg("foo")
                .expectArg("bar").run();
    }

    @Test
    public void testToArguments2() {
        runner("foo bar")
                .property(1, 2, "String")
                .expectList("foo", "bar").run();
    }

    @Test
    public void testToArguments3() {
        runner("foo bar toto titi")
                .property(1, 2, "String")
                .property(1, 2, "String")
                .expectList("foo", "bar")
                .expectList("toto", "titi")
                .run();
    }

    @Test
    public void testToArguments4() {
        runner("foo bar -t toto")
                .property(2, 2, "String")
                .property(1, 10, "String")
                .property(0, 0, "String", "-t")
                .expectList("foo", "bar")
                .expectList("toto")
                .expectArg((String) null)
                .run();
    }

    @Test
    public void testToArguments5() {
        runner("foo bar toto -t")
                .property(2, 2, "String")
                .property(1, 10, "String")
                .property(0, 2, "String", "-t")
                .expectList("foo", "bar")
                .expectList("toto")
                .expectList(String.class)
                .run();
    }


    @Test(expected = IllegalArgumentException.class)
    public void testToArguments6() {
        runner("foo bar 24 22 toto -t")
                .property(0, 2, "String")
                .property(1, 10, "Integer")
                .run();
    }

    @Test
    public void testToArguments7() {
        runner("foo bar 21 24 22 -t toto")
                .property(2, 3, "String")
                .property(1, 10, "Integer")
                .property(0, 2, "String", "-t")
                .expectList("foo", "bar", "21")
                .expectList(24, 22)
                .expectList("toto")
                .run();
    }


    private TestRunner runner(String cmd) {
        return new TestRunner(cmd);
    }

    private class TestRunner {


        private final String cmdValue;
        private final CommandContainer.Builder builder = new CommandContainer.Builder();
        private int count = 0;

        private List<Consumer<ArgumentsSupplier>> expectedValues = new LinkedList<>();


        private TestRunner(String cmd) {
            this.cmdValue = cmd;
        }

        @SafeVarargs
        public final <T> TestRunner expectList(Class<T> c, T... expected) {
            expectedValues.add(as -> assertEquals(
                    expected == null ? null : Arrays.asList(expected),
                    as.supplyList(c)));

            return this;
        }
        //<editor-fold desc="Overload">
        public TestRunner expectList(String... expected) {
            return expectList(String.class, expected);
        }
        public TestRunner expectList(Integer... expected) {
            return expectList(Integer.class, expected);
        }
        //</editor-fold>

        public <T> TestRunner expectArg(Class<T> c, T expected) {
            expectedValues.add(as -> assertEquals(expected, as.supply(c)));
            return this;
        }
        //<editor-fold desc="Overload">
        public TestRunner expectArg(String expected) {
            return expectArg(String.class, expected);
        }
        public TestRunner expectArg(Integer expected) {
            return expectArg(Integer.class, expected);
        }
        //</editor-fold>

        public TestRunner property(int min, int max, String type, String... markers) {
            count++;
            builder.createNewProperty("p" + count)
                    .setMin(min)
                    .setMax(max)
                    .setType(type)
                    .addMarkers(markers);
            return this;
        }

        public void run() {
            final Command cmd = new DefaultCommandBuilder().build("test " + cmdValue);
            final CommandContainer cc = builder.build();

            ArgumentsSupplier actionArgs = cc.toArguments(cmd);


            for (Consumer<ArgumentsSupplier> expectedValue : expectedValues) {
                expectedValue.accept(actionArgs);
            }

            assertFalse("Arguments are remaining:\n" + actionArgs, actionArgs.hasMore());
        }
    }
}