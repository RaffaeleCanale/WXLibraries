package com.wx.properties;

import com.wx.properties.structures.list.SingleLineList;
import com.wx.util.representables.string.DoubleRepr;
import com.wx.util.representables.string.ListRepr;
import com.wx.util.representables.string.StringRepr;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

import static com.wx.properties.PropertyValue.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by Raffaele Canale (<a href="mailto:raffaelecanale@gmail.com?subject=WXLibraries">raffaelecanale@gmail.com</a>) on 28.04.16.
 */
public class PropertiesManagerTest {

    private enum TestProperties {
        TEST_STRING_1("foo", stringProperty("foo")),
        TEST_STRING_2(" ", stringProperty(" ")),
        TEST_STRING_3("", stringProperty("")),
        TEST_STRING_4(" ", stringProperty(" ")),

        TEST_INT_1("24", intProperty(24)),
        TEST_INT_2("-2", intProperty(-2)),
        TEST_INT_3("000", intProperty(0)),

        TEST_DOUBLE_1("24.5", doubleProperty(24.5)),
        TEST_DOUBLE_2("22", doubleProperty(24.5)),
        TEST_DOUBLE_3("0.00", doubleProperty(0.0)),
        TEST_DOUBLE_4("-00.5", doubleProperty(-0.5)),

        TEST_LONG_1("2323432424", longProperty(2323432424L)),
        TEST_LONG_2("0", longProperty(0L)),

        TEST_FIELD_1("hello ${field:" + TEST_INT_1 + "} world", stringProperty("hello " + TEST_INT_1.value.toString() + " world")),
        TEST_FIELD_2("${field:" + TEST_STRING_1 + "}/${field:" + TEST_FIELD_1 + "}", stringProperty(TEST_STRING_1.value.toString() + "/" + TEST_FIELD_1.value.toString())),
        TEST_FIELD_3("${field:" + TEST_INT_1 + "}${field:" + TEST_DOUBLE_1 + "}", doubleProperty(2424.5)),

        TEST_PARAM_1("Hello ${param:0}", stringProperty("Hello World"), "World"),

        TEST_LIST_1(listProperty("foo", "bar"), "foo", "bar"),
        _TEST_LIST_1(listProperty()),
        TEST_LIST_1_(listProperty(""), "");


        private final PropertyValue<?> value;
        private final BiConsumer<Map<String, String>, String> populateFunction;
        private final String[] args;

        TestProperties(String rawValue, PropertyValue<?> realValue, String... args) {
            this.populateFunction = (m,k) -> m.put(k, rawValue);
            this.value = realValue;
            this.args = args;
        }

        TestProperties(PropertyValue<? extends Collection<?>> realValue, String... values) {
            this.populateFunction = (m,k) -> {
                StructuresHelper.StructInfo info = StructuresHelper.putStructInfo(k, m, SingleLineList.class.getName());
                SingleLineList.factory().createList(info.view, new StringRepr(), Arrays.asList(values));
            };
            this.value = realValue;
            this.args = new String[0];
        }

        @Override
        public String toString() {
            return name().toLowerCase();
        }
    }


    private static final String MISSING_ELEMENT = "object";

    private static PropertiesManager exampleManager() {
        Map<String, String> map = new HashMap<>();

        for (TestProperties prop : TestProperties.values()) {
            prop.populateFunction.accept(map, prop.toString());
        }

        return new PropertiesManager(map);
    }

    private static void assertTrueForEach(Function<TestProperties, Boolean> function) {
        for (TestProperties prop : TestProperties.values()) {
            assertTrue("Condition failed for " + prop, function.apply(prop));
        }
    }

    private PropertiesManager manager;

    @Before
    public void initManager() {
        manager = exampleManager();
    }

    @Test
    public void containsKey() {
        assertTrueForEach(p -> manager.containsKey(p.toString()));

        assertFalse(manager.containsKey(MISSING_ELEMENT));
        assertFalse(manager.containsKey("st." + TestProperties.TEST_LIST_1.name()));
    }

    @Test
    public void getProperty() {
        for (TestProperties p : TestProperties.values()) {
            p.value.getAndCompare(manager, p.toString(), (Object[]) p.args);
        }
    }

    @Test
    public void removeProperty() {

    }

    @Test
    public void removeProperties() {

    }

    @Test
    public void keySet() {

    }

}