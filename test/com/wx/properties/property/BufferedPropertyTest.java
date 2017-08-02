package com.wx.properties.property;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Created by canale on 28.04.16.
 */
public class BufferedPropertyTest extends PropertyTest {

    @Override
    public <T> Property<T> createProperty(T value) {
        return new BufferedProperty<>(new SimpleProperty<>(value));
    }

    @Test
    public void underlyingPropertyIsUpdated() {
        SimpleProperty<Integer> sub = new SimpleProperty<>(24);
        BufferedProperty<Integer> buffered = new BufferedProperty<>(sub);

//        assertEquals(23, sub.get().intValue());

        buffered.clear();
        assertFalse(sub.exists());

        buffered.set(23);
        assertEquals(23, sub.get().intValue());
    }

    @Test
    public void minimalUnderlyingReads() {
        CountProperty<Integer> countSub = new CountProperty<>(24);
        BufferedProperty<Integer> buffered = new BufferedProperty<>(countSub);

        assertEquals(0, countSub.readCount);

        assertEquals(24, buffered.get().intValue());
        assertEquals(1, countSub.readCount);

        assertEquals(24, buffered.get().intValue());
        assertEquals(1, countSub.readCount);

        buffered.set(22);
        assertEquals(1, countSub.readCount);

        assertEquals(22, buffered.get().intValue());
        assertEquals(1, countSub.readCount);

        buffered.clear();
        assertEquals(1, countSub.readCount);
        assertFalse(buffered.exists());
        assertEquals(1, countSub.readCount);
    }

    @Test
    public void minimalUnderlyingWrites() {
        CountProperty<Integer> countSub = new CountProperty<>(24);
        BufferedProperty<Integer> buffered = new BufferedProperty<>(countSub);

        assertEquals(0, countSub.writeCount);
        assertEquals(24, buffered.get().intValue());
        assertEquals(0, countSub.writeCount);

        buffered.set(22);
        assertEquals(1, countSub.writeCount);

        buffered.set(22);
        assertEquals(1, countSub.writeCount);

        buffered.clear();
        assertEquals(1, countSub.writeCount);
    }

    @Test
    public void minimalUnderlyingClears() {
        CountProperty<Integer> countSub = new CountProperty<>(24);
        BufferedProperty<Integer> buffered = new BufferedProperty<>(countSub);

        assertEquals(0, countSub.clearCount);

        buffered.clear();
        assertEquals(1, countSub.clearCount);

        buffered.clear();
        assertEquals(1, countSub.clearCount);

        buffered.set(22);
        buffered.clear();
        assertEquals(2, countSub.clearCount);
    }


    private class CountProperty<T> extends SimpleProperty<T> {

        private int readCount;
        private int writeCount;
        private int clearCount;

        public CountProperty(T value) {
            super(value);
        }

        @Override
        public void set(T value) {
            writeCount++;
            super.set(value);
        }

        @Override
        public T get() {
            readCount++;
            return super.get();
        }

        @Override
        public T clear() {
            clearCount++;
            return super.clear();
        }
    }
}