package com.wx.lexer.tokens;

import com.wx.lexer.Position;

/**
 * This token represents and end of line.
 *
 * @author Raffaele Canale (raffaelecanale@gmail.com)
 * @version 2.0
 */
public class EOLToken extends Token {

    /**
     * Build a new End-of-line token.
     *
     * @param position This token's position
     */
    public EOLToken(Position position) {
        super(position);
    }


    public EOLToken() {
        super(null);
    }

    @Override
    public String getStringValue() {
        return "\n";
    }

    @Override
    public char getCharValue() {
        return '\n';
    }

    @Override
    public String toString() {
        return "[EOL]";
    }
}
