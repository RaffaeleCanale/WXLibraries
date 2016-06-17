package com.wx.util.representables;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This utility class allows to encode lists into a single string.
 * <p>
 * The list is encoded using separators between the elements. The separator can be either provided or automatically
 * computed such that the separation is not ambiguous.
 * <p>
 * Furthermore, empty strings or null values in the list will be correctly reconstructed when decoding.
 * <p>
 * Created on 19/03/2015
 *
 * @author Raffaele Canale (raffaelecanale@gmail.com)
 * @version 1.0
 */
public class DelimiterEncoder {

    private static final String[] DEFAULT_DELIMITERS = {":", "/", "_", "#", "::", ":::", "//"};
    public static final String NULL_TAG = "NULL";
    public static final String EMPTY_TAG = "EMPTY";

    /**
     * Tests if the given delimiter is able to separate all the given values without ambiguity.
     *
     * @param delimiter Delimiter to test
     * @param values    Values that need to be separated by the delimiter
     *
     * @return {@code true} if the given delimiter can unambiguously separate all the values
     */
    public static boolean isDelimiterSuitable(String delimiter, Iterable<String> values) {
        for (String value : values) {
            if (value != null && !value.isEmpty() &&
                    (value.contains(delimiter) || delimiter.contains(value))) {
                return false;
            }
        }

        return true;
    }

    /**
     * Find the first suitable delimiter for these values from the given set of delimiters or throws an exception if
     * none is found.
     *
     * @param values     Values to separate
     * @param delimiters Set of delimiters to use
     *
     * @return The first delimiter from the given array that is suitable for the given values
     *
     * @throws NoSuchElementException if none of these delimiters is suitable
     * @see #isDelimiterSuitable(String, Iterable)
     */
    public static String findSuitableDelimiter(Collection<String> values, String[] delimiters) {
        for (String delimiter : delimiters) {
            if (isDelimiterSuitable(delimiter, values)) {
                return delimiter;
            }
        }

        throw new NoSuchElementException("Could not find a suitable delimiter for the following set:\n" + values);
    }

    /**
     * Encode the given list into a string with one of the default delimiters.
     * <p>
     * The encoding takes into account empty or null values that may be present in the list such that they are correctly
     * reconstructed when decoding.
     * <p>
     * The delimiter is chosen such that the values can be encoded unambiguously. Furthermore, the delimiter is also
     * encoded in the output string such that it can be retrieved when decoding with {@link #autoDecode(String)}.
     *
     * @param values List to encode
     *
     * @return Encoded representation of the given list
     *
     * @throws NoSuchElementException if no suitable delimiter was found to encode this list
     * @see #autoDecode(String)
     */
    public static String autoEncode(Collection<String> values) {
        return autoEncode(values, DEFAULT_DELIMITERS);
    }

    /**
     * Encode the given list into a string with one of the given delimiters.
     * <p>
     * The encoding takes into account empty or null values that may be present in the list such that they are correctly
     * reconstructed when decoding.
     * <p>
     * The delimiter is chosen from the given array such that the values can be encoded unambiguously. Furthermore, the
     * delimiter is also encoded in the output string such that it can be retrieved when decoding with {@link
     * #autoDecode(String)}.
     *
     * @param values     List to encode
     * @param delimiters Delimiters to choose from
     *
     * @return Encoded representation of the given list
     *
     * @throws NoSuchElementException if no suitable delimiter was found to encode this list
     * @see #autoDecode(String)
     */
    public static String autoEncode(Collection<String> values, String[] delimiters) {
        String delimiter = findSuitableDelimiter(values, delimiters);
        if (delimiter.contains("]")) {
            throw new IllegalArgumentException("Delimiters cannot contain ']'");
        }
        return "[" + delimiter + "] " + encode(delimiter, values);
    }

