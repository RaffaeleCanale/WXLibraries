package com.wx.action_deprecated;

/**
 *
 * @author Raffaele Canale (raffaelecanale@gmail.com)
 * @version 0.1
 */
public class ActionProperty<Type> implements Property<Type> {
    
    private Type value;

    public ActionProperty() {
    }

    public ActionProperty(Type value) {
        this.value = value;
    }

    
    
    @Override
    public Type get() {
        return value;
    }

    public void setValue(Type value) {
        this.value = value;
    }
}
