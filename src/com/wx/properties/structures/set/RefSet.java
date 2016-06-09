/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wx.properties.structures.set;

import com.wx.properties.structures.view.StructureView;
import com.wx.util.DefArgs;
import com.wx.util.representables.TypeCaster;
import com.wx.util.representables.string.IntRepr;
import com.wx.util.representables.string.SetRepr;

import java.util.*;

/**
 * @param <E>
 *
 * @author Raffaele Canale - raffaelecanale@gmail.com
 * @version 0.1
 *          <p>
 *          <p>
 *          Project: WXLibraries File: RefSet.java (UTF-8) Date: May 17, 2013
 */
public class RefSet<E> extends SetResource<E> {

    public static class Factory implements SetResourceFactory {

        @Override
        public <E> SetResource<E> loadSet(StructureView view, TypeCaster<String, E> caster, Object... args) {
            String header = view.getHeader();
            String separator = new DefArgs<>(args).arg(0, null, String::valueOf);
            RefSet<E> result = new RefSet<>(view, caster, separator);

            if (header == null) {
                return null;
            }

            result.size = new IntRepr().castOut(header);
            return result;
        }

        @Override
        public <E> SetResource<E> createSet(StructureView view, TypeCaster<String, E> caster, Set<E> otherSet, Object... args) {
            String separator = new DefArgs<>(args).arg(0, null, String::valueOf);
            RefSet<E> result = new RefSet<>(view, caster, separator);
            Objects.requireNonNull(otherSet);

            view.clear();
            result.addAll(otherSet);
            result.setSize(otherSet.size());

            return result;
        }
    }

    public static SetResourceFactory factory() {
        return new Factory();
    }

    private final SetRepr<E> caster;
    private int size;

    private RefSet(StructureView view, TypeCaster<String, E> elemCaster,
                   String separator) {
        super(view);
        caster = new SetRepr<>(elemCaster, separator);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean contains(Object o) {
        String hash = toHexString(o);
        Set<E> elements = getSetFor(hash);

        return elements != null && elements.contains(o);

    }

    @Override
    public Iterator<E> iterator() {
        Set<String> keys = new HashSet<>(getKeys());
        if (keys.isEmpty()) {
            return Collections.emptyIterator();
        } else {
            return new RefSetIterator(keys.iterator());
        }
    }

    @Override
    public boolean add(E e) {
        String hash = toHexString(e);

        Set<E> set = getSetFor(hash);
        if (set == null) {
            set = new HashSet<>();
        }

        if (set.add(e)) {
            setSetFor(hash, set);
            setSize(size + 1);
            return true;
        }
        return false;
    }

    @Override
    public boolean remove(Object o) {
        String hash = toHexString(o);

        Set<E> set = getSetFor(hash);

        if (set != null && set.remove(o)) {
            setSetFor(hash, set);
            setSize(size - 1);
            return true;
        }
        return false;
    }

    @Override
    public void clear() {
        view.clear();
        setSize(0);
    }

    private void setSize(int newSize) {
        size = newSize;
        view.setHeader(String.valueOf(size));
    }

    private static String toHexString(Object e) {
        String t = Integer.toHexString(Objects.hashCode(e));
        while (t.length() < 8) {
            t = "0" + t;
        }
        return t;
    }

    private Set<String> getKeys() {
        return view.keySet();
    }

    private Set<E> getSetFor(String hash) {
        String value = view.get(hash);
        return value == null ? null : caster.castOut(value);
    }

    private void setSetFor(String hash, Set<E> set) {
        if (set.isEmpty()) {
            view.remove(hash);
        } else {
            view.put(hash, caster.castIn(set));
        }
    }

    private class RefSetIterator implements Iterator<E> {

        private final Iterator<String> keys;
        private Iterator<E> currentHashElementIterator;
        private String currentKey;
        private Set<E> currentHashElementSet;

        private RefSetIterator(Iterator<String> keys) {
            assert keys.hasNext();

            this.keys = keys;
            loadNextKey();
        }

        @Override
        public boolean hasNext() {
            return keys.hasNext() || currentHashElementIterator.hasNext();
        }

        @Override
        public E next() {
            if (!currentHashElementIterator.hasNext()) {
                loadNextKey();
            }

            return currentHashElementIterator.next();
        }

        @Override
        public void remove() {
            currentHashElementIterator.remove();
            setSetFor(currentKey, currentHashElementSet);
            setSize(size - 1);
        }

        private void loadNextKey() {
            currentKey = keys.next();
            currentHashElementSet = getSetFor(currentKey);
            currentHashElementIterator = currentHashElementSet.iterator();
        }
    }

}
