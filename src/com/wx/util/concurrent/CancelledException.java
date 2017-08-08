package com.wx.util.concurrent;

/**
 * @author Raffaele Canale (<a href="mailto:raffaelecanale@gmail.com?subject=WXLibraries">raffaelecanale@gmail.com</a>)
 * @version 0.1 - created on 08.08.17.
 */
public class CancelledException extends Exception {
    public CancelledException() {
    }

    public CancelledException(String message) {
        super(message);
    }

    public CancelledException(String message, Throwable cause) {
        super(message, cause);
    }

    public CancelledException(Throwable cause) {
        super(cause);
    }

    public CancelledException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
