package com.wx.properties.property;

import java.util.Optional;



/**
 * Simple and generic interface for a property that can be read or set.
 * <p>
 * <p>
 * Note: It has been decided that this would not be an interface in order to let space to improvements in the future,
 * such as change listeners, invalidation listeners, property bindings and more.
 * <p>
 * Reviewed: 7/11/14
 *
 * @param <Type> Type of this property
 *
 * @author Raffaele Canale - raffaelecanale@gmail.com
 * @version 1.0
 */
public abstract class Property<Type> {


    public abstract void set(Type value);

    public abstract Optional<Type> get();

    public abstract Optional<Type> clear();

}
