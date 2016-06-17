package com.wx.util.future;

import java.io.IOException;
import java.util.Objects;


/**
 * This class emulates an {@link IoSupplier} that provides a single element.
 * <p>
 * This wrapper can be set with an actual value or with an exception.
 * <p>
 * Created by canale on 11/1/14.
 */
public class Future<E> implements IoSupplier<E> {

    /**
     * This method uses the given supplier to create a {@code Future} object.
     * <p>
     * This will call the given supplier, store its result (ie. a value or an exception) in a {@code Future} object that
     * will yield the same behaviour as the supplier when the {@link #get()} method is called.
     *
     * @param supplier Supplier of the value
     * @param <E>      Type of the value
     *
     * @return A {@code Future} object that will return the same result as the supplier
     */
    public static <E> Future<E> future(IoSupplier<E> supplier) {
        try {
            return new Future<>(supplier.get());
        } catch (IOException e) {
            return new Future<>(e);
        }
    }

    private final E element;
    private final IOException exception;

    /**
     * Build this wrapper with the given element.
     *
     * @param element Element contained in this wrapper
     */
    public Future(E element) {
        this.element = element;
        this.exception = null;
    }

    /**
     * Build this wrapper such that it throws an exception when {@link #get()} will be invoked.
     *
     * @param exception Exception to throw
     */
    public Future(IOException exception) {
        this.element = null;
        this.exception = Objects.requireNonNull(exception);
    }

    /**
     * Get the element contained in this wrapper or throws the associated {@code IOException}.
     * <p>
     * Note that this wrapper only contains one element, thus, any call of this method will have the same result.
     *
     * @return The element contained in this wrapper
     *
     * @throws IOException
     */
    public E get() throws IOException {
        if (exception != null) {
            throw exception;
        }

        return element;
    }
}
