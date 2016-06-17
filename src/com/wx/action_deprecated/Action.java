//package com.wx.action_deprecated;
//
//import com.wx.util.condition.Condition;
//import com.wx.console.InputException;
//
///**
// *
// * @author Raffaele Canale (raffaelecanale@gmail.com)
// * @version 0.1
// */
//public class Action {
//
//    private final ActionBuilder builder;
//
////    private final List<PropertyContainer<SourceType, ?>> properties;
//
//    public Action(ActionBuilder builder) {
//        this.builder = builder;
//    }
//
//
//    public String[] stringArray(Condition<String[]>... conditions) throws InputException {
//        return stringArray(null, conditions);
//    }
//    public String[] stringArray(Object id, Condition<String[]>... conditions) throws InputException {
//        return checkConditions(conditions, builder.getStringArray(id));
//    }
//
//    public int intProperty(Condition<Integer>... conditions) throws InputException {
//        return intProperty(null, conditions);
//    }
//    public int intProperty(Object id, Condition<Integer>... conditions) throws InputException {
//        return checkConditions(conditions, builder.getIntValue(id));
//    }
//
//    public String stringProperty(Condition<String>... conditions) throws InputException {
//        return stringProperty(null, conditions);
//    }
//    public String stringProperty(Object id,
//            Condition<String>... conditions) throws InputException {
//        return checkConditions(conditions, builder.getStringValue(id));
//    }
//
//    private <Type> Type checkConditions(Condition<Type>[] conditions,
//            Type value) throws InputException {
//        for (Condition<Type> cond : conditions) {
//            cond.check(value);
//        }
//
//        return value;
//    }
//
////    private <Type> List<Type> checkConditionsArray(Condition<Type>[] conditions,
////            List<Type> values) throws InputException {
////        for (Condition<Type> cond : conditions) {
////            for (Type value : values) {
////                cond.check(value);
////            }
////        }
////
////        return values;
////    }
//
//}
