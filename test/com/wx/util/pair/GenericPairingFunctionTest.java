///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//
//package com.wx.util.pair;
//
//import java.util.Random;
//import org.junit.Assert;
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertTrue;
//import org.junit.Ignore;
//import org.junit.Test;
//
///**
// *
// * @author Raffaele Canale - raffaelecanale@gmail.com
// * @version 0.1
// *
// * Project: WXLibraries.ANT
// * File: GenericPairingFunctionTest.java (UTF-8)
// * Date: Oct 8, 2013
// */
//@Ignore
//public abstract class GenericPairingFunctionTest extends Assert {
//
//    private static final int TESTS_COUNT = 100;
//    private final PairingFunction function = getFunction();
//
//    @Test
//    public void testPair() {
//
//        for (int i = 0; i < TESTS_COUNT; i++) {
//            final long x = Math.abs(new Random().nextInt(48));
//            final long y = Math.abs(new Random().nextInt(48));
//
//            final long z = function.pair(x, y);
//            assertTrue(z >= 0);
//            Pair<Long, Long> result = function.unPair(z);
//            assertTrue(result.get1() >= 0);
//            assertTrue(result.get2() >= 0);
//
//            String message = getMessage1(x, y, z, result);
//            assertEquals(message, (long) result.get1(), x);
//            assertEquals(message, (long) result.get2(), y);
//        }
//    }
//
//    @Test
//    public void testUnpair() {
//
//        for (int i = 0; i < TESTS_COUNT; i++) {
//            final long z = Math.abs(new Random().nextInt(48));
//            Pair<Long, Long> pair = function.unPair(z);
//            assertTrue(pair.get1() >= 0);
//            assertTrue(pair.get2() >= 0);
//
//            long z2 = function.pair(pair);
//            assertTrue(z2 >= 0);
//
//            String message = getMessage2(z, pair, z2);
//            assertEquals(message, z, z2);
//        }
//    }
//
//    private String getMessage1(long x, long y, long z, Pair<Long, Long> pair) {
//        return "Mismatch, starting numbers:\n"
//                + "\tx=" + x + "\n"
//                + "\ty=" + y + "\n"
//                + "Paired:\n"
//                + "\tz=" + z + "\n"
//                + "Unpaired back:\n"
//                + "\tresult=" + pair;
//    }
//
//    private String getMessage2(long z, Pair<Long, Long> pair, long z2) {
//        return "Mismatch, starting number:\n"
//                + "\tz=" + z + "\n"
//                + "Unpaired:\n"
//                + "\tpair=" + pair + "\n"
//                + "Paired back:\n"
//                + "\tz2=" + z2;
//    }
//
//    protected abstract PairingFunction getFunction();
//}
