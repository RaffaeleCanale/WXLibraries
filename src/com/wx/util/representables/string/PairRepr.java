package com.wx.util.representables.string;

import com.wx.util.pair.Pair;
import com.wx.util.representables.DelimiterEncoder;
import com.wx.util.representables.TypeCaster;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;


/**
 *
 * @author Raffaele
 */
public class PairRepr<E, F> implements TypeCaster<String, Pair<E, F>> {

    public static PairRepr<String, String> stringPairRepr(String separator) {
        return pairRepr(new StringRepr(), separator);
    }

    public static PairRepr<String, String> stringPairRepr() {
        return pairRepr(new StringRepr());
    }

    public static <E> PairRepr<E, E> pairRepr(TypeCaster<String, E> caster, String separator) {
        return new PairRepr<>(caster, caster, separator);
    }

    public static <E> PairRepr<E, E> pairRepr(TypeCaster<String, E> caster) {
        return new PairRepr<>(caster, caster);
    }


    private final TypeCaster<String, E> reprE;
    private final TypeCaster<String, F> reprF;
    private final String separator;

    public PairRepr(TypeCaster<String, E> reprE, TypeCaster<String, F> reprF) {
        this(reprE, reprF, null);
    }

    public PairRepr(TypeCaster<String, E> reprE, TypeCaster<String, F> reprF, 
            String separator) {
        this.separator = separator;
        this.reprE = Objects.requireNonNull(reprE);
        this.reprF = Objects.requireNonNull(reprF);
    }

    @Override
    public String castIn(Pair<E, F> e) throws ClassCastException {
        return e.apply(this::castIn);
    }

    public String castIn(E e, F f) {
        List<String> values = Arrays.asList(reprE.castIn(e), reprF.castIn(f));
        return separator == null ? DelimiterEncoder.autoEncode(values)
                : DelimiterEncoder.encode(separator, values);

//        String firstElement = reprE.castIn(e);
//        String secondElement = reprF.castIn(f);
//        Objects.requireNonNull(firstElement);
//        Objects.requireNonNull(secondElement);



//        StringBuilder result = new StringBuilder();
//        String delimiter;
//
//
//        if (separator == null) {
//            delimiter = DelimiterEncoder.findSuitableDelimiter(Collections.singletonList(firstElement));
//            result.append("[").append(delimiter).append("] ");
//
//        } else {
//            delimiter = separator;
//            if (!DelimiterEncoder.isDelimiterSuitable(delimiter, Collections.singletonList(firstElement))) {
//                throw new IllegalArgumentException("Value " + firstElement + " is not suitable for delimiter " + delimiter);
//            }
//        }
//
//        return result.append(firstElement).append(delimiter).append(secondElement).toString();
    }

    @Override
    public Pair<E, F> castOut(String value) throws ClassCastException {
        return castOut(value, Pair::new);
    }

    public <G> G castOut(String value, BiFunction<E, F, G> function) {

        List<String> values = separator == null ? DelimiterEncoder.autoDecode(value)
                : DelimiterEncoder.decode(separator, value);

        if (values.size() != 2) {
            throw new ClassCastException("Does not contain a pair: " + value);
        }

        Iterator<String> it = values.iterator();
        return function.apply(reprE.castOut(it.next()), reprF.castOut(it.next()));

//        String delimiter;
//        if (separator == null) {
//            int closingBracketIndex = value.indexOf(']');
//            if (value.length() < 3 || value.charAt(0) != '[' || closingBracketIndex < 0) {
//                throw new IllegalArgumentException("No delimiter encoded in '" + value + "'");
//            }
//
//            delimiter = value.substring(1, closingBracketIndex);
//            value = value.substring(closingBracketIndex + 2);
//
//        } else {
//            delimiter = separator;
//        }
//
//        int delimiterStart = value.indexOf(delimiter);
//        if (delimiterStart < 0) {
//            throw new ClassCastException("No delimiter found");
//        }
//        int delimiterEnd = delimiterStart + delimiter.length();
//
//        E e = reprE.castOut(delimiterStart == 0 ? "" : value.substring(0, delimiterStart));
//        F f = reprF.castOut(delimiterEnd == value.length() ? "" : value.substring(delimiterEnd));
//        return function.apply(e, f);
    }

    public <T> TypeCaster<String, T> morph(BiFunction<E, F, T> out, Function<T, E> inE, Function<T, F> inF) {
        return new TypeCaster<String, T>() {
            @Override
            public String castIn(T value) throws ClassCastException {
                return PairRepr.this.castIn(inE.apply(value), inF.apply(value));
            }

            @Override
            public T castOut(String value) throws ClassCastException {
                return PairRepr.this.castOut(value, out);
            }
        };
    }
}
