///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package com.wx.util.pair;
//
//import java.math.BigDecimal;
//import java.math.BigInteger;
//import java.util.LinkedList;
//import java.util.List;
//
///**
// *
// * @author Raffaele
// */
//public class Stats {
//    private static final int precision = 10;
//    private static final int colWidth = 12;
//    private static final int firstWidth = 10;
//
//    public static void main(String[] args) {
//
//        //stats(1000, 100000, 10);
//        samples(new ExpFunction(), 15, 20, 10000, 100000000);
//
//    }
//
//    public static void samples(PairingFunction func, int cols, int... regions) {
//        for(int region : regions) {
//            stats(region-precision, region+precision, cols, func);
//        }
//    }
//
//    private static boolean rPrint;
//
//    public static void stats(int start, int end, int cols, PairingFunction func) {
//        rPrint = true;
//
//        String print = format("N", firstWidth) + "|" +
//                            format(" Size of Z", colWidth) + "|";
//        for(int i = 2; i < cols+2; i++)
//            print+= " Size of " + format(i, colWidth-9) + "|";
//
//        println(print);
//
//        for(int i = 0; i<print.length();i++)
//            print("-");
//        println();
//
//        rPrint = PRINT;
//        BigDecimal[] averages = new BigDecimal[cols+1];
//        for(int i = 0; i < averages.length; i++)
//            averages[i] = BigDecimal.ZERO;
//
//        int length;
//        for(int i = start; i < end; i++) {
//            length = BigInteger.valueOf(i).toString(2).length();
//            averages[0] = averages[0].add(BigDecimal.valueOf(length));
//            print(format(i, firstWidth) + "|"
//                    + format(" " + length, colWidth) + "|");
//
//
//            for (int j = 2; j < cols + 2; j++) {
//                List<Double> list = new LinkedList<>();//PairingFunction.unPair(i, j, func);
//                length = 0;
//                for(Double s : list) {
//                    BigInteger x = BigInteger.valueOf(s.longValue());
//                    length+= x.toString(2).length();
//                }
//                averages[j-1] = averages[j-1].add(BigDecimal.valueOf(length));
//                print(format(" " + length, colWidth) + "|");
//            }
//            println();
//        }
//        rPrint = true;
//        BigDecimal samples = BigDecimal.valueOf(end-start);
//        print = format("Averages", firstWidth) + "|";
//        for(BigDecimal average : averages) {
//            average = average.divide(samples);
//            print+= format(" " + average.toPlainString(), colWidth) + "|";
//        }
//
//
//        println(print);
//        for(int i = 0; i<print.length();i++)
//            print("-");
//        println();
//    }
//
//    public static String format(int n, int l) {
//        return format("" + n, l);
//    }
//
//    public static String format(String n, int l) {
//        if(!rPrint) return null;
//        while(n.length() < l)
//            n+= " ";
//        if(n.length() > l)
//            n = "+" + n.substring(l, n.length());
//
//        return n;
//    }
//
//    public static final boolean PRINT = true;
//    public static void print(String s) {
//        if(rPrint) System.out.print(s);
//    }
//    public static void println() {
//        if(rPrint) System.out.println();
//    }
//    public static void println(String s) {
//        if(rPrint) System.out.println(s);
//    }
//}
