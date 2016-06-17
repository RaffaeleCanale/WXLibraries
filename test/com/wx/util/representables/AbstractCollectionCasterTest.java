/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.wx.util.representables;

import java.util.Collection;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author Raffaele Canale - raffaelecanale@gmail.com
 * @version 0.1
 *
 * Project: WXLibraries
 * File: AbstractArrayReprTest.java (UTF-8)
 * Date: 27 déc. 2012 
 */
@Ignore
public abstract class AbstractCollectionCasterTest<ArrayType extends Collection<ElementType>, ElementType> extends 
                                            GenericCasterTest<String, ArrayType> {
    
    private final String separator;
    
    public AbstractCollectionCasterTest(String separator) {
        this.separator = separator;
    }
    
    @Test
    public void testEmptyIn() {
        assertEquals(emptyCollection(), castOut(""));
    }
    
    @Test
    public void testEmptyOut() {
        assertEquals("", castIn(emptyCollection()));
    }


    protected abstract ArrayType emptyCollection();
    
    protected void pair(String value, ElementType... elements) {
        ArrayType array = emptyCollection();
        for (ElementType e : elements) {
            array.add(e);
        }
        
        super.pair(value, array);
    }


    protected void invalidOutput(ElementType... elements) {
        ArrayType array = emptyCollection();
        Collections.addAll(array, elements);

        super.invalidOutput(array);
    }
}
