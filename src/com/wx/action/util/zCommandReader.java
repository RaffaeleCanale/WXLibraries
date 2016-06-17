//package com.wx.actionN.util;
//
//import com.wx.console.InputException;
//import com.wx.console.command.Command;
//import com.wx.util.representables.TypeCaster;
//import com.wx.util.representables.string.IntRepr;
//import com.wx.util.representables.string.StringRepr;
//import java.util.LinkedList;
//import java.util.List;
//
///**
//*
//* @author Raffaele Canale (raffaelecanale@gmail.com)
//* @version 0.1
//*/
//public class zCommandReader {
//
//
//
//    private Command cmd;
//
//    public Object[] read(CommandContainer cc, Command cmd) throws InputException {
//        List<PropertyContainer> props = cc.getProperties();
//
//        Object[] result = new Object[props.size()];
//
//        int i = 0;
//        for (PropertyContainer prop : props) {
//            if (prop.isMarkedOption()) {
//                result[i] = readOption(prop);
//            }
//            i++;
//        }
//
//        i = 0;
//        for (PropertyContainer prop : props) {
//            if (!prop.isMarkedOption()) {
//                result[i] = readArgument(prop);
//            }
//            i++;
//        }
//
//        return result;
//    }
//
//
//    public Object readOption(PropertyContainer option) throws InputException {
//        assert option.isMarkedOption();
//
//        String[] markers = option.getMarkers();
//
//        int index = cmd.indexOf(markers);
//
//        if (index >= 0) {
//            Object[] values = registerWithMinMax(index, option);
//
//            return values == null ? new Object[0] : values;
//        }
//
//        return null;
//    }
//
//    // Argument
//    private Object readArgument(PropertyContainer prop) throws InputException {
//        Object[] values = registerWithMinMax(0, prop);
//
//        if (values == null) {
//            values = getDefaults(prop);
//        }
//
//        if (prop.getMax() == 1) {
//            assert values.length <= 1;
//            return values.length > 0 ? values[0] : null;
//        }
//        return values;
//    }
//
//
//    private Object[] registerWithMinMax(final int from, PropertyContainer prop) throws InputException {
//        List<Object> result = new LinkedList<>();
//
//        int count = 0;
//        while (count < prop.getMax() && cmd.length() > from
//                && !cmd.param(from).startsWith("-")) {
////            result.add(cmd.param(from, caster));
//            result.add(getParam(prop, from));
//            cmd = cmd.drop(from);
//            count++;
//        }
//
////        if (def != null) {
////            for (int i = result.size(); i < def.size(); i++) {
////                result.add(def.get(i));
////            }
////        }
//
//        if (count < prop.getMin()) {
//            throw new InputException("Missing argument(s)\n"
//                    + "Try 'help " + cmd.getName() + "'");
//        }
//
//        if (count > 0) {
////            values = result;
//            return result.toArray();
//        }
//
//        return null;
//    }
//
//    private Object getParam(PropertyContainer prop, int index) throws InputException {
//        switch (prop.getType()) {
//            case "String":
//                return cmd.param(index);
//            case "int":
//                return cmd.param(index, new IntRepr());
//            default:
//                throw new AssertionError();
//        }
//    }
//
//    private Object[] getDefaults(PropertyContainer prop) {
//        String[] defaults = prop.getDefaultValues();
//        Object[] result = new Object[defaults.length];
//        TypeCaster<String, ?> caster = getCaster(prop);
//
//        for (int i = 0; i < result.length; i++) {
//            result[i] = caster.castOut(defaults[i]);
//        }
//
//        return result;
//    }
//
//    private TypeCaster<String, ?> getCaster(PropertyContainer prop) {
//        switch (prop.getType()) {
//            case "String":
//                return new StringRepr();
//            case "int":
//                return new IntRepr();
//            default:
//                throw new AssertionError();
//        }
//    }
//}
