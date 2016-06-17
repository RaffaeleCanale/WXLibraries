package com.wx.util.representables;

import com.wx.util.representables.string.NullSafeCaster;

/**
 * This interface allows to cast any type into any other and vice-versa.
 * <p>
 * Note that most implementation do not allow to cast {@code null} values. If that is needed, any caster can be enforced
 * to cast {@code null} values by using {@link #nullable()}.
 *
 * @param <InType>  First type
 * @param <OutType> Second type
 *
 * @author Raffaele Canale - raffaelecanale@gmail.com
 * @version 3.0
 *          <p>
 *          Date: May 13, 2013
 */
@SuppressWarnings("ProhibitedExceptionDeclared")
public interface TypeCaster<InType, OutType> {

    /**
     * Cast an element into the {@code F} type.
     *
     * @param value Element to cast
     *
     * @return A {@code F} representation of the element
     *
     * @throws ClassCastException
     */
    InType castIn(OutType value) throws ClassCastException;

    /**
     * Cast an element into the {@code E} type.
     *
     * @param value {@code F} to cast
     *
     * @return A {@code E} representation of the element
     *
     * @throws ClassCastException
     */
    OutType castOut(InType value) throws ClassCastException;


    /**
     * Creates a wrapper of this caster that allows to cast {@code null} values. Casting a {@code null} value (in any
     * direction) will always yield a {@code null} value.
     * <p>
     * <b>Note:</b> Some casters already allow to cast {@code null} values, in such case, this method will return the
     * same caster.
     *
     * @return The same caster enhanced with the possibility of casting {@code null}
     */
    default TypeCaster<InType, OutType> nullable() {
        return new NullSafeCaster<>(this);
    }

}
