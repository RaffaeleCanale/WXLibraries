package com.wx.lexer;

import com.wx.util.pair.Pair;
import java.io.InputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Simple builder to create a new {@link Lexer}. <br><br>
 *
 * Information needed by the {@code Lexer} are:
 * <ul>
 * <li>Separators: Set of characters that must be considered as word separator
 * (commonly a space)</li>
 * <li>Specials: Set of specials characters that must be considered as
 * tokens</li>
 * <li>Comments: (optional) comments marker (single or multi line)</li>
 * </ul>
 *
 * @author Raffaele Canale (raffaelecanale@gmail.com)
 */
public class LexerBuilder {

    private String singleLineCommentsMarker;
    private Pair<String, String> multiLineCommentsMarker;
    private final Set<Character> separators;
    private final Set<Character> specials;
    private final Set<String> keyWords;
    private boolean ignoreEol = false;
    private boolean keyWordsIgnoreCase = false;
    private boolean ignoreNumericLitterals = false;

    /**
     * Constructs a new {@code LexerBuilder}
     */
    public LexerBuilder() {
        this.separators = new HashSet<>();
        this.specials = new HashSet<>();
        this.keyWords = new HashSet<>();
    }

    public LexerBuilder ignoreNumericLitterals() {
        this.ignoreNumericLitterals = true;
        return this;
    }

    /**
     * Set the marker for a line comment. <br>
     * Every character standing after this marker and before the end of line
     * will be ignored. <br><br>
     *
     * Only one single line marker can be set.
     *
     * @param marker Single line marker
     *
     * @return {@code this}
     */
    public LexerBuilder lineCommentsMarker(String marker) {
        assert singleLineCommentsMarker == null : "Marker already set: "
                + singleLineCommentsMarker;

        singleLineCommentsMarker = marker;
        return this;
    }

    /**
     * Set the markers for a multi line comment. <br>
     * Every character standing between the {@code begin} marker and {@code end}
     * marker will be ignored. <br><br>
     *
     * Only one tuple of multi line markers can be set.
     *
     * @param begin Begin marker
     * @param end   End marker
     *
     * @return {@code this}
     */
    public LexerBuilder multiCommentsMarkers(String begin, String end) {
        multiLineCommentsMarker = new Pair<>(begin, end);
        return this;
    }

    /**
     * Set the separators. (This method is additive)
     *
     * @param sep Separators
     *
     * @return {@code this}
     */
    public LexerBuilder separators(Set<Character> sep) {
        separators.addAll(sep);
        return this;
    }

    /**
     * Set the separators. (This method is additive)
     *
     * @param sep Separators
     *
     * @return {@code this}
     */
    public LexerBuilder separators(char... sep) {
        for (char s : sep) {
            separators.add(s);
        }

        return this;
    }
    
    public LexerBuilder separators(String sep) {
        for (char s : sep.toCharArray()) {
            separators.add(s);
        }

        return this;
    }

    /**
     * Set the special characters. (This method is additive)
     *
     * @param spe specials
     *
     * @return {@code this}
     */
    public LexerBuilder specials(Set<Character> spe) {
        specials.addAll(spe);
        return this;
    }

    /**
     * Set the special characters. (This method is additive)
     *
     * @param spe specials
     *
     * @return {@code this}
     */
    public LexerBuilder specials(char... spe) {
        for (char s : spe) {
            specials.add(s);
        }

        return this;
    }
    
    public LexerBuilder specials(String chars) {
        for (char s : chars.toCharArray()) {
            specials.add(s);
        }

        return this;
    }

    public LexerBuilder keyWords(String... values) {
        Collections.addAll(keyWords, values);

        return this;
    }

    public LexerBuilder keyWords(Collection<String > values) {
        keyWords.addAll(values);

        return this;
    }

    public LexerBuilder ignoreEOL() {
        this.ignoreEol = true;
        return this;
    }

    public LexerBuilder ignoreCaseForKeywords() {
        this.keyWordsIgnoreCase = true;
        return this;
    }

    /**
     * Build the {@code Lexer} from the given input.
     *
     * @param in Stream to parse
     *
     * @return Ready to use {@code Lexer}
     */
    public Lexer build(InputStream in) {
        return new Lexer(singleLineCommentsMarker, multiLineCommentsMarker,
                separators, specials, keyWords, ignoreEol, keyWordsIgnoreCase, ignoreNumericLitterals, in);
    }
}
