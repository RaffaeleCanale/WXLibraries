package com.wx.properties.structures.set;

import com.wx.properties.structures.view.StructureView;

import java.util.AbstractSet;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Raffaele Canale - raffaelecanale@gmail.com
 * @version 0.1
 *
 * Project: WXLibraries File: SetResource.java (UTF-8) Date: May 17, 2013
 */
public abstract class SetResource<E> extends AbstractSet<E> {
    
    final StructureView view;

    SetResource(StructureView view) {
        this.view = view;
    }

    public Set<E> removeStructure() {
        Set<E> copy = new HashSet<>(this);

        view.removeHeader();
        view.clear();

        return copy;
    }

}
