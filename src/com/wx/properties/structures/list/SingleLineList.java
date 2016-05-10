package com.wx.properties.structures.list;

import com.wx.properties.structures.view.StructureView;
import com.wx.util.Args;
import com.wx.util.representables.TypeCaster;
import com.wx.util.representables.string.ListRepr;

import java.util.*;


/**
 *
 * @author Raffaele Canale - raffaelecanale@gmail.com
 * @version 0.1
 *
 * Project: WXLibraries File: SingleLineList.java (UTF-8) Date: May 13, 2013
 */
public class SingleLineList<E> extends ListResource<E> {

    public static class Factory implements ListResourceFactory {
        @Override
        public <E> ListResource<E> loadList(StructureView view, TypeCaster<String, E> caster, Object... args) {
            String separator = new Args<>(args).arg(0, null, String::valueOf);
            String header = view.getHeader();

            ListRepr<E> listRepr = new ListRepr<>(caster, separator);

            if (header == null) {
                return null;
            }


            List<E> list = listRepr.castOut(header);

            return new SingleLineList<>(view, listRepr, list);
        }

        @Override
        public <E> ListResource<E> createList(StructureView view, TypeCaster<String, E> caster, List<E> otherList, Object... args) {
            String separator = new Args<>(args).arg(0, null, String::valueOf);
            Objects.requireNonNull(otherList);

            ListRepr<E> listRepr = new ListRepr<>(caster, separator);
            view.setHeader(listRepr.castIn(otherList));

            return new SingleLineList<>(view, listRepr, new ArrayList<>(otherList));
        }
    }

    public static ListResourceFactory factory() {
        return new Factory();
    }

    private final ListRepr<E> listCaster;
    private final List<E> list;

    private SingleLineList(StructureView view, ListRepr<E> listCaster, List<E> list) {
        super(view);
        this.listCaster = listCaster;
        this.list = list;
    }

    void save() {
        view.setHeader(listCaster.castIn(list));
    }

    @Override
    public List<E> removeStructure() {
        view.removeHeader();
        return Collections.unmodifiableList(list);
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return list.toArray(a);
    }

    @Override
    public Object[] toArray() {
        return list.toArray();
    }

    @Override
    public ListResource<E> subList(int fromIndex, int toIndex) {
        return new SingleLineList<E>(view, null, list.subList(fromIndex, toIndex)) {
            @Override
            void save() {
                SingleLineList.this.save();
            }
        };
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public E set(int index, E element) {
        E result = list.set(index, element);
        save();
        return result;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean result = list.retainAll(c);
        save();
        return result;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean result = list.removeAll(c);
        save();
        return result;
    }

    @Override
    public E remove(int index) {
        E e = list.remove(index);
        save();
        return e;
    }

    @Override
    public boolean remove(Object o) {
        boolean b = list.remove(o);
        save();
        return b;
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        return new SingleLineIterator(list.listIterator(index));
    }

    @Override
    public ListIterator<E> listIterator() {
        return new SingleLineIterator(list.listIterator());
    }

    @Override
    public int lastIndexOf(Object o) {
        return list.lastIndexOf(o);
    }

    @Override
    public Iterator<E> iterator() {
        return listIterator();
    }

    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }

    @Override
    public int indexOf(Object o) {
        return list.indexOf(o);
    }

    @Override
    public int hashCode() {
        return list.hashCode();
    }

    @Override
    public E get(int index) {
        return list.get(index);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return list.containsAll(c);
    }

    @Override
    public boolean contains(Object o) {
        return list.contains(o);
    }

    @Override
    public void clear() {
        list.clear();
        save();
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        boolean b = list.addAll(index, c);
        save();
        return b;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean b = list.addAll(c);
        save();
        return b;
    }

    @Override
    public void add(int index, E element) {
        list.add(index, element);
        save();
    }

    @Override
    public boolean add(E e) {
        boolean b = list.add(e);
        save();
        return b;
    }

    private class SingleLineIterator implements ListIterator<E> {

        private ListIterator<E> iterator;

        private SingleLineIterator(ListIterator<E> iterator) {
            this.iterator = iterator;
        }

        @Override
        public void set(E e) {
            iterator.set(e);
            save();
        }

        @Override
        public void remove() {
            iterator.remove();
            save();
        }

        @Override
        public int previousIndex() {
            return iterator.previousIndex();
        }

        @Override
        public E previous() {
            return iterator.previous();
        }

        @Override
        public int nextIndex() {
            return iterator.nextIndex();
        }

        @Override
        public E next() {
            return iterator.next();
        }

        @Override
        public boolean hasPrevious() {
            return iterator.hasPrevious();
        }

        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }

        @Override
        public void add(E e) {
            iterator.add(e);
            save();
        }
    }
}
