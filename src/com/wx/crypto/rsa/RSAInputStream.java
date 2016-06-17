package com.wx.crypto.rsa;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;

/**
 *
 * @author Raffaele Canale, Triin Merivald
 *
 */
public class RSAInputStream extends InputStream {

    private final DataInputStream in;
    private final KeyPair pair;
    private final boolean mEncrypt;

    /**
     *
     * @param pair Key to be used for the RSA decryption
     * @param is   Source stream
     * @param encrypt {@code true} to set this input stream into encryption mode
     */
    public RSAInputStream(KeyPair pair, InputStream is, boolean encrypt) {
        this.in = new DataInputStream(is);
        this.pair = pair;
        this.mEncrypt = encrypt;
    }

    @Override
    public int read() throws IOException {
        byte[] data = new byte[(pair.getKeyLength() / 8) + 1];

        int bytesRead = in.read(data);
        if (bytesRead == -1) {
            return -1;
        }
        
        if (bytesRead != data.length) {
            throw new IOException("Wrong padding");
        }
        return mEncrypt ? pair.encrypt(new BigInteger(data)).intValue() :
                              pair.decrypt(new BigInteger(data)).intValue();
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
