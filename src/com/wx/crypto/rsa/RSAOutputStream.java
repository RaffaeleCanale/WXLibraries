package com.wx.crypto.rsa;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;

/**
 * 
 * @author Raffaele Canale, Triin Merivald
 *
 */
public class RSAOutputStream extends OutputStream {

    private final DataOutputStream out;
    private final KeyPair pair;
    private final boolean mEncrypt;

    /**
     *
     * @param pair Key to be used for the RSA encryption
     * @param os Target stream
     */
    public RSAOutputStream(KeyPair pair, OutputStream os, boolean encrypt) {
        this.out = new DataOutputStream(os);
        this.pair = pair;
        this.mEncrypt = encrypt;
    }

    @Override
    public void write(int b) throws IOException {
        BigInteger bi = BigInteger.valueOf(b & 0xFF);
        byte[] data = mEncrypt ? pair.encrypt(bi).toByteArray() :
                                 pair.decrypt(bi).toByteArray();

        byte[] nData = new byte[(pair.getKeyLength() / 8) + 1];

        //Writes the data array in the nData array from the end to the beginning leaving 0s at the beginning if needed
        for (int i = data.length - 1, j = nData.length - 1; i >= 0; i--, j--) {
            nData[j] = data[i];
        }

        out.write(nData);
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
