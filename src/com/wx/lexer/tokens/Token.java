package com.wx.lexer.tokens;

import com.wx.lexer.Position;

import java.util.Objects;


public abstract class Token {

    //    private final TokenKind kind;
    private final Position position;

    /**
     * Abstract token constructor
     *
     * @param position Token's position
     */
    Token(Position position) {
//        this.kind = kind;
        this.position = position;
    }

    /**
     * Get this token's position
     *
     * @return This token's position
     */
    public Position getPosition() {
        return position;
    }

//    public TokenKind getKind() {
//        return kind;
//    }

    /**
     * Get a string representation of this token's value. Tokens do not necessarily implement this method.
     *
     * @return This token value
     */
    public String getStringValue() {
        throw new UnsupportedOperationException("This token has no string value: " + this);
    }

    public double getDoubleValue() {
        throw new UnsupportedOperationException("This token has no double value: " + this);
    }

    public char getCharValue() {
        throw new UnsupportedOperationException("This token has no char value: " + this);
    }

    /**
     * Compare two token's type. This method will not consider any token value and only compare the tokens types.
     *
     * @param other Token to compare
     *
     * @return {@code true} if the given token is of the same type
     */
    public boolean compareTokenType(Token other) {
        return getClass() == Objects.requireNonNull(other).getClass();
    }

    public String formatted() {
        final String name = getClass().getName();
        return name.substring(name.lastIndexOf('.')) + "[" + toString() + "](" + position + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Token token = (Token) o;
        return position != null ? position.equals(token.position) : token.position == null;

    }

    @Override
    public int hashCode() {
        return position != null ? position.hashCode() : 0;
    }
}
