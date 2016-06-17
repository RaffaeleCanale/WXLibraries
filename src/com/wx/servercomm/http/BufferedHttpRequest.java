package com.wx.servercomm.http;

import com.wx.util.log.LogHelper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Wrapper of an {@link AbstractHttpRequest} that will buffer every request in memory for a given time period.
 * <p>
 * <b>Note: </b> the buffer always starts of empty.
 * <p>
 * Created on 08/10/2015
 *
 * @author Raffaele Canale (raffaelecanale@gmail.com)
 * @version 0.1
 */
public class BufferedHttpRequest extends AbstractHttpRequest {

    private static final Logger LOG = LogHelper.getLogger(BufferedHttpRequest.class);

    private final Map<String, ServerResponse<byte[]>> buffer = new HashMap<>();
    private final AbstractHttpRequest httpGet;
    private final long validityPeriod;

    private int responseCode;

    /**
     * Initialize a new buffered {@code HTTP Get}.
     * <p>
     * Each buffered page is considered valid for a given period of time. When an invalid page is requested, the
     * buffered page is dropped and a new request is performed. <br> Alternatively, a period of 0 can be set to indicate
     * that the buffered pages should stay permanently.
     * <p>
     * <p>
     * <b>Note:</b> When a buffered page ultimately ends its validity period, it actually stays in the buffer (memory)
     * until the next request to the same url. To clear all invalid pages from the buffer, use the {@link
     * #clearInvalid()} method.
     *
     * @param httpGet        Underlying {@link AbstractHttpRequest} used to perform the actual requests
     * @param validityPeriod Period (in milliseconds) during which a buffered page is considered valid
     */
    public BufferedHttpRequest(AbstractHttpRequest httpGet, long validityPeriod) {
        this.httpGet = httpGet;
        this.validityPeriod = validityPeriod;
    }

    @Override
    public byte[] execute(String url) throws IOException {
        ServerResponse<byte[]> cached = buffer.get(url);

        if (cached != null && cached.isValid(validityPeriod)) {
            LOG.finest("\t\tBUFFER HIT " + url);

            responseCode = cached.getResponseCode();
            return cached.getData();
        }

        return forceExecute(url);
    }

    /**
     * Executes the request by ignoring (and updating) the buffer.
     *
     * @param url Url to execute
     *
     * @return Server's response
     *
     * @see #execute(String)
     */
    public byte[] forceExecute(String url) throws IOException {
        byte[] data = httpGet.execute(url);
        buffer.put(url, new ServerResponse<>(data, httpGet.getResponseCode()));

        responseCode = httpGet.getResponseCode();
        return data;
    }

    @Override
    public int getResponseCode() {
        return responseCode;
    }

    /**
     * Clear all invalid pages from the buffer
     */
    public void clearInvalid() {
        buffer.values().removeIf(r -> !r.isValid(validityPeriod));
    }

    @Override
    public void interruptExecute() {
        httpGet.interruptExecute();
    }
}
