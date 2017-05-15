package com.wx.util.iterator;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author Raffaele Canale (<a href="mailto:raffaelecanale@gmail.com?subject=WXLibraries">raffaelecanale@gmail.com</a>)
 * @version 0.1 - created on 15.05.17.
 */
public class FilteredIteratorTest {

    @Test
    public void empty() {
        List<Integer> list = Arrays.asList();

        FilteredIterator<Integer> it = new FilteredIterator<>(list.iterator(), n -> true);

        assertFalse(it.hasNext());
    }

    @Test
    public void noMatch() {
        List<Integer> list = Arrays.asList(11,12,14,15);

        FilteredIterator<Integer> it = new FilteredIterator<>(list.iterator(), n -> n < 10);

        assertFalse(it.hasNext());
    }

    @Test
    public void regular() {
        List<Integer> list = Arrays.asList(1,2,3,4,5);

        FilteredIterator<Integer> it = new FilteredIterator<>(list.iterator(), n -> n % 2 == 0);

        assertTrue(it.hasNext());
        assertEquals(2, (int) it.next());
        assertTrue(it.hasNext());
        assertEquals(4, (int) it.next());
        assertFalse(it.hasNext());
    }

}