package com.wx.util.iterator;

import java.util.Iterator;
import java.util.function.Predicate;

/**
 * @author Raffaele Canale (<a href="mailto:raffaelecanale@gmail.com?subject=WXLibraries">raffaelecanale@gmail.com</a>)
 * @version 0.1 - created on 15.05.17.
 */
public class FilteredIterator<T> implements Iterator<T> {

    private final PeekIterator<T> it;
    private final Predicate<T> predicate;

    public FilteredIterator(Iterator<T> it, Predicate<T> predicate) {
        this.it = new PeekIterator<>(it);
        this.predicate = predicate;

        moveIterator();
    }

    @Override
    public boolean hasNext() {
        return it.hasNext();
    }

    @Override
    public T next() {
        T result = it.next();
        moveIterator();

        return result;
    }

    private void moveIterator() {
        while (hasNext() && !predicate.test(it.peek())) {
            it.next();
        }
    }
}
