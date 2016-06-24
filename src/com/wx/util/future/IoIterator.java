package com.wx.util.future;

import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;

import static com.wx.util.future.Future.*;

/**
 * Iterator enhanced with that the {@link #next()} method may throw an IoException
 *
 * @param <E> Type of the iterator
 */
public interface IoIterator<E> {

    static <E> IoIterator<E> from(Iterator<E> it) {
        return new IoIterator<E>() {
            @Override
            public E next() {
                return it.next();
            }

            @Override
            public boolean hasNext() {
                return it.hasNext();
            }

            @Override
            public void remove() {
                it.remove();
            }

            @Override
            public void forEachRemaining(Consumer<? super E> action) {
                it.forEachRemaining(action);
            }
        };
    }

    E next() throws IOException;

    boolean hasNext();

    default void remove() throws IOException {
        throw new UnsupportedOperationException("remove");
    }

    default void forEachRemaining(Consumer<? super E> action) throws IOException {
        Objects.requireNonNull(action);
        while (hasNext())
            action.accept(next());
    }

    /**
     * Wraps this iterator into a conventional {@link Iterator} by wrapping every call of {@link #next()}
     * into a {@link Future} object.
     * @return An {@link Iterator} of {@link Future} wrapping this iterator
     */
    default Iterator<Future<E>> iterator() {
        return new Iterator<Future<E>>() {
            @Override
            public boolean hasNext() {
                return IoIterator.this.hasNext();
            }

            @Override
            public Future<E> next() {
                return future(IoIterator.this::next);
            }
        };
    }

}
