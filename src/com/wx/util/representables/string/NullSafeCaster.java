package com.wx.util.representables.string;

import com.wx.util.representables.TypeCaster;

/**
 *  This is class wraps another caster ensuring that no {@code null} value
 * is ever passed to the sub caster (but directly returns null instead).
 * 
 * This can be useful if you want to enforce a caster that does not support null
 * values to support them.
 * 
 * @author Raffaele Canale - raffaelecanale@gmail.com
 * @version 1.0
 *
 * Project: WXLibraries.ANT
 * File: NullSafeCaster.java (UTF-8)
 * Date: Oct 8, 2013 
 */
public class NullSafeCaster<InType, OutType> implements TypeCaster<InType, OutType> {
    
    private final TypeCaster<InType, OutType> subCaster;

    public NullSafeCaster(TypeCaster<InType, OutType> subCaster) {
        this.subCaster = subCaster;
    }

    @Override
    public InType castIn(OutType value) throws ClassCastException {
        return value == null ? null : subCaster.castIn(value);
    }

    @Override
    public OutType castOut(InType value) throws ClassCastException {
        return value == null ? null : subCaster.castOut(value);
    }

}
