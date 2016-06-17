package com.wx.servercomm.http;

import com.wx.util.pair.Pair;
import com.wx.util.representables.TypeCaster;
import com.wx.util.representables.string.IntRepr;
import com.wx.util.representables.string.LongRepr;
import com.wx.util.representables.string.PairRepr;
import com.wx.util.representables.string.StringRepr;

/**
 * Created on 08/10/2015
 *
 * @author Raffaele Canale (raffaelecanale@gmail.com)
 * @version 0.1
 */
class ServerResponse<ResponseType> {

    public static <R> ServerResponse<R> fromString(String value, TypeCaster<String, R> responseCaster) {
        PairRepr<Integer, Long> numbersRepr = new PairRepr<>(new IntRepr(), new LongRepr(), "/");


        Pair<R, String> outerPair = new PairRepr<>(responseCaster, new StringRepr())
                .castOut(value);
        Pair<Integer, Long> numbers = numbersRepr.castOut(outerPair.get2());
        return new ServerResponse<>(outerPair.get1(), numbers.get1(), numbers.get2());
    }

    public static <R> String toString(ServerResponse<R> response, TypeCaster<String, R> responseCaster) {
        PairRepr<Integer, Long> numbersRepr = new PairRepr<>(new IntRepr(), new LongRepr(), "/");

        String numbers = numbersRepr.castIn(response.responseCode, response.timestamp);

        PairRepr<R, String> pair = new PairRepr<>(responseCaster, new StringRepr());
        return pair.castIn(response.data, numbers);
    }

    private final ResponseType data;
    private final int responseCode;
    private final long timestamp;


    public ServerResponse(ResponseType data, int responseCode) {
        this(data, responseCode, System.currentTimeMillis());
    }

    public ServerResponse(ResponseType data, int responseCode, long timestamp) {
        this.data = data;
        this.responseCode = responseCode;
        this.timestamp = timestamp;
    }

    public boolean isValid(long validityPeriod) {
        return validityPeriod <= 0 || System.currentTimeMillis() <= timestamp + validityPeriod;
    }

    public ResponseType getData() {
        return data;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public int getResponseCode() {
        return responseCode;
    }
}
