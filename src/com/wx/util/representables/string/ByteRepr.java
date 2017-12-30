package com.wx.util.representables.string;

import com.wx.util.representables.TypeCaster;

import java.util.Objects;

/**
 *
 * @author Raffaele
 */
public class ByteRepr implements TypeCaster<String, Byte> {

    @Override
    public String castIn(Byte value) {
        return String.valueOf(Objects.requireNonNull(value));
    }

    @Override
    public Byte castOut(String value) {
        try{
            return Byte.parseByte(Objects.requireNonNull(value));
        } catch(NumberFormatException ex) {
            throw new ClassCastException("Couldn't cast to int: " + value);
        }
    }
}
