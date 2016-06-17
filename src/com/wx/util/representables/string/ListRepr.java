package com.wx.util.representables.string;

import com.wx.util.representables.DelimiterEncoder;
import com.wx.util.representables.TypeCaster;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 *
 * @author Raffaele
 */
public class ListRepr<E> implements TypeCaster<String, List<E>> {

    static <E, F> List<F> cast(List<E> values, Function<E, F> cast) {
        return values.stream().map(cast).collect(Collectors.toList());
    }

    public static ListRepr<String> stringRepr(String separator) {
        return new ListRepr<>(new StringRepr(), separator);
    }

    public static ListRepr<String> stringRepr() {
        return new ListRepr<>(new StringRepr());
    }

    private final TypeCaster<String, E> subCaster;
    private final String separator;

    public ListRepr(TypeCaster<String, E> subCaster) {
        this(subCaster, null);
    }

    /**
     * Creates a new ListRepr
     * @param subCaster Representable for the elements of the list
     * @param separator Separator of the elements
     */
    public ListRepr(TypeCaster<String, E> subCaster, String separator) {
        this.separator = separator;
        this.subCaster = Objects.requireNonNull(subCaster);
    }
    
    @Override
    public String castIn(List<E> list) {
        List<String> castedList = cast(list, subCaster::castIn);

        if (separator == null) {
            return DelimiterEncoder.autoEncode(castedList);
        } else {
            return DelimiterEncoder.encode(separator, castedList);
        }
    }

    @Override
    public List<E> castOut(String value) {
        Objects.requireNonNull(value);
        List<String> values;
        if (separator == null) {
            values = DelimiterEncoder.autoDecode(value);
        } else {
            values = DelimiterEncoder.decode(separator, value);
        }

        return cast(values, subCaster::castOut);
    }
    
}
