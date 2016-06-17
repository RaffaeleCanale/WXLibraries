package com.wx.io;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static org.junit.Assert.assertEquals;

/**
 */
public class ProgressInputStreamTest {

    private static final int INTERVAL_SIZE = 10;
    private static final int BYTES_TO_READ_1 = INTERVAL_SIZE * 10 + INTERVAL_SIZE / 2;
    private static final int BYTES_TO_READ_2 = INTERVAL_SIZE * 10;
    private static final int BUFFER_SIZE = 16;

    @Test
    public void testSize1() throws IOException {
        testRead(BYTES_TO_READ_1, INTERVAL_SIZE, false);
    }

    @Test
    public void testSize1Block() throws IOException {
        testRead(BYTES_TO_READ_1, INTERVAL_SIZE, true);
    }

    @Test
    public void testSize2() throws IOException {
        testRead(BYTES_TO_READ_2, INTERVAL_SIZE, false);
    }

    @Test
    public void testSize2Block() throws IOException {
        testRead(BYTES_TO_READ_2, INTERVAL_SIZE, true);
    }

    private void testRead(int size, int interval, boolean readByBlock) throws IOException {


        CounterConsumer counter = new CounterConsumer();
        InputStream subStream = zerosStream(size);

        ProgressInputStream pis = new ProgressInputStream(subStream, counter, interval);

        if (readByBlock) readByBlock(pis); else read(pis);


        assertEquals(computeExpectedValues(size, interval), counter.values);
        System.out.println("Size = " + size + ", interval = " + interval + ", values = " + counter.values);
    }

    private List<Long> computeExpectedValues(int size, int interval) {
        List<Long> expected = new ArrayList<>();

        long total = interval;
        while (total < size) {
            expected.add(total);
            total += interval;
        }

        expected.add((long) size);

        return expected;
    }

    private void read(ProgressInputStream pis) throws IOException {
        while (pis.read() >= 0) {} // Read till the end
        pis.close();
    }

    private void readByBlock(ProgressInputStream pis) throws IOException {
        byte[] buffer = new byte[BUFFER_SIZE];
        while (pis.read(buffer) >= 0) {} // Read till the end
        pis.close();
    }

    private InputStream zerosStream(int sizeToProduce) {
        return new ByteArrayInputStream(new byte[sizeToProduce]);
    }


    private class CounterConsumer implements Consumer<Long> {

        private final List<Long> values = new ArrayList<>();


        @Override
        public void accept(Long value) {
            values.add(value);
        }
    }

}