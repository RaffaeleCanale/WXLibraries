package com.wx.lexer.tokens;

import com.wx.lexer.Position;

/**
 * This token represents and End Of File. This is token represents the end of the input.
 *
 * @author Raffaele Canale (raffaelecanale@gmail.com)
 * @version 2.0
 */
public class EOFToken extends Token {

    /**
     * Build a new end-of-file token.
     *
     * @param position This token's position
     */
    public EOFToken(Position position) {
        super(position);
    }

    public EOFToken() {
        super(null);
    }

    @Override
    public String toString() {
        return "[EOF]";
    }
}
