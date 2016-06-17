//package com.wx.actionN.util;
//
//import com.wx.action.ActionBuilder;
//import java.util.List;
//
///**
// *
// * @author Raffaele Canale (raffaelecanale@gmail.com)
// * @version 0.1
// */
//public class Values implements ActionBuilder {
//    
//    private Values() {};
//    
//    @Override
//    public List<Integer> getIntArray(Object id) {
//        throw new ClassCastException("Not an int array: " + this);
//    }
//
//    @Override
//    public int getIntValue(Object id) {
//        throw new ClassCastException("Not an int: " + this);
//    }
//
//    @Override
//    public List<String> getStringArray(Object id) {
//        throw new ClassCastException("Not a String array: " + this);
//    }
//
//    @Override
//    public String getStringValue(Object id) {
//        throw new ClassCastException("Not a String: " + this);
//    }
//    
//    public static class IntArrayValue extends Values {
//        private final String value;
//
//        public IntArrayValue(String value) {
//            this.value = value;
//        }
//        
//        @Override
//        public String getStringValue(Object id) {
//            return value;
//        }        
//    }
//    public static class StringArrayValue extends Values {
//        private final List<String> value;
//
//        public StringArrayValue(List<String> value) {
//            this.value = value;
//        }
//
//        @Override
//        public List<String> getStringArray(Object id) {
//            return value;
//        }
//    }
//    public static class IntValue extends Values {
//        private final int value;
//
//        public IntValue(int value) {
//            this.value = value;
//        }
//        
//        @Override
//        public int getIntValue(Object id) {
//            return value;
//        }        
//    }
//    public static class StringValue extends Values {
//        private final List<Integer> value;
//
//        public StringValue(List<Integer> value) {
//            this.value = value;
//        }
//        
//        @Override
//        public List<Integer> getIntArray(Object id) {
//            return value;
//        }        
//    }
//
//}
