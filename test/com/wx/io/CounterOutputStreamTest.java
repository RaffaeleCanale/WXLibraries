package com.wx.io;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.junit.Assert.*;

/**
 * Created on 12/04/2016
 *
 * @author Raffaele Canale (raffaelecanale@gmail.com)
 */
public class CounterOutputStreamTest {

    private static final int TEST_SIZE = 1 << 20;
    private static final int BUFFER_SIZE = 1 << 10;

    @Test
    public void testCount() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        CounterOutputStream stream = new CounterOutputStream(baos);

        for (int i = 0; i < TEST_SIZE; i++) {
            stream.write(0);
        }
        stream.close();

        assertEquals(TEST_SIZE, stream.getCount());
        assertEquals(TEST_SIZE, baos.size());
    }

    @Test
    public void testCountByBlock() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        CounterOutputStream stream = new CounterOutputStream(baos);

        int iterations = (int) Math.ceil( (double) TEST_SIZE / (double) BUFFER_SIZE );
        int actualSize = iterations * BUFFER_SIZE;

        byte[] buffer = new byte[BUFFER_SIZE];
        for (int i = 0; i < iterations; i++) {
            stream.write(buffer);
        }
        stream.close();

        assertEquals(actualSize, stream.getCount());
        assertEquals(actualSize, baos.size());
    }


}