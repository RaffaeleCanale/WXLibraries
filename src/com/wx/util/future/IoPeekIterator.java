package com.wx.util.future;

import com.wx.util.future.Future;
import com.wx.util.future.IoIterator;

import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * This class enhances any IO iterator by offering the ability to peek at the next value (ie. to access the next value
 * without calling next or changing the state of the iterator).
 * <p>
 * <b>Note:</b> The implementation may call the {@link Iterator#next()} method arbitrarily, if you need a clear control
 * on when the {@link Iterator#next} method of the underlying iterator is called, you should avoid using this wrapper.
 *
 * @author Raffaele Canale (raffaelecanale@gmail.com)
 * @version 1.0
 */
public class IoPeekIterator<Type> implements IoIterator<Type> {

    private final Iterator<Future<Type>> it;
    private Future<Type> next;
    private boolean endReached;

    /**
     * Initialize a new peek iterator.
     *
     * @param it Underlying iterator
     */
    public IoPeekIterator(IoIterator<Type> it) {
        this.it = it.iterator();
        doNext();
    }

    @Override
    public boolean hasNext() {
        return !endReached;
    }

    public Future<Type> peek() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        return next;
    }

    @Override
    public Type next() throws IOException {
        Future<Type> result = next;
        doNext();

        return result.get();
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
