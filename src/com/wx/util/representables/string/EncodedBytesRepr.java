package com.wx.util.representables.string;

import com.wx.crypto.CryptoUtil;
import com.wx.util.representables.TypeCaster;

import java.util.Objects;

/**
 * Easily cast from bytes to hexadecimal {@code String} and vice-versa.
 *
 * @author Raffaele
 */
public class EncodedBytesRepr implements TypeCaster<String, byte[]> {

    @Override
    public String castIn(byte[] e) throws ClassCastException {
        Objects.requireNonNull(e);
        if (e.length == 0) {
            return "";
        }
        return CryptoUtil.encodeHex(e);
    }

    @Override
    public byte[] castOut(String value) throws ClassCastException {
        Objects.requireNonNull(value);
        if (value.isEmpty()) {
            return new byte[0];
        }

        try {
            return CryptoUtil.decodeHex(value.toCharArray());
        } catch (RuntimeException ex) {
            // IMPROVE Really not the best way to do that
            throw new ClassCastException(ex.getMessage());
        }
    }
}
