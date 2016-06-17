package com.wx.servercomm.http;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * Abstract representation of an HTTP Get request
 *
 * @author Raffaele Canale - raffaelecanale@gmail.com
 * @version 1.0
 *          <p>
 *          Project: Worx File: HttpGetInterface.java (UTF-8) Date: May 12, 2013
 */
public abstract class AbstractHttpRequest {

    /**
     * Execute the GET request and read the result as a {@code String}.
     *
     * @param url Url to execute
     *
     * @return The resulting page in a string
     *
     * @throws IOException
     */
    public String executeAndRead(String url) throws IOException {
        byte[] response = execute(url);
        return response == null ? null : new String(response, StandardCharsets.UTF_8);
    }

    public abstract int getResponseCode();

    /**
     * Execute an HTTP GET request at the given url.
     *
     * @param url Url to execute
     *
     * @return The server's response
     *
     * @throws IOException
     */
    public abstract byte[] execute(String url) throws IOException;


    public abstract void interruptExecute();

    /**
     * Execute an HTTP GET request at the given url and return the response as an {@code InputStream}.
     * <p>
     * <b>Note: </b> This method does not directly return the {@code Socket InputStream}. Instead, it reads the entire
     * response and the returns an {@code InputStream} based on the read bytes.
     *
     * @param url Url to execute
     *
     * @return The server's response as a stream
     *
     * @throws IOException
     */
    public InputStream executeAsStream(String url) throws IOException {
        byte[] response = execute(url);
        return response == null ? null : new ByteArrayInputStream(response);
    }

}
