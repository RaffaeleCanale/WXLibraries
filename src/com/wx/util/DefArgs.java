package com.wx.util;

import java.util.Optional;
import java.util.function.Function;

/**
 * Simple wrapper for an array of arguments.
 * <p>
 * It provides convenient methods to access the arguments without having to check the array size every time.
 * <p>
 * Created by canale on 10.05.16.
 */
public class DefArgs<T> {

    private final T[] args;

    /**
     * Initialize wrapper around the given args.
     *
     * @param args Arguments to wrap
     */
    public DefArgs(T[] args) {
        this.args = args;
    }

    /**
     * Get the argument at the given index or returns the default value there is no such argument.
     * <p>
     * This will return the default value if the index is bigger (or equal) than the number of arguments or if the
     * argument at that index is null.
     *
     * @param index Index whose argument to get
     * @param def   Default value to return if the argument is missing
     *
     * @return The argument at the given index or def if no such argument exist
     */
    public T arg(int index, T def) {
        return arg(index).orElse(def);
    }

    /**
     * Get and apply some mapping to the argument at the given index or returns the default value there is no such
     * argument.
     * <p>
     * This will return the default value if the index is bigger (or equal) than the number of arguments or if the
     * argument at that index is null.
     *
     * @param index   Index whose argument to get
     * @param def     Default value to return if the argument is missing
     * @param mapping Mapping to apply to the argument if it exists
     *
     * @return The mapped argument at the given index or def if no such argument exist
     */
    public <E> E arg(int index, E def, Function<T, E> mapping) {
        return arg(index).map(mapping).orElse(def);
    }

    private Optional<T> arg(int index) {
        if (index >= args.length) {
            return Optional.empty();
        }

        return Optional.ofNullable(args[index]);
    }
}
