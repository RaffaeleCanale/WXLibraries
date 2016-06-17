package com.wx.io.table.search;

/**
 * This is a simple interface representing a condition upon a register.
 * <br><br>
 * This interface is mainly use to perform queries on register tables.
 * 
 * @author Raffaele Canale - raffaelecanale@gmail.com
 * @version 1.0
 *
 * Project: Worx.ANT
 * File: Condition.java (UTF-8)
 * Date: Jul 12, 2013 
 */
public interface Condition {
    public abstract boolean matches(Object[] register);
}
