package com.wx.io.table.search;

/**
 * A trivial implementation of {@link Condition} that will always return true.
 * 
 * @author Raffaele Canale - raffaelecanale@gmail.com
 * @version 0.1
 *
 * Project: Worx.ANT
 * File: AlwaysTrueCondition.java (UTF-8)
 * Date: Jul 14, 2013 
 */
public class AlwaysTrueCondition implements Condition {

    @Override
    public boolean matches(Object[] register) {
        return true;
    }

}
