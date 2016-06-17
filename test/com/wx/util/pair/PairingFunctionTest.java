///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package com.wx.util.pair;
//
//import org.junit.Assert;
//import org.junit.Test;
//
///**
// *
// * @author Raffaele Canale - raffaelecanale@gmail.com
// */
//public class PairingFunctionTest extends Assert {
//
//    private static final PairingFunction function = new ExpFunction();
//    private final long[] testNumbers = {0L, 16L, 1L, 56L};
//
//    private final long testPaired = 21445424562L;
//    private final int depth = 122;
//
//    @Test
//    public void testPair() {
//        long z = PairingFunction.pair(function, testNumbers);
//        assertTrue(z >= 0);
//
//        long[] result = PairingFunction.unPair(z, testNumbers.length, function);
//        assertArrayEquals(testNumbers, result);
//    }
//
//    @Test
//    public void testUnpair() {
//        long[] values = PairingFunction.unPair(testPaired, depth, function);
//        for (long value : values) {
//            assertTrue(value >=  0);
//        }
//        long z =PairingFunction.pair(function, values);
//
//        assertEquals(testPaired, z);
//    }
//
//}