package com.wx.action.help;

import com.wx.grammar.ParseException;
import com.wx.io.sp.SPObject;
import com.wx.io.sp.SPObjectParser;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class SPObjectParserTest {


    @Test
    public void testExample() throws IOException, ParseException {
        InputStream is = SPObjectParserTest.class.getResourceAsStream("SPObjectExample.sp");
        SPObject object = new SPObjectParser().parse(is);

        TestRunner tester = new TestRunner();

        TestRunner obj1 = tester.object("OBJ1")
                .property("p1", "p1 text")
                .property("p2", "p2 text");
        obj1.object("OBJ11");
        obj1.object("OBJ12").property("p3", "p3\ntext\n\tand\n\ttext");

        tester.object("OBJ2").property("p1", "p1 text");

        System.out.println(object);
        tester.run(object);
    }



    private class TestRunner {
        private final Map<String, String> expectedProperties = new HashMap<>();
        private final Map<String, TestRunner> expectedObjects = new HashMap<>();

        public TestRunner property(String key, String property) {
            expectedProperties.put(key, property);

            return this;
        }

        public TestRunner object(String key) {
            TestRunner testRunner = new TestRunner();
            expectedObjects.put(key, testRunner);

            return testRunner;
        }


        public void run(SPObject object) {
            assertNotNull(object);

            assertEquals(expectedProperties.size(), object.propertiesKeySet().size());
            assertEquals(expectedObjects.size(), object.objectsKeySet().size());

            for (Map.Entry<String, String> propEntry : expectedProperties.entrySet()) {
                assertEquals(propEntry.getValue(), object.getString(propEntry.getKey()));
            }

            for (Map.Entry<String, TestRunner> objEntry : expectedObjects.entrySet()) {
                objEntry.getValue().run(object.getObject(objEntry.getKey()));
            }
        }

    }
}