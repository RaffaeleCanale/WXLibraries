package com.wx.util.future;

import java.io.IOException;

/**
 * This interface is very similar to the {@link java.util.function.Supplier}, except that the
 * get method may throw an exception.
 */
@FunctionalInterface
public interface IoSupplier<T> {

    /**
     * Gets a result.
     *
     * @return a result
     */
    T get() throws IOException;
}
