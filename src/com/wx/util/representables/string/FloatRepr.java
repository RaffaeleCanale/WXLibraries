package com.wx.util.representables.string;

import com.wx.util.representables.TypeCaster;

import java.util.Objects;

/**
 *
 * @author Raffaele
 */
public class FloatRepr implements TypeCaster<String, Float> {

    @Override
    public String castIn(Float value) {
        return String.valueOf(Objects.requireNonNull(value));
    }

    @Override
    public Float castOut(String value) {
        try{
            return Float.parseFloat(Objects.requireNonNull(value));
        } catch(NumberFormatException ex) {
            throw new ClassCastException("Couldn't cast to double: " + value);
        }
    }
}
