package com.wx.grammar;

import com.wx.lexer.tokens.Token;

/**
 * Created on 20/11/2014
 *
 * @author Raffaele Canale (raffaelecanale@gmail.com)
 * @version 0.1
 */
public class ParseException extends Exception {

    public ParseException(Token expected, Token found) {
        super(found.getPosition() + ": Expected " + expected + ", but found " + found);
    }

    public ParseException(String message) {
        super(message);
    }
}
