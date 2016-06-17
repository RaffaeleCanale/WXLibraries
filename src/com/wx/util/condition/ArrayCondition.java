package com.wx.util.condition;

import java.util.List;

/**
 *
 * @author Raffaele Canale (raffaelecanale@gmail.com)
 * @version 0.1
 */
public class ArrayCondition<Type> implements Condition<List<Type>>{
    
    public static ArrayCondition size(int min, int max) {
        return new ArrayCondition(min, max);
    }
    
    
    private final int min;
    private final int max;

    public ArrayCondition(int min, int max) {
        this.min = min;
        this.max = max < 0 ? Integer.MAX_VALUE : max;
    }

    @Override
    public void check(List<Type> value) {
        int size = value.size();
        if (size < min || size > max) {
            throw new IllegalArgumentException("Expected [" + min + ", " + max + "] "
                    + "elements but got " + size);
        }
    }
    
    

}
