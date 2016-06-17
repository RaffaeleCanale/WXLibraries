package com.wx.lexer.tokens;

import com.wx.lexer.Position;

/**
 * @author Canale
 * @version 2.0
 */
public class SpecialCharToken extends Token {

    private final char value;

    /**
     * Constructs a new special character token.
     *
     * @param value This token's value
     */
    public SpecialCharToken(char value) {
        this(value, null);
    }


    /**
     * Constructs a new special character token.
     *
     * @param value    This token's value
     * @param position This token's position
     */
    public SpecialCharToken(char value, Position position) {
        super(position);
        this.value = value;
    }

    @Override
    public char getCharValue() {
        return value;
    }

    @Override
    public String getStringValue() {
        return String.valueOf(value);
    }

    @Override
    public String toString() {
        return "'" + value + "'";
    }

    @Override
    public boolean compareTokenType(Token other) {
        if (other == null || getClass() != other.getClass()) return false;

        return value == other.getCharValue();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        SpecialCharToken that = (SpecialCharToken) o;

        return value == that.value;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (int) value;
        return result;
    }
}
