/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.wx.util.pair;

/**
 *
 * @author Raffaele Canale - raffaelecanale@gmail.com
 * @version 0.1
 *
 * Project: WXLibraries.ANT
 * File: ImmutablePair.java (UTF-8)
 * Date: Oct 8, 2013 
 */
public class ImmutablePair<E, F> extends Pair<E, F> {

    public ImmutablePair(E e, F f) {
        super(e, f);
    }

    @Override
    public void set1(E e) {
        throw new UnsupportedOperationException("Can't modify an immutable pair");
    }
    
    @Override
    public void set2(F f) {
        throw new UnsupportedOperationException("Can't modify an immutable pair");
    }
}
