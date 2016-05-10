/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wx.properties.structures.map;

import com.wx.properties.structures.view.StructureView;
import com.wx.util.Args;
import com.wx.util.representables.TypeCaster;
import com.wx.util.representables.string.IntRepr;
import com.wx.util.representables.string.PairRepr;

import java.util.*;
import java.util.stream.Stream;

/**
 * @author Raffaele
 */
public class RefHashMap<K, V> extends MapResource<K, V> {

    public static class Factory implements MapResourceFactory {

        @Override
        public <K, V> MapResource<K, V> loadMap(StructureView view, TypeCaster<String, K> kCaster, TypeCaster<String, V> vCaster, Object... args) {
            String header = view.getHeader();
            String separator = new Args<>(args).arg(0, null, String::valueOf);

            RefHashMap<K, V> newMap = new RefHashMap<>(view, separator, kCaster,
                    vCaster);

            if (header == null) {
                return null;
            }
            newMap.size = (new IntRepr()).castOut(header);

            return newMap;
        }

        @Override
        public <K, V> MapResource<K, V> createMap(StructureView view, TypeCaster<String, K> kCaster, TypeCaster<String, V> vCaster, Map<K, V> otherMap, Object... args) {
            Objects.requireNonNull(otherMap);
            String separator = new Args<>(args).arg(0, null, String::valueOf);

            RefHashMap<K, V> newMap = new RefHashMap<>(view, separator, kCaster,
                    vCaster);
            view.clear();
            newMap.setSize(0);
            newMap.putAll(otherMap);

            return newMap;
        }
    }

    public static MapResourceFactory factory() {
        return new Factory();
    }

    private final PairRepr<K, V> pairCaster;
    private int size;

    transient volatile Set<Entry<K, V>> entrySet;

    private RefHashMap(StructureView view, String separator,
                       TypeCaster<String, K> kCaster, TypeCaster<String, V> vCaster) {
        super(view);
        pairCaster = new PairRepr<>(kCaster, vCaster, separator);
    }

    private void setSize(int size) {
        this.size = size;
        view.setHeader(String.valueOf(size));
    }

    private void incrementSize(int delta) {
        setSize(size + delta);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean containsKey(Object key) {
        return findEntry(key).isPresent();
    }

    @Override
    public V get(Object key) {
        return findEntry(key).getCurrentValue();
    }

    @Override
    public V put(K key, V value) {
        return findEntry(key).set(key, value);
    }

    @Override
    public V remove(Object key) {
        return findEntry(key).remove();
    }

    @Override
    public void clear() {
        view.clear();
        setSize(0);
    }

    private Stream<String> allHashes() {
        return view.keySet().stream()
                .map(RefHashMap::extractHash)
                .distinct();

    }

    @Override
    public Set<Entry<K, V>> entrySet() {

        if (entrySet == null) {
            entrySet = new AbstractSet<Entry<K, V>>() {
                @Override
                public Iterator<Entry<K, V>> iterator() {
                    if (isEmpty()) {
                        return Collections.emptyIterator();
                    }

                    return new Iterator<Entry<K, V>>() {

                        final Iterator<String> hashes = allHashes().iterator();
                        EntryInfo lastEntry = new EntryInfo(-1, hashes.next());


                        @Override
                        public boolean hasNext() {
                            return hashes.hasNext() || nextEntryIndex().isPresent();
                        }

                        private EntryInfo nextEntryIndex() {
                            return new EntryInfo(lastEntry.index + 1, lastEntry.hash);
                        }

                        @Override
                        public Entry<K, V> next() {
                            lastEntry = nextEntryIndex();

                            if (!lastEntry.isPresent()) {
                                lastEntry = new EntryInfo(0, hashes.next());
                            }

                            return lastEntry.entry;
                        }

                        @Override
                        public void remove() {
                            if (lastEntry.index < 0) {
                                throw new IllegalStateException();
                            }
                            lastEntry.remove();
                        }
                    };
                }

                @Override
                public int size() {
                    return RefHashMap.this.size();
                }

                @Override
                public void clear() {
                    RefHashMap.this.clear();
                }
            };
        }

        return entrySet;
    }


//    private RefEntry getEntry(String hash, int count) {
//        String key = hash + "." + toHexString(count, -1);
//
//        String value = view.get(key);
//        return value == null ? null : entryCaster.castOut(value);
//    }
//
//    private String setEntry(String hash, int count, RefEntry entry) {
//        String key = hash + "." + toHexString(count, -1);
//
//        return view.put(key, entryCaster.castIn(entry));
//    }
//
//    private String removeEntry(String hash, int count) {
//        String key = hash + "." + toHexString(count, -1);
//        return view.remove(key);
//    }

    private EntryInfo findEntry(Object key) {
        int index = 0;
        EntryInfo result;

        do {
            result = new EntryInfo(key, index);
            index++;
        } while (result.isPresent() && !result.keyEquals(key));

        return result;
    }

    private class EntryInfo {

        private RefEntry entry;
        private final String hash;
        private final int index;

        public EntryInfo(Object key, int index) {
            this(index, getHash(key));
        }

        private EntryInfo(int index, String hash) {
            this.hash = hash;
            this.index = index;

            String viewKey = getViewKey(hash, index);
            String value = view.get(viewKey);

            this.entry = value == null ? null : loadEntry(viewKey, value);
        }

        private RefEntry loadEntry(String viewKey, String viewValue) {
            return pairCaster.castOut(viewValue, (k, v) -> new RefEntry(viewKey, k, v));
        }

        public boolean isPresent() {
            return entry != null;
        }

        public boolean keyEquals(Object key) {
            return Objects.equals(key, entry.getKey());
        }

        public V getCurrentValue() {
            return isPresent() ? entry.getValue() : null;
        }

        public V set(K key, V value) {
            if (entry == null) {
                incrementSize(1);
                entry = new RefEntry(getViewKey(hash, index), key, null);
            }

            return entry.setValue(value);
        }

        public V remove() {
            if (!isPresent()) {
                return null;
            }

            incrementSize(-1);

            V oldValue = getCurrentValue();
            EntryInfo lastEntry = findLastEntry();
            if (lastEntry == null) {
                view.remove(entry.viewKey);

            } else {
                view.put(entry.viewKey, view.get(lastEntry.entry.viewKey));
                view.remove(lastEntry.entry.viewKey);

            }

            entry = null;
            return oldValue;
        }

        private EntryInfo findLastEntry() {
            EntryInfo lastEntry = null;
            EntryInfo entry;

            int startIndex = index + 1;

            while ((entry = new EntryInfo(startIndex, hash)).isPresent()) {
                startIndex++;
                lastEntry = entry;
            }

            return lastEntry;
        }
    }

    private class RefEntry implements Entry<K, V> {

        private final String viewKey;
        private final K key;
        private V value;

        private RefEntry(String viewKey, K key, V value) {
            this.viewKey = viewKey;
            this.key = key;
            this.value = value;
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public V setValue(V value) {
            V oldValue = this.value;

            this.value = value;
            view.put(viewKey, pairCaster.castIn(key, value));

            return oldValue;
        }
    }

    private static String getHash(Object key) {
        return toHexString(Objects.hashCode(key), 4);
    }

    private static String getViewKey(String hash, int index) {
        return hash + "." + index;
    }

    private static String extractHash(String viewKey) {
        return viewKey.substring(0, viewKey.lastIndexOf('.'));
    }


    private static String toHexString(int n, int minSize) {
        String t = Integer.toHexString(n);
        while (t.length() < minSize) {
            t = "0" + t;
        }
        return t;
    }
}
