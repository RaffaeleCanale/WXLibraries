package com.wx.servercomm;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created on 16/11/2015
 *
 * @author Raffaele Canale (raffaelecanale@gmail.com)
 * @version 0.1
 */
public class URLBuilder {

    private final StringBuilder url;
    private boolean firstParameterAdded = false;

    public URLBuilder(String baseUrl) {
        this.url = new StringBuilder(baseUrl);
    }

    public URLBuilder addParameter(String parameterName, String parameterValue) throws UnsupportedEncodingException {
        char separator = firstParameterAdded ? '&' : '?';
        addParameter(separator, parameterName, parameterValue);
        firstParameterAdded = true;

        return this;
    }

    public String build() {
        return url.toString();
    }

    @Override
    public String toString() {
        return build();
    }

    private void addParameter(char separator, String parameterName, String parameterValue)
            throws UnsupportedEncodingException {
        url.append(separator).append(parameterName).append("=").
                append(URLEncoder.encode(parameterValue, "UTF-8"));
    }

}
