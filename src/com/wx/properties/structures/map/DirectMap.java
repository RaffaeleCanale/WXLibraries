package com.wx.properties.structures.map;

import com.wx.properties.structures.view.StructureView;
import com.wx.util.representables.TypeCaster;

import java.util.*;

/**
 * Created by canale on 04.05.16.
 */
public class DirectMap<K, V> extends MapResource<K, V> {

    public static class Factory implements MapResourceFactory {

        @Override
        public <K, V> MapResource<K, V> loadMap(StructureView view, TypeCaster<String, K> kCaster, TypeCaster<String, V> vCaster, Object... args) {
            String header = view.getHeader();
            DirectMap<K, V> newMap = new DirectMap<>(view, kCaster, vCaster);

            if (header == null) {
                return null;
            }

            return newMap;
        }

        @Override
        public <K, V> MapResource<K, V> createMap(StructureView view, TypeCaster<String, K> kCaster, TypeCaster<String, V> vCaster, Map<K, V> otherMap, Object... args) {
            Objects.requireNonNull(otherMap);
            DirectMap<K, V> newMap = new DirectMap<>(view, kCaster, vCaster);
            view.clear();
            newMap.putAll(otherMap);
            view.setHeader(DirectMap.class.getCanonicalName());

            return newMap;
        }
    }

    public static MapResourceFactory factory() {
        return new Factory();
    }

    private final TypeCaster<String, K> keyCaster;
    private final TypeCaster<String, V> valueCaster;

    transient volatile Set<Entry<K,V>> entrySet;

    private DirectMap(StructureView view, TypeCaster<String, K> kCaster, TypeCaster<String, V> vCaster) {
        super(view);
        this.keyCaster = Objects.requireNonNull(kCaster);
        this.valueCaster = Objects.requireNonNull(vCaster);
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        if (entrySet == null) {
            entrySet = new AbstractSet<Entry<K, V>>() {
                @Override
                public Iterator<Entry<K, V>> iterator() {
                    return new Iterator<Entry<K, V>>() {

                        final Iterator<Entry<String, String>> it = view.entrySet().iterator();

                        @Override
                        public boolean hasNext() {
                            return it.hasNext();
                        }

                        @Override
                        public Entry<K, V> next() {
                            return new DirectEntry(it.next());
                        }

                        @Override
                        public void remove() {
                            it.remove();
                        }
                    };
                }

                @Override
                public int size() {
                    return DirectMap.this.size();
                }
            };
        }

        return entrySet;
    }

    @Override
    public int size() {
        return view.size();
    }

    @Override
    public boolean containsValue(Object value) {
        return view.containsValue(valueCaster.castIn((V) value));
    }

    @Override
    public boolean containsKey(Object key) {
        return view.containsKey(keyCaster.castIn((K) key));
    }

    @Override
    public V get(Object key) {
        return safeCast(view.get(keyCaster.castIn((K) key)));
    }

    @Override
    public V put(K key, V value) {
        return safeCast(view.put(keyCaster.castIn(key), valueCaster.castIn(value)));
    }

    @Override
    public V remove(Object key) {
        return safeCast(view.remove(keyCaster.castIn((K) key)));
    }

    @Override
    public void clear() {
        view.clear();
    }

    private V safeCast(String value) {
        return value == null ? null : valueCaster.castOut(value);
    }

    private class DirectEntry implements Entry<K,V> {

        private final Entry<String, String> viewEntry;

        public DirectEntry(Entry<String, String> viewEntry) {
            this.viewEntry = viewEntry;
        }

        @Override
        public K getKey() {
            return keyCaster.castOut(viewEntry.getKey());
        }

        @Override
        public V getValue() {
            return valueCaster.castOut(viewEntry.getValue());
        }

        @Override
        public V setValue(V value) {
            return safeCast(viewEntry.setValue(valueCaster.castIn(value)));
        }
    }
}
