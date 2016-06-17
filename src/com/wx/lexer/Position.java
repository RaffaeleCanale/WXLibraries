package com.wx.lexer;

/**
 * @author Canale
 */
public final class Position {
    private final int lineNo;
    private final int charNo;

    public Position(int lineNo, int charNo) {
        this.lineNo = lineNo;
        this.charNo = charNo;
    }

    public int getLineNo() {
        return lineNo;
    }

    public int getCharNo() {
        return charNo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Position position = (Position) o;

        return charNo == position.charNo && lineNo == position.lineNo;
    }

    @Override
    public int hashCode() {
        int result = lineNo;
        result = 31 * result + charNo;
        return result;
    }

    @Override
    public String toString() {
        return lineNo + ":" + charNo;
    }
}
