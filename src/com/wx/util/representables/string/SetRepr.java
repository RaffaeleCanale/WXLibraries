/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wx.util.representables.string;

import com.wx.util.representables.TypeCaster;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

/**
 *
 * @author Raffaele
 */
public class SetRepr<E> implements TypeCaster<String, Set<E>> {

    private final ListRepr<E> listCaster;
    
    /**
     * Creates a new SetRepr
     * @param subRepresentable Representable for the elements of the list
     * @param separator Separator of the elements
     */
    public SetRepr(TypeCaster<String, E> subRepresentable, String separator) {
        this.listCaster = new ListRepr<>(subRepresentable, separator);
    }

    @Override
    public String castIn(Set<E> set) throws ClassCastException {
        return listCaster.castIn(new LinkedList<>(set));
    }

    @Override
    public Set<E> castOut(String value) throws ClassCastException {
        return new HashSet<>(listCaster.castOut(value));
    }
    
}
