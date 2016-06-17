/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wx.util.representables;

import static com.wx.util.representables.DelimiterEncoder.NULL_TAG;
import static com.wx.util.representables.DelimiterEncoder.EMPTY_TAG;
import com.wx.util.representables.string.ListRepr;
import com.wx.util.representables.string.StringRepr;
import junit.framework.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Raffaele Canale - raffaelecanale@gmail.com
 */
public class ListReprTest extends AbstractCollectionCasterTest<List<String>, String> {

    private static final String SEPARATOR = "//";

    public ListReprTest() {
        super(SEPARATOR);
    }

    @Override
    protected List<String> emptyCollection() {
        return new LinkedList<>();
    }
        
    @Override
    protected TypeCaster<String, List<String>> getCaster() {
        return new ListRepr<>(new StringRepr(), SEPARATOR);
    }

    @Test
    public void genericTest() {
        List<String> list = Arrays.asList("a", "b", "c", "d", null, "", null, "lol");

        TypeCaster<String, List<String>> caster = getCaster();
        Assert.assertEquals(list, caster.castOut(caster.castIn(list)));

        caster = ListRepr.stringRepr();
        Assert.assertEquals(list, caster.castOut(caster.castIn(list)));
    }


    @Override
    protected void setUpPairs() {
        pair("23.0" + SEPARATOR + "-2.9" + SEPARATOR + "2134.1", "23.0", "-2.9", "2134.1");
        pair("   " + SEPARATOR + " ", "   ", " ");
        pair("single", "single");
        pair("e1" + SEPARATOR + SEPARATOR + NULL_TAG, "e1", null);

        pair(SEPARATOR + EMPTY_TAG + SEPARATOR + SEPARATOR + NULL_TAG, "", null);

//        invalidOutput(SEPARATOR.charAt(0) + "");
        invalidOutput(SEPARATOR);
        invalidOutput("test" + SEPARATOR + "test");
        invalidInput(SEPARATOR + "foo");
        invalidInput("A" + SEPARATOR + SEPARATOR + "B");
        invalidInput("foo" + SEPARATOR + SEPARATOR);

    }
    
    
}
