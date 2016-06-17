package com.wx.crypto.symmetric;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

/**
 *
 * @author Raffaele Canale, Triin Merivald
 *
 */
public class SymmetricInputStream extends InputStream {

    private final byte[] symmetricKey;
    private final InputStream in;
    private int offsetInKey;

    /**
     *
     * @param symmetricKey Key to be used for the Symmetric decryption
     * @param is           Source stream
     * @param offset       Starting offset
     */
    public SymmetricInputStream(InputStream is, byte[] symmetricKey,
            int offset) {
        assert offset < symmetricKey.length : "offset > keylength: " + offset + " > " + symmetricKey.length;
        this.symmetricKey = Arrays.copyOf(symmetricKey, symmetricKey.length);
        this.in = is;
        offsetInKey = offset;
    }

    /**
     *
     * @param symmetricKey Key to be used for the Symmetric decryption
     * @param is           Source stream
     */
    public SymmetricInputStream(InputStream is, byte[] symmetricKey) {
        this.symmetricKey = Arrays.copyOf(symmetricKey, symmetricKey.length);
        this.in = is;
        offsetInKey = 0;
    }

    @Override
    public int read() throws IOException { //bitwise xor		
        int readByte = in.read();

        if (readByte == -1) {
            return readByte;
        }

        readByte = readByte ^ symmetricKey[offsetInKey];

        offsetInKey++;
        if (offsetInKey >= symmetricKey.length) {
            offsetInKey = 0;
        }

        return readByte & 0xFF;
    }

    @Override
    public void close() throws IOException {
        in.close();
    }

    @Override
    public int available() throws IOException {
        return in.available();
    }
}
