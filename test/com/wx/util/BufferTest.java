package com.wx.util;

import org.junit.Test;

import java.util.NoSuchElementException;

import static org.junit.Assert.*;

public class BufferTest {

    private static void assertContentEquals(Buffer<String> buffer, String... values) {
        assertEquals(values.length, buffer.size());

        for (int i = 0; i < values.length; i++) {
            assertEquals(values[i], buffer.get(i));
        }
    }

    private static void simpleTests(int size) {
        Buffer<String> buffer = new Buffer<>(new String[size]);

        for (int i = 0; i < size; i++) {
            buffer.add("e" + i);
        }

        for (int i = 0; i < size; i++) {
            assertEquals("e" + i, buffer.get(i));
        }

        for (int i = 0; i < size; i++) {
            buffer.add("e" + (size + i));
            for (int j = 0; j < size; j++) {
                assertEquals("e" + (i+j+1), buffer.get(j));
            }
        }
    }

    @Test
    public void simpleTests() {
        simpleTests(10);
    }

    @Test
    public void emptyBufferTest() {
        Buffer<String> buffer = new Buffer<>(new String[2]);

        assertEquals(0, buffer.size());
        assertEquals(2, buffer.maximumSize());
    }

    @Test
    public void immutableConstructorTest() {
        String[] array = {"hello", "world"};
        Buffer<String> buffer = new Buffer<>(array);

        assertContentEquals(buffer);

        buffer.add("foo");
        assertContentEquals(buffer, "foo");
    }

    @Test
    public void addTest() {
        Buffer<String> buffer = Buffer.stringBuffer(5);
        buffer.add("hello");
        assertContentEquals(buffer, "hello");

        buffer.add("world");
        assertContentEquals(buffer, "hello", "world");
    }

    @Test(expected = NoSuchElementException.class)
    public void emptyRemoveTest() {
        Buffer<String> buffer = Buffer.stringBuffer(5);
        buffer.removeFirst();
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void oobTest() {
        Buffer.stringBuffer(5).get(0);
    }

    @Test
    public void outOfCapacityAdd() {
        Buffer<String> buffer = Buffer.stringBuffer(2);

        buffer.add("hello");
        buffer.add("world");

        assertContentEquals(buffer, "hello", "world");

        buffer.add("foo");
        assertContentEquals(buffer, "world", "foo");
    }

    @Test(expected = IllegalArgumentException.class)
    public void zeroSizeBufferTest() {
        Buffer.stringBuffer(0);
    }

    @Test(expected = NullPointerException.class)
    public void nullConstructorTest() {
        new Buffer<>(null);
    }

    @Test
    public void removeTest() {
        Buffer<String> buffer = new Buffer<>(new String[10]);

        buffer.add("e1");
        buffer.add("e2");
        buffer.add("e3");

        buffer.removeFirst();
        assertEquals("e2", buffer.get(0));

        buffer.removeFirst();
        assertEquals("e3", buffer.get(0));
    }
}