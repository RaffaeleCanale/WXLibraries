package com.wx.util.representables.string;

import com.wx.util.representables.TypeCaster;

import java.util.Objects;

import static com.sun.corba.se.spi.activation.IIOP_CLEAR_TEXT.value;

/**
 *
 * @author Raffaele
 */
public class IntRepr implements TypeCaster<String, Integer> {

    @Override
    public String castIn(Integer value) {
        return String.valueOf(Objects.requireNonNull(value));
    }

    @Override
    public Integer castOut(String value) {
        try{
            return Integer.parseInt(Objects.requireNonNull(value));
        } catch(NumberFormatException ex) {
            throw new ClassCastException("Couldn't cast to int: " + value);
        }
    }
}
