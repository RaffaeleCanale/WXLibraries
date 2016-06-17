package com.wx.io;


import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;


/**
 * Created on 12/04/2016
 *
 * @author Raffaele Canale (raffaelecanale@gmail.com)
 */
public class CounterInputStreamTest {

    private static final int TEST_SIZE = 1 << 20;
    private static final int BUFFER_SIZE = 1 << 10;

    @Test
    public void testCount() throws IOException {
        CounterInputStream stream = createStream(TEST_SIZE);

        while (stream.read() >= 0);
        stream.close();

        assertEquals(TEST_SIZE, stream.getCount());
    }

    @Test
    public void testCountByBlock() throws IOException {
        CounterInputStream stream = createStream(TEST_SIZE);

        byte[] buffer = new byte[BUFFER_SIZE];
        while (stream.read(buffer) >= 0);
        stream.close();

        assertEquals(TEST_SIZE, stream.getCount());
    }

    @Test
    public void testCountPartial() throws IOException {
        CounterInputStream stream = createStream(TEST_SIZE + 20);

        for (int i = 0; i < TEST_SIZE; i++) {
            assertThat(stream.read(), greaterThanOrEqualTo(0));
        }
        stream.close();

        assertEquals(TEST_SIZE, stream.getCount());
    }

    private CounterInputStream createStream(int size) {
        return new CounterInputStream(new ByteArrayInputStream(new byte[size]));
    }


}