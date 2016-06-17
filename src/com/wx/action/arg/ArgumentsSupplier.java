package com.wx.action.arg;


import java.util.Arrays;
import java.util.List;

/**
 * Interface that represents an iterator of arguments. An object of this instance contains an ordered list of arguments.
 * These arguments can be iterated using the right method according to the type of the next argument. The behaviour of
 * calling the method corresponding to the wrong type depends on the implementation.
 * <p>
 * Created on 19/11/2014
 *
 * @author Raffaele Canale (raffaelecanale@gmail.com)
 * @version 1.0
 */
public interface ArgumentsSupplier {

    /**
     * Concatenates several suppliers into a single one.
     *
     * @param suppliers Suppliers to combine
     *
     * @return A combined suppliers that will supply all the arguments of all the suppliers in order
     */
    static ArgumentsSupplier combine(ArgumentsSupplier... suppliers) {
        return new CombinedSupplier(Arrays.asList(suppliers));
    }

    /**
     * Supply an argument of type string.
     *
     * @return A string argument
     */
    default String supplyString() {
        return supply(String.class);
    }

    /**
     * Supply an argument of type list of string.
     *
     * @return A list of string argument
     */
    default List<String> supplyStringList() {
        return supplyList(String.class);
    }

    /**
     * Supply an argument of type boolean.
     *
     * @return A boolean argument
     */
    default Boolean supplyBoolean() {
        return supply(Boolean.class);
    }

    /**
     * Supply an argument of type double.
     *
     * @return A double argument
     */
    default Double supplyDouble() {
        return supply(Double.class);
    }

    /**
     * Supply an argument of type integer.
     *
     * @return A integer argument
     */
    default Integer supplyInteger() {
        return supply(Integer.class);
    }

    /**
     * Supply an argument of type list of integer.
     *
     * @return A list of integer argument
     */
    default List<Integer> supplyIntegerList() {
        return supplyList(Integer.class);
    }

    /**
     * Supply an argument of type list.
     *
     * @param c   Class of the list elements
     * @param <T> Type of the list elements
     *
     * @return A list of the given type
     */
    <T> List<T> supplyList(Class<T> c);

    /**
     * Supply an argument of the given type.
     * <p>
     * Note: Depending on the implementation, a wrong type might throw an exception.
     *
     * @param c Class of the argument
     * @param <T> Type of the argument
     *
     * @return The argument of the given type
     */
    <T> T supply(Class<T> c);

    /**
     * Returns {@code true} if this supplier has more arguments.
     * @return {@code true} if this supplier has more arguments
     */
    boolean hasMore();


}
