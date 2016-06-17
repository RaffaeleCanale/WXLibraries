package com.wx.io.table.search;

/**
 * A simple implementation of a {@link Condition} that will try to match only a
 * specific value from the value.
 *
 * @author Raffaele Canale - raffaelecanale@gmail.com
 * @version 0.9
 *
 * Project: Worx.ANT File: FieldMatch.java (UTF-8) Date: Jul 12, 2013
 */
public class FieldMatch implements Condition {

    private final Object searchObject;
    private final int fieldIndex;

    /**
     * Constructs a new field match.
     *
     * @param searchObject Object that needs to be matched
     * @param columnIndex   Column index of that value
     */
    public FieldMatch(Object searchObject, int columnIndex) {
        this.searchObject = searchObject;
        this.fieldIndex = columnIndex;
    }

    @Override
    public boolean matches(Object[] register) {
        if (searchObject == null) {
            return register[fieldIndex] == null;
        }
        
        return searchObject.equals(register[fieldIndex]);
    }

}
