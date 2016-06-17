package com.wx.action.arg;

import java.util.LinkedList;
import java.util.List;

/**
 * A simple supplier that combines several sub-suppliers.
 * <p>
 * Created on 31/03/2015
 *
 * @author Raffaele Canale (raffaelecanale@gmail.com)
 * @version 1.0
 */
public class CombinedSupplier implements ArgumentsSupplier {

    private final LinkedList<ArgumentsSupplier> suppliers;

    /**
     * Creates a new combined supplier. This supplier will supply all the arguments of the sub-suppliers in order.
     *
     * @param suppliers List of suppliers to combine.
     */
    public CombinedSupplier(List<ArgumentsSupplier> suppliers) {
        this.suppliers = new LinkedList<>(suppliers);
    }

    private ArgumentsSupplier firstNonEmptySupplier() {
        while (!suppliers.isEmpty()) {
            ArgumentsSupplier supplier = suppliers.getFirst();
            if (supplier.hasMore()) {
                return supplier;
            }

            suppliers.removeFirst();
        }

        return null;
    }

    private ArgumentsSupplier current() {
        ArgumentsSupplier supplier = firstNonEmptySupplier();
        if (supplier == null) {
            throw new IllegalStateException("No more suppliers/arguments available");
        }

        return supplier;
    }

    @Override
    public String supplyString() {
        return current().supplyString();
    }

    @Override
    public List<String> supplyStringList() {
        return current().supplyStringList();
    }

    @Override
    public Boolean supplyBoolean() {
        return current().supplyBoolean();
    }

    @Override
    public Double supplyDouble() {
        return current().supplyDouble();
    }

    @Override
    public Integer supplyInteger() {
        return current().supplyInteger();
    }

    @Override
    public List<Integer> supplyIntegerList() {
        return current().supplyIntegerList();
    }

    @Override
    public <T> List<T> supplyList(Class<T> c) {
        return current().supplyList(c);
    }

    @Override
    public <T> T supply(Class<T> c) {
        return current().supply(c);
    }

    @Override
    public boolean hasMore() {
        return firstNonEmptySupplier() != null;
    }
}
