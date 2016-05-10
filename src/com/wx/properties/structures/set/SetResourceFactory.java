package com.wx.properties.structures.set;

import com.wx.properties.structures.view.StructureView;
import com.wx.util.representables.TypeCaster;

import java.util.Set;

/**
 * Created by canale on 10.05.16.
 */
public interface SetResourceFactory {

    <E> SetResource<E> loadSet(StructureView view, TypeCaster<String, E> caster, Object... args);

    <E> SetResource<E> createSet(StructureView view, TypeCaster<String, E> caster, Set<E> otherSet, Object... args);

    static String getString(Object[] args, int index, String def) {
        if (args == null || index >= args.length) {
            return def;
        }

        return (String) args[index];
    }

}
