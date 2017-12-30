package com.wx.util.concurrent;

import java.util.concurrent.Callable;
import java.util.function.Consumer;

/**
 * @author Raffaele Canale (<a href="mailto:raffaelecanale@gmail.com?subject=InvoiceFX">raffaelecanale@gmail.com</a>)
 * @version 0.1 - created on 26.06.17.
 */
public class ConcurrentUtil {

    public static final Callback<?> NO_OP = (Callback<Object>) o -> null;

    public interface ExRunnable {
        void run() throws Exception;
    }

    public static <R> Thread executeAsync(Consumer<Callback<R>> process, Callback<R> callback) {
        Thread thread = new Thread(() -> {
            process.accept(callback);
        });
        thread.start();
        return thread;
    }

    public static <R> Thread executeAsync(Callable<R> process, Callback<R> callback) {
        Thread thread = new Thread(() -> {
            try {
                R result = process.call();
                if (callback != null) {
                    callback.success(result);
                }
            } catch (Exception e) {
                if (callback != null) {
                    callback.failure(e);
                }
            }
        });
        thread.start();
        return thread;
    }

    public static Thread executeAsync(ExRunnable process, Callback<?> callback) {
        return executeAsync(() -> {
            process.run();
            return null;
        }, callback);
    }

}
