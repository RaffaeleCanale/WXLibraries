/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.wx.util.representables.string;

import com.wx.util.pair.Pair;
import com.wx.util.representables.TypeCaster;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

/**
 *
 * @author Raffaele Canale - raffaelecanale@gmail.com
 * @version 0.1
 *
 * Project: WXLibraries
 * File: MapRepr.java (UTF-8)
 * Date: 28 déc. 2012 
 */
public class MapRepr<K, V> implements TypeCaster<String, Map<K, V>> {


    public static MapRepr<String, String> stringRepr(String keyValueSeparator, String elementsSeparator) {
        StringRepr repr = new StringRepr();
        return new MapRepr<>(keyValueSeparator, elementsSeparator, repr, repr);
    }

    private final SetRepr<Entry<K, V>> entryCaster;

    public MapRepr(String keyValueSeparator, String elementsSeparator,
                                          TypeCaster<String, K> keyRepresentable, 
                                          TypeCaster<String, V> valueRepresentable) {
        if (elementsSeparator != null && keyValueSeparator != null &&
                (elementsSeparator.contains(keyValueSeparator) || keyValueSeparator.contains(elementsSeparator))) {
            throw new IllegalArgumentException("Separators must be different");
        }

        this.entryCaster = new SetRepr<>(
                new EntryReprAdapter(keyValueSeparator, keyRepresentable, valueRepresentable),
                elementsSeparator);
    }
            
    @Override
    public String castIn(Map<K, V> map) throws ClassCastException {
        return entryCaster.castIn(map.entrySet());
    }

    @Override
    public Map<K, V> castOut(String value) throws ClassCastException {
        return entryCaster.castOut(value).stream()
                .collect(Collectors.toMap(Entry::getKey, Entry::getValue));
    }
    
    private class EntryReprAdapter implements TypeCaster<String, Map.Entry<K, V>> {

        private PairRepr<K, V> mPairRepr;
        
        private EntryReprAdapter(String separator,
                TypeCaster<String, K> reprE,
                TypeCaster<String, V> reprF) {
            mPairRepr = new PairRepr<>(reprE, reprF, separator);
        }

        @Override
        public String castIn(Entry<K, V> entry) throws ClassCastException {
            Pair<K, V> pair = new Pair<>(entry.getKey(), entry.getValue());
            return mPairRepr.castIn(pair);
        }

        @Override
        public Entry<K, V> castOut(String value) throws ClassCastException {
            Pair<K, V> pair = mPairRepr.castOut(value);
            return new AbstractMap.SimpleEntry<>(pair.get1(), pair.get2());
        }
        
    }

}
