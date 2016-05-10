package com.wx.properties.structures.list;

import com.wx.properties.structures.view.StructureView;
import com.wx.util.representables.TypeCaster;
import com.wx.util.representables.string.IntRepr;

import java.util.*;

/**
 *
 * @author Raffaele Canale - raffaelecanale@gmail.com
 * @version 0.6
 *
 * Project: WXLibraries File: IndexedList.java (UTF-8) Date: May 13, 2013
 */
public class IndexedList<E> extends ListResource<E> {

    public static class Factory implements ListResourceFactory {
        @Override
        public <E> IndexedList<E> loadList(StructureView view, TypeCaster<String, E> caster, Object... args) {
            String header = view.getHeader();
            IndexedList<E> result = new IndexedList<>(view, caster);

            if (header == null) {
                return null;
            }

            result.size = new IntRepr().castOut(header);

            return result;
        }

        @Override
        public <E> IndexedList<E> createList(StructureView view, TypeCaster<String, E> caster, List<E> otherList, Object... args) {
            IndexedList<E> newList = loadList(view, caster);

            if (newList != null && newList.size() > otherList.size()) {
                for (int i = otherList.size(); i < newList.size(); i++) {
                    newList.removeValue(i);
                }
            } else {
                newList = new IndexedList<>(view, caster);
            }

            int i = 0;
            for (E e : otherList) {
                newList.setValue(i, caster.castIn(e));
                i++;
            }
            newList.setSize(otherList.size());
            return newList;
        }
    }

    public static ListResourceFactory factory() {
        return new Factory();
    }

    private final TypeCaster<String, E> caster;
    private int size;

    private IndexedList(StructureView view, TypeCaster<String, E> caster) {
        super(view);
        this.caster = Objects.requireNonNull(caster);
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
    public Iterator<E> iterator() {
        return listIterator(0);
    }

    @Override
    public void add(int index, E element) {
        rangeCheckForAdd(index);

        shiftValuesDown(index, 1);

        setValue(index, caster.castIn(element));
        incrementSize(1);
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        rangeCheckForAdd(index);

        shiftValuesDown(index, c.size());

        int i = 0;
        for (E element : c) {
            setValue(index + i, caster.castIn(element));
            i++;
        }

        incrementSize(c.size());
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int i = indexOf(o);
        if (i < 0) {
            return false;
        }

        remove(i);
        return true;
    }

    @Override
    public E remove(int index) {
        rangeCheck(index);
        String oldValue = getValue(index);

        shiftValuesUp(index + 1, 1);
        removeValue(size - 1);

        incrementSize(-1);

        return caster.castOut(oldValue);
    }

    @Override
    public void clear() {
        view.clear();
        setSize(0);
    }

    @Override
    public E get(int index) {
        rangeCheck(index);
        String value = getValue(index);

        return caster.castOut(value);
    }

    @Override
    public E set(int index, E element) {
        rangeCheck(index);
        String oldValue = getValue(index);

        setValue(index, caster.castIn(element));

        return caster.castOut(oldValue);
    }

    @Override
    public int indexOf(Object o) {
        for (int i = 0; i < size; i++) {
            if (Objects.equals(get(i), o)) {
                return i;
            }
        }

        return -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        for (int i = size - 1; i >= 0; i--) {
            if (get(i).equals(o)) {
                return i;
            }
        }

        return -1;
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        return new IndexedListIterator(index);
    }

    private void shiftValuesDown(int fromIndex, int delta) {
        for (int i = size() - 1; i >= fromIndex; i--) { //Shift all indexes
            String toShift = getValue(i);
            setValue(i + delta, toShift);
        }
    }

    private void shiftValuesUp(int fromIndex, int delta) {
        assert fromIndex - delta >= 0;

        for (int i = fromIndex; i < size; i++) {
            String toShift = getValue(i);
            setValue(i - delta, toShift);
        }
    }

    private void rangeCheck(int index) {
        if (index >= size || index < 0) {
            throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
        }
    }

    private void rangeCheckForAdd(int index) {
        if (index > size || index < 0) {
            throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
        }
    }

    private String outOfBoundsMsg(int index) {
        return "Index: " + index + ", Size: " + size;
    }

    private String getValue(int index) {
        return view.get("[" + index + "]");
    }

    private String setValue(int index, String value) {
        String key = "[" + index + "]";

        return value == null ? view.remove(key) : view.put(key, value);
    }

    private String removeValue(int index) {
        return view.remove("[" + index + "]");
    }

    private class IndexedListIterator implements ListIterator<E> {

        private int cursor = 0;
        private int lastEmitted = -1;

        private IndexedListIterator(int cursor) {
            this.cursor = cursor;
        }

        @Override
        public boolean hasNext() {
            return cursor < size;
        }

        @Override
        public E next() {
            if (cursor >= size) {
                throw new NoSuchElementException();
            }

            int ret = cursor;

            lastEmitted = cursor;
            cursor++;

            return get(ret);
        }

        @Override
        public boolean hasPrevious() {
            return cursor > 0;
        }

        @Override
        public E previous() {
            if (cursor <= 0) {
                throw new NoSuchElementException();
            }

            cursor--;
            lastEmitted = cursor;

            return get(cursor);
        }

        @Override
        public int nextIndex() {
            return cursor;
        }

        @Override
        public int previousIndex() {
            return cursor - 1;
        }

        @Override
        public void remove() {
            if (lastEmitted < 0) {
                throw new IllegalStateException("Nothing to remove");
            }
            // TODO Directly remove, without going trough public API ?

            IndexedList.this.remove(lastEmitted);
            cursor = lastEmitted;
            lastEmitted = -1;
        }

        @Override
        public void set(E e) {
            if (lastEmitted < 0) {
                throw new IllegalStateException("Nothing to set");
            }

            IndexedList.this.set(lastEmitted, e);
//            lastEmitted = -1;
        }

        @Override
        public void add(E e) {
            IndexedList.this.add(cursor, e);
            cursor++;
            lastEmitted = -1;
        }
    }
}