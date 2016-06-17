package com.wx.lexer.tokens;

import com.wx.lexer.Position;

/**
 * This token represents a unique key word.
 *
 * @author Raffaele Canale (raffaelecanale@gmail.com)
 * @version 2.0
 */
public class KeyWordToken extends Token {

    private final String name;

    /**
     * Build a new key word token.
     * <p>
     * Note that a key word name is NOT a value but, instead, a unique definition of its type.
     *
     * @param name     Key word name
     * @param position This token's position
     */
    public KeyWordToken(String name, Position position) {
        super(position);
        assert name != null;
        this.name = name;
    }

    public KeyWordToken(String name) {
        this(name, null);
    }

    @Override
    public String getStringValue() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean compareTokenType(Token other) {
        if (other == null || getClass() != other.getClass()) return false;

        return name.equals(other.getStringValue());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        KeyWordToken that = (KeyWordToken) o;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + name.hashCode();
        return result;
    }
}
