package com.wx.util.representables.string;

import com.wx.util.representables.TypeCaster;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * Similar to the {@link EnumCaster} but enforces the use of lower case in the {@code String} representation.
 * Thus, the enum constants name must be differentiable by more the letter cases.
 *
 * Created on 03/04/2015
 *
 * @author Raffaele Canale (raffaelecanale@gmail.com)
 * @version 0.1
 */
public class EnumCasterLC<E extends Enum<E>> implements TypeCaster<String, E> {

    private final Class<E> enumClass;

    public EnumCasterLC(Class<E> enumClass) {
        this.enumClass = Objects.requireNonNull(enumClass);

        checkEnumValidity();
    }

    @Deprecated
    public EnumCasterLC(E[] values) {
        Objects.requireNonNull(values);
        if (values.length == 0) {
            throw new IllegalArgumentException("Must have at least 1 enum");
        }

        enumClass = values[0].getDeclaringClass();  // Ugly, but for legacy purposes

        checkEnumValidity();
    }

    @Override
    public String castIn(E value) {
        return value.name().toLowerCase();
    }

    @Override
    public E castOut(String value) {
        Objects.requireNonNull(value);

        for (E e : enumClass.getEnumConstants()) {
            if (e.name().equalsIgnoreCase(value)) {
                return e;
            }
        }
        throw new ClassCastException("No match found for '" + value + "',"
                + " should be one of " + Stream.of(enumClass.getEnumConstants()).map(this::castIn).collect(Collectors.joining(", ")));
    }

    private void checkEnumValidity() {
        Set<String> uniqueNames = new HashSet<>();

        for (E e : enumClass.getEnumConstants()) {
            if (!uniqueNames.add(e.name().toLowerCase())) {
                throw new IllegalArgumentException("Some constants are only differentiable by the letter cases");
            }
        }
    }


}
