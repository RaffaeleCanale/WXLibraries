package com.wx.properties.structures.set;

import com.wx.properties.structures.view.StructureView;
import com.wx.util.DefArgs;
import com.wx.util.representables.TypeCaster;
import com.wx.util.representables.string.SetRepr;

import java.util.*;

/**
 * Created on 20/01/2016
 *
 * @author Raffaele Canale (raffaelecanale@gmail.com)
 * @version 0.1
 */
public class SingleLineSet<E> extends SetResource<E> {

    public static class Factory implements SetResourceFactory {

        @Override
        public <E> SetResource<E> loadSet(StructureView view, TypeCaster<String, E> caster, Object... args) {
            String separator = new DefArgs<>(args).arg(0, null, String::valueOf);
            SetRepr<E> setRepr = new SetRepr<>(caster, separator);
            String header = view.getHeader();

            if (header == null) {
                return null;
            }

            Set<E> initialSet = setRepr.castOut(header);
            return new SingleLineSet<>(view, setRepr, initialSet);
        }

        @Override
        public <E> SetResource<E> createSet(StructureView view, TypeCaster<String, E> caster, Set<E> otherSet, Object... args) {
            String separator = new DefArgs<>(args).arg(0, null, String::valueOf);
            Objects.requireNonNull(otherSet);
            SetRepr<E> setRepr = new SetRepr<>(caster, separator);

            view.setHeader(setRepr.castIn(otherSet));

            return new SingleLineSet<>(view, setRepr, new HashSet<>(otherSet));
        }
    }

    public static SetResourceFactory factory() {
        return new Factory();
    }

    private final SetRepr<E> setCaster;
    private final Set<E> set;

    public SingleLineSet(StructureView view, SetRepr<E> setCaster, Set<E> set) {
        super(view);
        this.setCaster = setCaster;
        this.set = set;
    }

    private void save() {
        view.setHeader(setCaster.castIn(set));
    }

    @Override
    public Set<E> removeStructure() {
        view.removeHeader();
        return set;
    }

    @Override
    public int size() {
        return set.size();
    }

    @Override
    public Iterator<E> iterator() {
        Iterator<E> it = set.iterator();
        return new Iterator<E>() {
            @Override
            public boolean hasNext() {
                return it.hasNext();
            }

            @Override
            public E next() {
                return it.next();
            }

            @Override
            public void remove() {
                it.remove();
                save();
            }
        };
    }

    @Override
    public boolean add(E e) {
        if (set.add(e)) {
            save();
            return true;
        }

        return false;
    }

    @Override
    public boolean addAll(Collection<? extends E> collection) {
        if (set.addAll(collection)) {
            save();
            return true;
        }

        return false;
    }

    @Override
    public boolean remove(Object o) {
        if (set.remove(o)) {
            save();
            return true;
        }

        return false;
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        if (set.removeAll(collection)) {
            save();
            return true;
        }

        return false;
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        if (set.retainAll(collection)) {
            save();
            return true;
        }

        return false;
    }

    @Override
    public Object[] toArray() {
        return set.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return set.toArray(a);
    }

    @Override
    public void clear() {
        set.clear();
        save();
    }

    @Override
    public boolean isEmpty() {
        return set.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return set.contains(o);
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        return set.containsAll(collection);
    }

    @Override
    public String toString() {
        return set.toString();
    }
}
