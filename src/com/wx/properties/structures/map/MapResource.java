/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.wx.properties.structures.map;

import com.wx.properties.structures.view.StructureView;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Raffaele Canale - raffaelecanale@gmail.com
 * @version 0.1
 *
 * Project: WXLibraries
 * File: MapResource.java (UTF-8)
 * Date: May 14, 2013 
 */
public abstract class MapResource<K, V> extends AbstractMap<K, V> {
    
    final StructureView view;

    public MapResource(StructureView view) {
        this.view = view;
    }

    public Map<K,V> removeStructure() {
        Map<K, V> copy = new HashMap<>(this);

        view.removeHeader();
        view.clear();

        return copy;
    }


}
