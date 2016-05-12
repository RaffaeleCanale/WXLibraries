package com.wx.util;

import java.util.NoSuchElementException;

/**
 * Simple and light implementation of a cyclic buffer.
 * <p>
 * If the buffer capacity is full and a new element is added, the oldest entry is removed.
 * <p>
 * Created on 20/11/2014
 *
 * @author Raffaele Canale (raffaelecanale@gmail.com)
 * @version 1.0
 */
public class Buffer<Type> {

    public static Buffer<String> stringBuffer(int size) {
        return new Buffer<>(new String[size]);
    }

    private final Type[] array;
    private int headIndex;
    private int length;

    /**
     * Initialize a new buffer using the given array. Note that none of the values of the given array are used.
     *
     * The size of this array will define the maximum capacity of this buffer.
     *
     * @param array Array to use as buffer
     */
    public Buffer(Type[] array) {
        if (array.length == 0) {
            throw new IllegalArgumentException("Empty array");
        }
        this.array = array.clone();
        this.headIndex = 0;
        this.length = 0;
    }

    /**
     * Number of elements in the buffer. This size is always lesser or equal to {@link #maximumSize()}
     *
     * @return Current size of this buffer
     */
    public int size() {
        return length;
    }

    /**
     * Maximum capacity of this buffer.
     *
     * @return Maximum capacity of this buffer
     */
    public int maximumSize() {
        return array.length;
    }

    /**
     * Remove the first element of this buffer.
     *
     * @return The value stored at the head of the buffer
     */
    public Type removeFirst() {
        if (length == 0) {
            throw new NoSuchElementException("Buffer is already empty");
        }

        int removedIndex = headIndex;

        length--;
        headIndex = (headIndex + 1) % array.length;
        return array[removedIndex];
    }

    /**
     * Add an element to this buffer.
     * <p>
     * <b>Note:</b> If the buffer capacity is full, the oldest element of the buffer will be dropped.
     *
     * @param element Element to add
     */
    public void add(Type element) {
        int tailIndex = (headIndex + length) % array.length;
        array[tailIndex] = element;

        if (length == array.length) {
            headIndex++;
        } else {
            length++;
        }
    }

    /**
     * Get an element in the buffer at the specified index.
     *
     * @param index Index of the buffer
     *
     * @return Element stored at the given index
     */
    public Type get(int index) {
        if (index < 0 || index >= length) {
            throw new IndexOutOfBoundsException();
        }

        int realIndex = (headIndex + index) % array.length;
        return array[realIndex];
    }

}
