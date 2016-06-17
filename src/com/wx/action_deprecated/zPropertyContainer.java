package com.wx.action_deprecated;

import com.wx.util.representables.TypeCaster;

/**
 *
 * @author Raffaele Canale (raffaelecanale@gmail.com)
 * @version 0.1
 */
public class zPropertyContainer<SourceType, Type> {
    
    private final ActionProperty<Type> property;
    private final TypeCaster<SourceType, Type> caster;

//    public PropertyContainer(Type defaultValue, TypeCaster<SourceType, Type> caster) {
//        this(new SimpleProperty<>(defaultValue), caster);
//    }
    
    public zPropertyContainer(ActionProperty<Type> property, TypeCaster<SourceType, Type> caster) {
        this.property = property;
        this.caster = caster;
    }
    
    public void setValue(SourceType value) {
        property.setValue(caster.castOut(value));
    }

}
