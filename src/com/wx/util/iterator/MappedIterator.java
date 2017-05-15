package com.wx.util.iterator;

import java.util.Iterator;
import java.util.function.Function;

/**
 * @author Raffaele Canale (<a href="mailto:raffaelecanale@gmail.com?subject=WXLibraries">raffaelecanale@gmail.com</a>)
 * @version 0.1 - created on 15.05.17.
 */
public class MappedIterator<E,F> implements Iterator<F> {

    private final Iterator<E> it;
    private final Function<E, F> mapper;

    public MappedIterator(Iterator<E> it, Function<E, F> mapper) {
        this.it = it;
        this.mapper = mapper;
    }

    @Override
    public boolean hasNext() {
        return it.hasNext();
    }

    @Override
    public F next() {
        return mapper.apply(it.next());
    }

    @Override
    public void remove() {
        it.remove();
    }
}
