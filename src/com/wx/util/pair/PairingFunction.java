///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package com.wx.util.pair;
//
///**
// *
// * @author Raffaele
// */
//public abstract class PairingFunction {
//
//    public static Pair<Long, Long> unPair(long z, PairingFunction pairingFunction) {
//        return pairingFunction.unPair(z);
//    }
//
//    public static long[] unPair(long z, int n, PairingFunction pairingFunction) {
//        if(n < 2) {
//            throw new IllegalArgumentException("n must be > 1");
//        }
//
//        long[] result = new long[n];
//
//        Pair<Long, Long> pair = pairingFunction.unPair(z);
//        result[0] = pair.get1();
//        for(int i = 0; i < n-2; i++) {
//            pair = pairingFunction.unPair(pair.get2());
//            result[i + 1] = pair.get1();
//        }
//        result[n - 1] = pair.get2();
//        return result;
//    }
//
//    public static long pair(PairingFunction pairingFunction, long... values) {
//        if(values.length < 2) {
//            throw new IllegalArgumentException();
//        }
//
//        int index = values.length - 1;
//        long z = values[index];
//        for(int i = index-1; i >= 0; i--) {
//            z = pairingFunction.pair(values[i], z);
//            if (z < 0) {
//                throw new RuntimeException("Long capacity exceeded: " + z);
//            }
//        }
//        return z;
//    }
//
//    public long pair(Pair<Long, Long> pair) {
//        return pair(pair.get1(), pair.get2());
//    }
//
//    public long pair(long x, long y) {
//        if (x < 0 || y < 0) {
//            throw new IllegalArgumentException("Arguments must be positive: " + x + ", " + y);
//        }
//        return pair0(x, y);
//    }
//
//    public Pair<Long, Long> unPair(long z) {
//        if (z < 0) {
//            throw new IllegalArgumentException("Arguments must be positive: " + z);
//        }
//
//        return unPair0(z);
//    }
//
//    protected abstract long pair0(long x, long y);
//    protected abstract Pair<Long, Long> unPair0(long z);
//
//}
