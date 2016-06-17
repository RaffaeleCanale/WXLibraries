///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package com.wx.util.representables;
//
//import com.wx.util.pair.Pair;
//import com.wx.util.representables.string.PairRepr;
//import com.wx.util.representables.string.StringRepr;
//import java.util.List;
//import org.junit.Test;
//
///**
// *
// * @author Raffaele Canale - raffaelecanale@gmail.com
// */
//public class PairReprTest extends GenericCasterTest<String, Pair<String, String>> {
//
//    private static final String SEPARATOR = "/";
//
//    public PairReprTest() {
////        super(SEPARATOR);
//    }
//
//
//    @Override
//    protected TypeCaster<String, Pair<String, String>> getCaster() {
//        StringRepr subRepr1 = new StringRepr();
//        StringRepr subRepr2 = new StringRepr();
//        return new PairRepr<>(subRepr1, subRepr2, SEPARATOR);
//    }
//
//    @Override
//    protected Pair<String, String> castToArrayType(List<String> list) {
//        assertEquals(2, list.size());
//        return new Pair<>(list.get(0), list.get(1));
//    }
//
//    @Test (expected=ClassCastException.class)
//    public void testInvalidPair() {
//        castOut("element1" + SEPARATOR + "element2" + SEPARATOR + "element3");
//    }
//
//    @Override
//    public void testEmptyIn() {
//        // Impossible to test
//    }
//
//    @Override @Test(expected=ClassCastException.class)
//    public void testEmptyOut() {
//        castOut("");
//    }
//
//    @Test(expected=ClassCastException.class)
//    public void testSingleIn() {
//        castOut("hello");
//    }
//
//
//
//
//    @Override
//    protected void createTests() {
//        addTestPair("23");
//        addTestPair(null);
//
//        startNewTestBuild();
//        addTestPair(null);
//        addTestPair(null);
//
//        startNewTestBuild();
//        addTestPair("");
//        addTestPair(null);
//
//        startNewTestBuild();
//        addTestPair(AbstractArrayRepr.EMPTY_TAG);
//        addTestPair("");
//
//        startNewTestBuild();
//        addTestPair(AbstractArrayRepr.NULL_TAG);
//        addTestPair(AbstractArrayRepr.NULL_TAG);
//
//        startNewTestBuild();
//        addTestPair("");
//        addTestPair("");
//    }
//
//}
