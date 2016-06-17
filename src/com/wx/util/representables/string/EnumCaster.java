/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.wx.util.representables.string;

import com.wx.util.representables.TypeCaster;

import java.util.Objects;

/**
 *
 * Allows to cast enum values to string by using their name
 *
 * @author Raffaele Canale - raffaelecanale@gmail.com
 * @version 0.1
 *
 * Project: WXLibraries.ANT
 * File: EnumCaster.java (UTF-8)
 * Date: Jul 4, 2013 
 */
public class EnumCaster<E extends Enum<E>> implements TypeCaster<String, E> {

//    private final E[] values;
    private final Class<E> enumClass;

    public EnumCaster(Class<E> enumClass) {
        this.enumClass = Objects.requireNonNull(enumClass);
    }

    @Deprecated
    public EnumCaster(E[] values) {
        Objects.requireNonNull(values);
        if (values.length == 0) {
            throw new IllegalArgumentException("Must have at least 1 enum");
        }

        enumClass = values[0].getDeclaringClass();  // Ugly, but for legacy purposes
    }
    
    @Override
    public String castIn(E value) throws ClassCastException {
        return value.name();
    }

    @Override
    public E castOut(String value) throws ClassCastException {
        try {
            return Enum.valueOf(enumClass, value);
        } catch (IllegalArgumentException e) {
            throw new ClassCastException(e.getMessage());
        }
    }

}
