//package com.wx.actionN;
//
//
//import com.google.common.base.Supplier;
//import com.wx.util.condition.Condition;
//
//import java.util.List;
//
///**
// *
// *
// * Created on 18/11/2014
// *
// * @author Raffaele Canale (raffaelecanale@gmail.com)
// * @version 0.1
// */
//public abstract class zActionArgs {
//
//
//    @SafeVarargs
//    public final String getString(String defaultValue, Condition<String>... conditions) {
//        return getValue(this::supplyString, conditions, defaultValue);
//    }
//
//    @SafeVarargs
//    public final String getString(Condition<String>... conditions) {
//        return getValue(this::supplyString, conditions, null);
//    }
//
//    public final List<String> getStringList(Condition<List<String>>... conditions) {
//        return getValue(() -> supplyList(String.class), conditions, null);
//    }
//
//    @SafeVarargs
//    public final Integer getInt(Integer defaultValue, Condition<Integer>... conditions) {
//        return getValue(this::supplyInt, conditions, defaultValue);
//    }
//
//    @SafeVarargs
//    public final Integer getInt(Condition<Integer>... conditions) {
//        return getValue(this::supplyInt, conditions, null);
//    }
//
//
//    protected abstract String supplyString();
//
//    protected abstract Integer supplyInt();
//
//    protected abstract <T> List<T> supplyList(Class<T> c);
//
//
//    private <T> T getValue(Supplier<T> supplier, Condition<T>[] conditions, T defaultValue) {
//        T value = supplier.get();
//        if (value == null) {
//            return defaultValue;
//        }
//
//        for (Condition<T> condition : conditions) {
//            condition.check(value);
//        }
//
//        return value;
//    }
//}
