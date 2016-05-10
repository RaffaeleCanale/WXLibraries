package com.wx.properties.structures.list;

import com.wx.properties.structures.view.StructureView;
import com.wx.util.representables.TypeCaster;

import java.util.List;

/**
 * Created by canale on 09.05.16.
 */
public interface ListResourceFactory {

    <E> ListResource<E> loadList(StructureView view, TypeCaster<String, E> caster, Object... args);

    <E> ListResource<E> createList(StructureView view, TypeCaster<String, E> caster, List<E> otherList, Object... args);

}
