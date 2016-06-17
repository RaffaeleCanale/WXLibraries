package com.wx.lexer.tokens;

import com.wx.lexer.Position;

/**
 * @author Canale
 * @version 2.0
 */
public class NumberToken extends Token {

    private final double value;

    /**
     * Construct a new number token.
     *
     * @param value    Token's value
     * @param position Token's position
     */
    public NumberToken(double value, Position position) {
        super(position);
        this.value = value;
    }

    public NumberToken(double value) {
        this(value, null);
    }

    @Override
    public double getDoubleValue() {
        return value;
    }

    @Override
    public String getStringValue() {
        return "" + value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        NumberToken that = (NumberToken) o;

        return Double.compare(that.value, value) == 0;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        long temp;
        temp = Double.doubleToLongBits(value);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
