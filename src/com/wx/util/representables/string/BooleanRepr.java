package com.wx.util.representables.string;

import com.wx.util.representables.TypeCaster;

import static java.util.Objects.requireNonNull;

/**
 *
 * Allows to cast a boolean into {@code String} and vice-versa.
 *
 * {@code null} values are not supported. To enforce the usage
 *
 * @author Raffaele Canale
 *
 */
public class BooleanRepr implements TypeCaster<String, Boolean> {

    /**
     * {@inheritDoc}
     */
    @Override
    public String castIn(Boolean value) {
        return requireNonNull(value).toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean castOut(String value) {
        return Boolean.valueOf(requireNonNull(value));
    }
}
