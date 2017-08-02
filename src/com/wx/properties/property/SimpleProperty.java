/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.wx.properties.property;

import java.util.Objects;
import java.util.Optional;

/**
 *
 * @author Raffaele Canale - raffaelecanale@gmail.com
 * @version 0.5
 *
 * Project: WXLibraries.ANT
 * File: SimpleProperty.java (UTF-8)
 * Date: Oct 16, 2013 
 */
public class SimpleProperty<Type> extends Property<Type> {
    
    private Type value;


    public SimpleProperty(Type value) {
        this.value = value;
    }

    public SimpleProperty() {
        this(null);
    }
    
    @Override
    public void set(Type value) {
        this.value = Objects.requireNonNull(value);
    }

    @Override
    public Type get() {
        return value;
    }

    @Override
    public boolean exists() {
        return value != null;
    }

    @Override
    public Type clear() {
        Type oldValue = this.value;
        this.value = null;

        return oldValue;
    }
}
