package com.wx.util.pair;

import java.util.function.BiFunction;

/**
 * This class represents a Tuple of any types
 *
 * @param <E> Type of the first tuple element
 * @param <F> Type of the second tuple element
 * @author Raffaele Canale
 */
public class Pair<E, F> {

    public static <E, F> Pair<E, F> of(E e, F f) {
        return new Pair<>(e, f);
    }

    private E e;
    private F f;

    /**
     * Constructs a new pair.
     *
     * @param e First element of the pair
     * @param f Second element of the pair
     */
    public Pair(E e, F f) {
        this.e = e;
        this.f = f;
    }


    public <T> T apply(BiFunction<E, F, T> function) {
        return function.apply(e, f);
    }

    /**
     * Get the first element of the pair.
     *
     * @return First element of the pair.
     */
    public E get1() {
        return e;
    }

    /**
     * Set the first element of the pair.
     *
     * @param e New element to set
     */
    public void set1(E e) {
        this.e = e;
    }

    /**
     * Get the second element of the pair.
     *
     * @return Second element of the pair.
     */
    public F get2() {
        return f;
    }

    /**
     * Set the second element of the pair.
     *
     * @param f New element to set
     */
    public void set2(F f) {
        this.f = f;
    }

    /**
     * @return {@code true} if any of the pair elements is null
     */
    public boolean containsNull() {
        return e == null || f == null;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        @SuppressWarnings("unchecked")  //Actually, it's checked
        final Pair<E, F> other = (Pair<E, F>) obj;
        if (this.e != other.e && (this.e == null || !this.e.equals(other.e))) {
            return false;
        }
        if (this.f != other.f && (this.f == null || !this.f.equals(other.f))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + (this.e != null ? this.e.hashCode() : 0);
        hash = 71 * hash + (this.f != null ? this.f.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "'" + e + "' / '" + f + "'";
    }
}
