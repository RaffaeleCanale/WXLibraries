/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wx.servercomm.http;

import com.wx.io.Accessor;
import com.wx.util.log.LogHelper;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.*;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.Objects;
import java.util.logging.Logger;

/**
 * Implementation of an HTTP Get request. This class opens single use connections. Thus, every
 *
 * @author Raffaele Canale - raffaelecanale@gmail.com
 * @version 0.1
 *          <p>
 *          Project: Worx File: HttpGet.java (UTF-8) Date: May 11, 2013
 */
public class HttpRequest extends AbstractHttpRequest {

//    public static void main(String[] args) throws IOException {
//        byte[] execute = createGET().execute("http://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml");
//        System.out.println(new String(execute));
//    }

    public static HttpRequest createPOST() {
        return new HttpRequest(new HttpPost());
    }

    public static HttpRequest createGET() {
        return new HttpRequest(new HttpGet());
    }

    public static HttpRequest createDELETE() {
        return new HttpRequest(new HttpDelete());
    }

    private static final Logger LOG = LogHelper.getLogger(HttpRequest.class);

    private final HttpRequestBase request;
    private boolean aborted;
    private int responseCode;


    private HttpRequest(HttpRequestBase request) {
        this.request = Objects.requireNonNull(request);
    }

    public HttpRequest setUserAgent(String agent) {
        request.addHeader("User-Agent", agent);
        return this;
    }

    public HttpRequest setTimeout(int timeout) {
        request.setConfig(RequestConfig.custom()
                .setConnectTimeout(timeout)
                .setSocketTimeout(timeout)
                .setConnectionRequestTimeout(timeout)
                .build());
        return this;
    }

    public HttpRequest setBasicAuthorization(String user, String password) {
        String encoding = Base64.getEncoder().encodeToString((user + ":" + password).getBytes(StandardCharsets.UTF_8));
        request.setHeader("Authorization", "Basic " + encoding);

        return this;
    }

    public synchronized InputStream executeDirect(String url) throws IOException {
        aborted = false;

        LOG.finest("\t" + request.getMethod() + " " + url);
        request.setURI(URI.create(url));

        HttpClient client = HttpClientBuilder.create().build();
        checkAborted();

        HttpResponse response = client.execute(request);

        checkAborted();

        this.responseCode = response.getStatusLine().getStatusCode();

        HttpEntity entity = response.getEntity();
        return entity == null ? null : entity.getContent();
    }

    @Override
    public synchronized byte[] execute(String url) throws IOException {
        return read(executeDirect(url));
    }

    private void checkAborted() throws IOException {
        if (aborted) {
            throw new IOException("Aborted");
        }
    }

    @Override
    public int getResponseCode() {
        return responseCode;
    }

    @Override
    public void interruptExecute() {
        aborted = true;
        request.abort();
    }

    private byte[] read(InputStream in) throws IOException {
        if (in == null) {
            return null;
        }

        try (ByteArrayOutputStream bytes = new ByteArrayOutputStream();
             Accessor accessor = new Accessor().setIn(in).setOut(bytes)) {
            accessor.pourInOut();

            return bytes.toByteArray();
        }
    }
}
