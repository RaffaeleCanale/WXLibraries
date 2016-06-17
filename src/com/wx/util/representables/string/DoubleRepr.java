package com.wx.util.representables.string;

import com.wx.util.representables.TypeCaster;

import java.util.Objects;

/**
 *
 * @author Raffaele
 */
public class DoubleRepr implements TypeCaster<String, Double> {
    
    @Override
    public String castIn(Double value) {
        return String.valueOf(Objects.requireNonNull(value));
    }

    @Override
    public Double castOut(String value) {
        try{
            return Double.parseDouble(Objects.requireNonNull(value));
        } catch(NumberFormatException ex) {
            throw new ClassCastException("Couldn't cast to double: " + value);
        }
    }
}