    /**
     * Encode the given list into a string with the given delimiter.
     * <p>
     * The encoding takes into account empty or null values that may be present in the list such that they are correctly
     * reconstructed when decoding.
     * <p>
     * When decoding using {@link #decode(String, String)}, the same delimiter must be provided to reconstruct the
     * correct list.
     *
     * @param delimiter Delimiter to use to separate the values
     * @param values    List to encode
     *
     * @return Encoded representation of the given list
     *
     * @throws IllegalArgumentException if the given delimiter is not suitable for these values
     * @see #decode(String, String)
     * @see #isDelimiterSuitable(String, Iterable)
     */
    public static String encode(String delimiter, Collection<String> values) {
        return encode(delimiter, values.stream());
    }

    /**
     * Encode the given list into a string with the given delimiter. This method will consume the given stream.
     * <p>
     * The encoding takes into account empty or null values that may be present in the list such that they are correctly
     * reconstructed when decoding.
     * <p>
     * When decoding using {@link #decode(String, String)}, the same delimiter must be provided to reconstruct the
     * correct list.
     *
     * @param delimiter Delimiter to use to separate the values
     * @param values    List to encode
     *
     * @return Encoded representation of the given list
     *
     * @throws IllegalArgumentException if the given delimiter is not suitable for these values
     * @see #decode(String, String)
     * @see #isDelimiterSuitable(String, Iterable)
     */
    public static String encode(String delimiter, Stream<String> values) {
        Objects.requireNonNull(delimiter);
        return values.map(v -> encodeSingleValue(delimiter, v)).collect(Collectors.joining(delimiter));
    }

    private static String encodeSingleValue(String delimiter, String value) {
        if (value == null) {
            return delimiter + NULL_TAG;
        } else if (value.isEmpty()) {
            return delimiter + EMPTY_TAG;
        } else {
            if (value.contains(delimiter)) {
                throw new IllegalArgumentException("Value " + value + " contains the separator");
            } else if (value.contains(delimiter)) {
                throw new IllegalArgumentException("Value " + value + " is a subset of the separator");
            }
            return value;
        }
    }

    /**
     * Decode the given string into a list of values. The given string must have a delimiter encoded by {@link
     * #autoEncode(Collection)}.
     *
     * @param value Value to decode
     *
     * @return The list of elements encoded in the given string
     *
     * @throws NoSuchElementException if the given string does not have a delimiter encoded in it
     */
    public static List<String> autoDecode(String value) {
        int closingBracketIndex = value.indexOf(']');

        if (value.charAt(0) != '[' || closingBracketIndex < 2 || value.length() < 4) {
            throw new NoSuchElementException("No delimiter encoded in '" + value + "'");
        }

        String delimiter = value.substring(1, closingBracketIndex);
        String content = value.substring(closingBracketIndex + 2);

        return decode(delimiter, content);
    }

    /**
     * Decode the given string into a list of values using the given delimiter.
     *
     * @param delimiter Delimiter separating the values in the encoded string
     * @param plainValue String to decode
     *
     * @return The list of elements encoded in the given string
     */
    public static List<String> decode(String delimiter, String plainValue) {
        List<String> result = new ArrayList<>();
        String[] values = plainValue.split(Pattern.quote(delimiter));

        boolean expectsTag = false;
        int totalLength = 0;

        for (String element : values) {
            totalLength += element.length();

            if (expectsTag) {
                switch (element) {
                    case NULL_TAG:
                        result.add(null);
                        expectsTag = false;
                        break;

                    case EMPTY_TAG:
                        result.add("");
                        expectsTag = false;
                        break;

                    default:
                        throw new ClassCastException("Malformed value");
                }

            } else {    //Don't expect commands
                if (element.isEmpty()) {
                    expectsTag = true;
                } else {
                    result.add(element);
                }
            }
        }

        int expectedLength = plainValue.length() - delimiter.length() * (values.length - 1);
        if (totalLength != expectedLength) {    // Only possible if there were leading or trailing separators.
            throw new ClassCastException("Malformed value (leading or trailing separators): " + plainValue);
        }

        return result;
    }


    private DelimiterEncoder() {
    }
}
