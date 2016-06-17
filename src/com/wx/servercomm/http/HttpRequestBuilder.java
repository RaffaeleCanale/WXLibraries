package com.wx.servercomm.http;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

/**
 * Created on 16/11/2015
 *
 * @author Raffaele Canale (raffaelecanale@gmail.com)
 * @version 0.1
 */
public class HttpRequestBuilder {

    private AbstractHttpRequest request;

    public HttpRequestBuilder() {
        this(HttpRequest.createGET());
    }

    public HttpRequestBuilder(AbstractHttpRequest underlyingRequest) {
        this.request = Objects.requireNonNull(underlyingRequest);
    }


    public HttpRequestBuilder buffered(long validity) {
        checkValidity(validity);
        request = new BufferedHttpRequest(request, validity);

        return this;
    }

    public HttpRequestBuilder cached(File cacheFile, long validity) throws IOException {
        checkValidity(validity);
        request = new CachedHttpRequest(request, cacheFile, validity);

        return this;
    }

    public HttpRequestBuilder cachedSafe(File cacheFile, long validity) {
        checkValidity(validity);
        request = new SafeCachedHttpRequest(request, cacheFile, validity);

        return this;
    }

    public AbstractHttpRequest get() {
        return request;
    }

    private void checkValidity(long validity) {
        if (validity < 0) {
            throw new IllegalArgumentException("Negative validity");
        }
    }



}
