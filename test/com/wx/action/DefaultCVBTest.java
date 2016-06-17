package com.wx.action;

import com.wx.action.type.DefaultCVB;
import com.wx.action.util.PropertyContainer;
import com.wx.action.command.Command;
import com.wx.action.command.DefaultCommandBuilder;
import com.wx.util.representables.string.StringRepr;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class DefaultCVBTest {


//    public CommandValueBuilder<String> builder(int min, int max, String marker, String... defaults) {
//        final PropertyContainer.Builder builder = new PropertyContainer.Builder();
//
//        builder.setType("String");
//        builder.setMin(min);
//        builder.setMax(max);
//
//        for (String def : defaults) {
//            builder.addDefaultValues(def);
//        }
//
//        if (marker != null) {
//            builder.addMarkers(marker);
//        }
//
//        return new CommandValueBuilder<>(builder.build(), new StringRepr());
//    }
//
//
//    public Command apply(String cmd, int min, int max, String marker, String... defaults) {
//        final Command c = new DefaultCommandBuilder().build(cmd);
//        final CommandValueBuilder<String> builder = builder(min, max, marker, defaults);
//
//        return builder.registerFrom(c);
//    }
//
//    public Command test(String cmd, int min, int max, String marker, String... defaults) {
//
//    }


    @Test
    public void testSingleArgument() {
        new TestBuilder("foo")
                .expectedValues("foo")
                .expectedRemainingCmd("").run();
    }

    @Test
    public void testTwoArgs() {
        new TestBuilder("foo bar")
                .min(2).max(2)
                .expectedValues("foo", "bar")
                .expectedRemainingCmd("").run();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMissingArg() {
        new TestBuilder("foo")
                .min(2).max(2).run();
    }

    @Test
    public void testSingleArgument2() {
        new TestBuilder("foo bar")
                .min(0).max(1)
                .expectedValues("foo")
                .expectedRemainingCmd("bar").run();
    }

    @Test
    public void testDefaultArgs() {
        new TestBuilder("-f john doe")
                .min(1).max(2)
                .defaults("foo", "bar")
                .expectedValues("foo", "bar")
                .expectedRemainingCmd("-f john doe").run();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDefaultArgs2() {
        new TestBuilder("foo")
                .min(2).max(2)
                .defaults("foo", "bar").run();
    }



    @Test
    public void testMarker1() {
        new TestBuilder("foo -f bar")
                .markers("-f")
                .min(1).max(1)
                .expectedValues("bar")
                .expectedRemainingCmd("foo").run();
    }

    @Test
    public void testMarker2() {
        new TestBuilder("test -b bar -f foo")
                .markers("-b", "-f")
                .min(1).max(10)
                .expectedValues("bar")
                .expectedRemainingCmd("test -f foo").run();
    }

    @Test
    public void testMarker2_2() {
        new TestBuilder("test -b bar -f foo")
                .markers("-f", "-b")
                .min(1).max(10)
                .expectedValues("foo")
                .expectedRemainingCmd("test -b bar").run();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMarker3() {
        new TestBuilder("test -f")
                .markers("-f")
                .min(1).max(1).run();
    }

    @Test
    public void testMarker4() {
        new TestBuilder("-f foo bar toto titi")
                .markers("-f", "-b")
                .min(1).max(3)
                .expectedValues("foo", "bar", "toto")
                .expectedRemainingCmd("titi").run();
    }

    @Test
    public void testMarker5() {
        new TestBuilder("test -f")
                .markers("-f")
                .min(0).max(1)
                .expectedValues() // Empty list
                .expectedRemainingCmd("test").run();
    }


    @Test
    public void testMarker6() {
        new TestBuilder("test -b bar")
                .markers("-f")
                .min(1).max(1)
                .expectedValues((String[]) null)
                .expectedRemainingCmd("test -b bar").run();
    }

    @Test
    public void testMarkerDefaults() {
        new TestBuilder("test -b bar")
                .markers("-f")
                .defaults("toto")
                .min(1).max(1)
                .expectedValues("toto")
                .expectedRemainingCmd("test -b bar").run();
    }

    private class TestBuilder {
        private final String cmdValue;
        private int min = 1;
        private int max = 1;
        private String[] markers = {};
        private String[] defaults = {};

        private List<String> expectedValues;
        private String  expectedRemainingCommand;

        private TestBuilder(String cmd) {
            this.cmdValue = cmd;
        }

        public TestBuilder min(int min) {
            this.min = min;
            return this;
        }

        public TestBuilder max(int max) {
            this.max = max;
            return this;
        }

        public TestBuilder defaults(String... defaults) {
            this.defaults = defaults;
            return this;
        }

        public TestBuilder markers(String... markers) {
            this.markers = markers;
            return this;
        }

        public TestBuilder expectedValues(String... values) {
            this.expectedValues = values == null ? null : Arrays.asList(values);
            return this;
        }

        public TestBuilder expectedRemainingCmd(String cmd) {
            this.expectedRemainingCommand = cmd;
            return this;
        }

        public void run() {
            final PropertyContainer.Builder builder = new PropertyContainer.Builder();

            builder.setType("String");
            builder.setMin(min);
            builder.setMax(max);

            for (String def : defaults) {
                builder.addDefaultValue(def);
            }
            for (String marker : markers) {
                builder.addMarkers(marker);
            }

            final DefaultCVB<String> cvb = new DefaultCVB<>(builder.build(), new StringRepr());
            final Command cmd = new DefaultCommandBuilder().build("test " + cmdValue);

            final Command rmc = cvb.registerFrom(cmd);

            assertEquals(expectedValues, cvb.getValues());
            assertEquals(new DefaultCommandBuilder().build("test " + expectedRemainingCommand), rmc);
        }
    }
}