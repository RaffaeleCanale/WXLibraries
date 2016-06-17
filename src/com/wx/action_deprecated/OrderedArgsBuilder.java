package com.wx.action_deprecated;

/**
 *
 * @author Raffaele Canale (raffaelecanale@gmail.com)
 * @version 0.1
 */
public class OrderedArgsBuilder implements ActionBuilder {

    private final Object[] arguments;
    private int current = -1;

    public OrderedArgsBuilder(Object... arguments) {
        this.arguments = arguments;
    }
    
    @Override
    public int[] getIntArray(Object id) {
        return (int[]) next();
    }

    @Override
    public int getIntValue(Object id) {
        return (int) next();
    }

    @Override
    public String[] getStringArray(Object id) {
        return (String[]) next();
    }

    @Override
    public String getStringValue(Object id) {
        return (String) next();
    }
    
    
    private Object next() {
        current++;
        if (current >= arguments.length) {
            throw new IllegalStateException("All args read");
        }
        
        return arguments[current];
    }
}
