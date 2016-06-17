package com.wx.crypto.symmetric;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;

/**
 *
 * @author Raffaele Canale, Triin Merivald
 *
 */
public class SymmetricOutputStream extends OutputStream {

    private final byte[] symmetricKey;
    private final OutputStream out;
    private int offsetInKey;

    /**
     *
     * @param symmetricKey Key to be used for the Symmetric encryption
     * @param os           Target stream
     * @param offset       Starting offset
     */
    public SymmetricOutputStream(OutputStream os, byte[] symmetricKey,
            int offset) {
        assert offset < symmetricKey.length;
        this.symmetricKey = Arrays.copyOf(symmetricKey, symmetricKey.length);
        this.out = os;
        offsetInKey = offset;
    }

    /**
     *
     * @param symmetricKey Key to be used for the Symmetric encryption
     * @param os           Target stream
     */
    public SymmetricOutputStream(OutputStream os, byte[] symmetricKey) {
        this.symmetricKey = Arrays.copyOf(symmetricKey, symmetricKey.length);
        this.out = os;
        offsetInKey = 0;
    }

    @Override
    public void write(int data) throws IOException {
        int temp = (data & 0xFF) ^ symmetricKey[offsetInKey];

        offsetInKey++;
        if (offsetInKey >= symmetricKey.length) {
            offsetInKey = 0;
        }

        out.write(temp);
    }

    @Override
    public void flush() throws IOException {
        out.flush();
    }

    @Override
    public void close() throws IOException {
        out.close();
    }
}
