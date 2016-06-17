package com.wx.lexer.tokens;

import com.wx.lexer.Position;

/**
 * @author Canale
 */
public class StringToken extends Token {
    private final String value;

    /**
     * Construct a new string literal token.
     *
     * @param value    This token's value
     * @param position This token's position
     */
    public StringToken(String value, Position position) {
        super(position);
        assert value != null;
        this.value = value;
    }

    public StringToken(String value) {
        this(value, null);
    }

    @Override
    public String toString() {
        return "\"" + value + "\"";
    }

    @Override
    public String getStringValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        StringToken that = (StringToken) o;

        return value.equals(that.value);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + value.hashCode();
        return result;
    }
}
