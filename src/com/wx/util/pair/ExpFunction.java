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
//public class ExpFunction extends PairingFunction {
//
//    @Override
//    protected long pair0(long x, long y) {
//        return (long) (Math.pow(2, x)*(2*y+1) - 1);
//    }
//
//    @Override
//    protected Pair<Long, Long> unPair0(long z) {
//        long x = 0;
//        z += 1;
//        while (z % 2 == 0) {
//            x += 1;
//            z /= 2;
//        }
//        //TODO Maybe improve with log2(z), maybe...
//        long y = (z - 1) / 2;
//        return new Pair<>(x, y);
//    }
//
//}
