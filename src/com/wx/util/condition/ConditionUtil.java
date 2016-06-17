package com.wx.util.condition;

import java.util.List;

/**
 *
 * @author Raffaele Canale (raffaelecanale@gmail.com)
 * @version 0.1
 */
public class ConditionUtil {
    
    public static <E> Condition<List<E>> forEach(Condition<E>... conditions) {
        return list -> {
            for (E element : list) {
                for (Condition<E> cond : conditions) {
                    cond.check(element);
                }
            }
        };
    }

}
