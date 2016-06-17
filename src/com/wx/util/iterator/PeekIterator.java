package com.wx.util.iterator;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * This class enhances any iterator by offering the ability to peek at the next value (ie. to access the next value
 * without calling next or changing the state of the iterator).
 * <p>
 * <b>Note:</b> The implementation may call the {@link Iterator#next()} method arbitrarily, if you need a clear control
 * on when the {@link Iterator#next} method of the underlying iterator is called, you should avoid using this wrapper.
 *
 * @author Raffaele Canale (raffaelecanale@gmail.com)
 * @version 1.0
 */
public class PeekIterator<Type> implements Iterator<Type> {

    private final Iterator<Type> it;
    private Type next;
    private boolean endReached;

    /**
     * Initialize a new peek iterator.
     *
     * @param it Underlying iterator
     */
    public PeekIterator(Iterator<Type> it) {
        this.it = it;
        doNext();
    }

    @Override
    public boolean hasNext() {
        return !endReached;
    }

    /**
     * Peek at the next value. The value returned by this method is the same as the value that will be returned by the
     * next call to {@link #next()}.
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
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        return next;
    }

    @Override
    public Type next() {
        Type result = next;
        doNext();
        return result;
    }

    private void doNext() {
        if (it.hasNext()) {
            next = it.next();
        } else {
            endReached = true;
            next = null;
        }
    }
}
