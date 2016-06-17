package com.wx.util.representables;

import com.wx.util.pair.Pair;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.junit.Assert;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author Raffaele Canale - raffaelecanale@gmail.com
 * @version 1.0
 *
 * Project: WXLibraries
 * File: GeneralRepresentableTest.java (UTF-8)
 * Date: 26 déc. 2012 
 */
@Ignore 
public abstract class GenericCasterTest<InType, OutType> {
    
    private final TypeCaster<InType, OutType> caster = getCaster();
    private final List<Pair<InType, OutType>> inTestPairs = new ArrayList<>();
    private final List<Pair<InType, OutType>> outTestPairs = new ArrayList<>();
    private final List<InType> invalidInputs = new ArrayList<>();
    private final List<OutType> invalidOutputs = new ArrayList<>();

    
    @Before
    public void setUp() {
        if (inTestPairs.isEmpty() || outTestPairs.isEmpty()) {
            setUpPairs();
            assert !inTestPairs.isEmpty() && !outTestPairs.isEmpty() : "No test pairs added";
        }
    }
    
    protected void pair(InType in, OutType out) {
        Pair<InType, OutType> pair = new Pair<>(in, out);
        inTestPairs.add(pair);
        outTestPairs.add(pair);
    }

    protected void inPair(InType in, OutType out) {
        inTestPairs.add(new Pair<>(in, out));
    }

    protected void outPair(InType in, OutType out) {
        outTestPairs.add(new Pair<>(in, out));
    }

    protected void invalidInput(InType in) {
        invalidInputs.add(in);
    }
    protected void invalidOutput(OutType out) {
        invalidOutputs.add(out);
    }
    
    @Test
    public void testCastIn() {
        inTestPairs.forEach(this::assertCastIn);
    }
    
    @Test
    public void testCastOut() {
        outTestPairs.forEach(this::assertCastOut);
    }    
    
    @Test
    public void testInvalidInputs() {
        invalidInputs.forEach(i -> testInvalid(i, caster::castOut));
    }
    
    @Test
    public void testInvalidOutputs() {
        invalidOutputs.forEach(i -> testInvalid(i, caster::castIn));
    }
    
    
    private <E> void testInvalid(E element, Function<E, ?> f) {
        try {
            Object result = f.apply(element);
            throw new AssertionError("Expected error while casting but none occurred: " + element + " -> " + result);
        } catch (ClassCastException ex) {}
    }
    
    protected InType castIn(OutType value) {
        return caster.castIn(value);
    }
    
    protected OutType castOut(InType value) {
        return caster.castOut(value);
    }

    protected void assertOutEquals(OutType expected, OutType actual) {
        assertEquals(expected, actual);
    }

    protected void assertInEquals(InType expected, InType actual) {
        assertEquals(expected, actual);
    }

    private void assertCastOut(Pair<InType, OutType> pair) {
        assertOutEquals(pair.get2(), castOut(pair.get1()));
    }
    
    private void assertCastIn(Pair<InType, OutType> pair) {
        assertInEquals(pair.get1(), castIn(pair.get2()));
    }


    @Test(expected=NullPointerException.class)
    public void nullIn() {
        castIn(null);
    }

    @Test(expected=NullPointerException.class)
    public void nullOut() {
        castOut(null);
    }

    protected abstract TypeCaster<InType, OutType> getCaster();
    protected abstract void setUpPairs();
    
}
