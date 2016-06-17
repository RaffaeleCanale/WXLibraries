package com.wx.action.arg;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * A simple supplier based on an object array. If the supply method called does not correspond to the argument type, a
 * {@code ClassCastException} is thrown.
 * <p>
 * Created on 19/11/2014
 *
 * @author Raffaele Canale (raffaelecanale@gmail.com)
 * @version 1.0
 */
public class ObjectsSupplier implements ArgumentsSupplier {

    private final Object[] arguments;
    private int lastSupplied = -1;

    /**
     * Create a new supplier with the given arguments.
     *
     * @param arguments Arguments of this supplier
     */
    public ObjectsSupplier(Object... arguments) {
        this.arguments = arguments.clone();
    }


    @Override
    public <T> List<T> supplyList(Class<T> c) {
        if (!hasMore()) {
            throw new IllegalStateException("Not enough arguments provided");
        }
        lastSupplied++;

        Object tmp = arguments[lastSupplied];

        LinkedList<T> r = new LinkedList<>();
        List L = List.class.cast(tmp);
        if (L == null) {
            return null;
        }
        for (Object o : L) {
            r.add(c.cast(o));
        }

        return r;
    }

    @Override
    public <T> T supply(Class<T> c) {
        if (!hasMore()) {
            throw new IllegalStateException("Not enough arguments provided");
        }
        lastSupplied++;

        return c.cast(arguments[lastSupplied]);
    }

    @Override
    public boolean hasMore() {
        return lastSupplied + 1 < arguments.length;
    }


    @Override
    public String toString() {
        return Arrays.toString(arguments);
    }
}
