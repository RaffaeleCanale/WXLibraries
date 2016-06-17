package com.wx.util.condition;


/**
 *
 * @author Raffaele Canale (raffaelecanale@gmail.com)
 * @version 0.1
 */
@FunctionalInterface
public interface Condition<Type> {

    public void check(Type value);

}
