package com.wx.util.condition;

/**
 *
 * @author Raffaele Canale (raffaelecanale@gmail.com)
 * @version 0.1
 */
public class IntCondition implements Condition<Integer> {
    
    public static IntCondition range(int min, int max) {
        return new IntCondition(min, max);
    }
    
    public static IntCondition max(int max) {
        return new IntCondition(Integer.MIN_VALUE, max);
    }
    
    public static IntCondition min(int min) {
        return new IntCondition(min, Integer.MAX_VALUE);
    }
    
    public static IntCondition positive() {
        return new IntCondition(0, Integer.MAX_VALUE);
    }
    
    
    private final int min;
    private final int max;

    private IntCondition(int min, int max) {
        this.min = min;
        this.max = max;
    }
    
    @Override
    public void check(Integer value) {
        if (value < min || value > max) {
            throw new IllegalArgumentException("Value must be within [" + min + ", " + max + "]");
        }
    }

}
