package com.wx.util.collections;

import com.wx.util.future.Future;
import com.wx.util.future.IoIterator;

import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;

/**
 * Created on 11/11/2015
 *
 * @author Raffaele Canale (raffaelecanale@gmail.com)
 * @version 0.1
 */
public class CollectionsUtil {

    private static final IoIterator<Object> EMPTY_ITERATOR = new emptyIterator<>();

    @SuppressWarnings("unchecked")
    public static <T> IoIterator<T> emptyIterator() {
        return (IoIterator<T>) EMPTY_ITERATOR;
    }


    public static <E> List<E> safe(List<E> source) {
        return Collections.unmodifiableList(new ArrayList<>(source));
    }

    public static <E> Set<E> safe(Set<E> source) {
        return Collections.unmodifiableSet(new HashSet<>(source));
    }

    public static <K,V> Map<K,V> safe(Map<K,V> source) {
        return Collections.unmodifiableMap(new HashMap<>(source));
    }


    private static class emptyIterator<T> implements IoIterator<T> {
        @Override
        public T next() throws IOException {
            throw new NoSuchElementException();
        }

        @Override
        public boolean hasNext() {
            return false;
        }

        @Override
        public void remove() throws IOException {
            throw new IllegalStateException();
        }

        @Override
        public void forEachRemaining(Consumer<? super T> action) {
            Objects.requireNonNull(action);
        }

        @Override
        public Iterator<Future<T>> iterator() {
            return Collections.emptyIterator();
        }
    }
}
