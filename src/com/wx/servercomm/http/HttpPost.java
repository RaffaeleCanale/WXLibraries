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
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.logging.Logger;

/**
 *
 * @author Raffaele Canale - raffaelecanale@gmail.com
 * @version 0.1
 *
 * Project: Worx File: HttpPost.java (UTF-8) Date: May 11, 2013
 */
public class HttpPost extends AbstractHttpRequest {

    private static final Logger LOG = LogHelper.getLogger(HttpPost.class);
    private static final int DEFAULT_TIME_OUT = 3000;

    private int responseCode;
    private String userAgent;
    private int timeout = DEFAULT_TIME_OUT;

    private org.apache.http.client.methods.HttpPost post;
    private boolean aborted;

    private String headerKey;
    private String headerValue;

    public HttpPost setUserAgent(String userAgent) {
        this.userAgent = userAgent;
        return this;
    }

    public HttpPost setTimeout(int timeout) {
        this.timeout = timeout;
        return this;
    }

    public HttpPost setBasicAuthorization(String user, String password) {
        this.headerKey = "Authorization";
        String encoding = Base64.getEncoder().encodeToString((user + ":" + password).getBytes(StandardCharsets.UTF_8));
        this.headerValue = "Basic " + encoding;

        return this;
    }

    @Override
    public synchronized byte[] execute(String url) throws IOException {
        return read(executeDirect(url));
    }

    public InputStream executeDirect(String url) throws IOException {
        if (headerKey == null) {
            throw new IllegalArgumentException("No header set");
        }

        HttpClient client = HttpClientBuilder.create().build();
        checkAborted();



        post = new org.apache.http.client.methods.HttpPost(url);
        checkAborted();
        // TODO Timeout not working (also for GET)
        post.setHeader(headerKey, headerValue);
        post.addHeader("User-Agent", userAgent);
        post.setConfig(RequestConfig.custom()
//                .setConnectTimeout(timeout)
//                .setSocketTimeout(timeout)
//                .setConnectionRequestTimeout(timeout)
                .build());


        LOG.finest("\tPOST " + url);

        HttpResponse response = client.execute(post);
        checkAborted();

        this.responseCode = response.getStatusLine().getStatusCode();
        HttpEntity entity = response.getEntity();
        return entity == null ? null : entity.getContent();
    }


    @Override
    public int getResponseCode() {
        return responseCode;
    }

    @Override
    public void interruptExecute() {
        aborted = true;

        if (post != null) {
            post.abort();
            post = null;
        }
    }

    private void checkAborted() throws IOException {
        if (aborted) {
            throw new IOException("Aborted");
        }
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

//    public static String excutePost(String targetURL, String urlParameters) throws IOException {
//        URL url;
//        HttpURLConnection connection = null;
//        try {
//            //Create connection
//            url = new URL(targetURL);
//
//            connection = (HttpURLConnection) url.openConnection();
//            connection.setRequestMethod("POST");
//            connection.setRequestProperty("Content-Type",
//                    "application/x-www-form-urlencoded");
//
//            connection.setRequestProperty("Content-Length", ""
//                    + Integer.toString(urlParameters.getBytes().length));
//            connection.setRequestProperty("Content-Language", "en-US");
//
//            connection.setUseCaches(false);
//            connection.setDoInput(true);
//            connection.setDoOutput(true);
//
//            //Send request
//            DataOutputStream wr = new DataOutputStream(
//                    connection.getOutputStream());
//            wr.writeBytes(urlParameters);
//            wr.flush();
//            wr.close();
//
//            //Get Response
//            InputStream is = connection.getInputStream();
//            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
//            String line;
//            StringBuilder response = new StringBuilder();
//            while ((line = rd.readLine()) != null) {
//                response.append(line);
//                response.append('\r');
//            }
//            rd.close();
//            return response.toString();
//
//
//
//        } finally {
//
//            if (connection != null) {
//                connection.disconnect();
//            }
//        }
//    }

}
