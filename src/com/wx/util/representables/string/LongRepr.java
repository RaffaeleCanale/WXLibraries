/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wx.util.representables.string;

import com.wx.util.representables.TypeCaster;

import java.util.Objects;

/**
 *
 * @author Raffaele
 */
public class LongRepr implements TypeCaster<String, Long> {

    @Override
    public String castIn(Long value) {
        return String.valueOf(Objects.requireNonNull(value));
    }

    @Override
    public Long castOut(String value) {
        try {
            return Long.parseLong(Objects.requireNonNull(value));
        } catch (NumberFormatException ex) {
            throw new ClassCastException("Couldn't cast to long: " + value
                    + "\n" + ex);
        }
    }
}
