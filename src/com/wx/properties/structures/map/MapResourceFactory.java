package com.wx.properties.structures.map;

import com.wx.properties.structures.view.StructureView;
import com.wx.util.representables.TypeCaster;

import java.util.Map;

/**
 * Created by canale on 10.05.16.
 */
public interface MapResourceFactory {

    <K,V> MapResource<K,V> loadMap(StructureView view, TypeCaster<String, K> kCaster, TypeCaster<String, V> vCaster, Object... args);

    <K,V> MapResource<K,V> createMap(StructureView view, TypeCaster<String, K> kCaster, TypeCaster<String, V> vCaster, Map<K,V> otherMap, Object... args);
    
}
