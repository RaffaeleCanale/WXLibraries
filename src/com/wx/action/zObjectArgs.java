//package com.wx.actionN;
//
///**
// * Created by canale on 11/6/14.
// */
//public class zObjectArgs extends ActionArgs {
//
//    private final Object[] arguments;
//    private int current = -1;
//
//    public zObjectArgs(Object... arguments) {
//        this.arguments = arguments;
//    }
//
//
//
//    @Override
//    protected String supplyString() {
//        return (String) next();
//    }
//
//    @Override
//    protected Integer supplyInt() {
//        return (Integer) next();
//    }
//
//
//    private Object next() {
//        current++;
//        if (current >= arguments.length) {
//            throw new IllegalStateException("Not enough arguments provided");
//        }
//
//        return arguments[current];
//    }
//}
