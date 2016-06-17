package com.wx.grammar;

/**
 *
 * @author Raffaele Canale (raffaelecanale@gmail.com)
 */
public class GrammarException extends Exception {

    /**
     * Creates a new instance of <code>GrammerException</code> without detail
     * message.
     */
    public GrammarException() {
    }

    /**
     * Constructs an instance of <code>GrammerException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public GrammarException(String msg) {
        super(msg);
    }
}
