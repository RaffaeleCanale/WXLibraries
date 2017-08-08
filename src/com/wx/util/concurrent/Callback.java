package com.wx.util.concurrent;

/**
 * @author Raffaele Canale (<a href="mailto:raffaelecanale@gmail.com?subject=WXLibraries">raffaelecanale@gmail.com</a>)
 * @version 0.1 - created on 08.08.17.
 */
public interface Callback<R> {

    Void success(R result);

    default Void failure(Throwable error) {
        return null;
    }

    default Void cancelled() {
        return failure(new CancelledException("cancelled"));
    }

    default Callback<R> then(Callback<R> next) {
        return new Callback<R>() {
            @Override
            public Void success(R result) {
                Callback.this.success(result);
                return next.success(result);
            }

            @Override
            public Void failure(Throwable ex) {
                Callback.this.failure(ex);
                return next.failure(ex);
            }

            @Override
            public Void cancelled() {
                Callback.this.cancelled();
                return next.cancelled();
            }
        };
    }
}
