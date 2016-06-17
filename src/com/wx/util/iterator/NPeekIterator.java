package com.wx.util.iterator;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;

/**
 * This class enhances any iterator by offering the ability to peek at N values (ie. to access the next N values without
 * calling next or changing the state of the iterator).
 * <p>
 * <b>Note:</b> The implementation may call the {@link Iterator#next()} method arbitrarily, if you need a clear control
 * on when the {@link Iterator#next} method of the underlying iterator is called, you should avoid using this wrapper.
 * <p>
 * <b>Note:</b> For N = 1, you should consider using {@link PeekIterator} instead.
 * <p>
 * Created on 20/11/2014
 *
 * @author Raffaele Canale (raffaelecanale@gmail.com)
 * @version 1.0
 */
public class NPeekIterator<Type> implements Iterator<Type> {

    private final Iterator<Type> it;
    private final LinkedList<Type> buffer;
    private final int maximumSize;

    /**
     * Initialize a new peek iterator.
     *
     * @param it          Underlying iterator
     * @param maximumSize Maximum number of elements to peek
     */
    public NPeekIterator(Iterator<Type> it, int maximumSize) {
        this.it = it;
        this.buffer = new LinkedList<>();
        this.maximumSize = maximumSize;

        fillBuffer();
    }

    @Override
    public boolean hasNext() {
        return hasNext(0);
    }

    /**
     * Returns {@code true} if this iterator as at least (index+1) more elements.
     *
     * @param index Index (starting from next element) to test
     *
     * @return {@code true} if an element is available at index
     */
    public boolean hasNext(int index) {
        return index < buffer.size();
    }

    /**
     * Peek at the next value. The value returned by this method is the same as the value that will be returned by the
     * next call to {@link #next()}. This is equivalent to calling peek(0).
     * <p>
     * If this iterator does not have a next element, a {@code NoSuchElementException} is thrown.
     * <p>
     * Calling this method does not affect the state of this iterator.
     *
     * @return The next element
     *
     * @throws NoSuchElementException if the iteration has no more elements
     */
    public Type peek() {
        return peek(0);
    }

    /**
     * Peek at the value at the given index. The next element is the first element (ie. index is 0).
     * <p>
     * If this iterator does not have an next element at index, a {@code NoSuchElementException} is thrown.
     * <p>
     * Calling this method does not affect the state of this iterator.
     * <p>
     * <p>
     * <b>Note:</b> The index must be lesser than N.
     *
     * @return The next element or {@code null} if there is none
     *
     * @throws NoSuchElementException if the iteration has no more elements
     */
    public Type peek(int index) {
        if (index >= buffer.size()) {
            throw new IllegalArgumentException("Index must be less than N");
        }

        if (!hasNext(index)) {
            throw new NoSuchElementException();
        }
        return buffer.get(index);
    }

    @Override
    public Type next() {
        if (!hasNext()) throw new IllegalStateException();

        Type result = buffer.removeFirst();
        fillBuffer();
        return result;
    }

    private void fillBuffer() {
        while (buffer.size() < maximumSize && it.hasNext()) {
            buffer.add(it.next());
        }
    }

}
