/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wx.properties.structures.list;

import com.wx.properties.structures.view.StructureView;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @param <E> 
 * @author Raffaele Canale - raffaelecanale@gmail.com
 * @version 0.1
 *
 * Project: WXLibraries File: ListResource.java (UTF-8) Date: May 14, 2013
 */
public abstract class ListResource<E> extends AbstractList<E> {

    final StructureView view;
    
    ListResource(StructureView view) {
        this.view = view;
    }

    public List<E> removeStructure() {
        List<E> copy = new ArrayList<>(this);

        view.removeHeader();
        view.clear();

        return copy;
    }

        
}
