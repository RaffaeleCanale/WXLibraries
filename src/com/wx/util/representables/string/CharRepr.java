package com.wx.util.representables.string;

import com.wx.util.representables.TypeCaster;

import java.util.Objects;

/**
 * Casts a {@code String} into {@code char} and vice-versa.
 * <br>
 * {@code null} elements will throw exceptions
 * @author Raffaele Canale
 */
public class CharRepr implements TypeCaster<String, Character> {

    /**
     * {@inheritDoc}
     */
    @Override
    public String castIn(Character value) {
        return String.valueOf(Objects.requireNonNull(value));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Character castOut(String value) {
        if(value.length() != 1) {
            throw new ClassCastException("The input string must be of length 1: '" + value + "'");
        }

        return value.charAt(0);
    }
    
}
