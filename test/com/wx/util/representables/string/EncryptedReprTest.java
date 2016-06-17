package com.wx.util.representables.string;

import com.wx.crypto.Crypter;
import com.wx.crypto.CryptoException;
import com.wx.crypto.cipher.AESCrypter;
import com.wx.util.pair.Pair;
import com.wx.util.representables.GenericCasterTest;
import com.wx.util.representables.TypeCaster;
import com.wx.util.representables.wrapper.EncryptedRepr;
import org.junit.Test;

import java.io.InputStream;
import java.io.OutputStream;

import static org.junit.Assert.assertArrayEquals;

public class EncryptedReprTest extends GenericCasterTest<String, byte[]> {


    private static final Crypter crypter = initCrypter();
    private static final EncodedBytesRepr repr = new EncodedBytesRepr();

    private static Crypter initCrypter() {
        AESCrypter result = new AESCrypter();

        try {
            result.generateRandomKey();
        } catch (CryptoException e) {
            return null;
        }

        return result;
    }

    @Override
    protected TypeCaster<String, byte[]> getCaster() {
        return new EncryptedRepr(crypter);
    }

    @Override
    protected void setUpPairs() {
        byte[] fooBytes = "Lorem ipsum dolor sit amet".getBytes();

        try {
            String encrypted = repr.castIn(crypter.encrypt(fooBytes));


            pair(encrypted, fooBytes);

        } catch (CryptoException e) {
            // Do nothing
        }
    }

    @Override
    protected void assertOutEquals(byte[] expected, byte[] actual) {
        assertArrayEquals(expected, actual);
    }

    @Test(expected = ClassCastException.class)
    public void invalidCrypter() throws CryptoException {
        Crypter dummyCrypter = new Crypter(0) {
            @Override
            protected void initKey(byte[] hash, int keySize) throws CryptoException {

            }

            @Override
            protected void generateKey(char[] password, byte[] salt, int keySize, int iterationsCount, String hashAlgorithm) throws CryptoException {

            }

            @Override
            protected void generateRandomKey(int size) throws CryptoException {

            }

            @Override
            public String getAlgorithmName() {
                return null;
            }

            @Override
            public byte[] getKey() {
                return new byte[0];
            }

            @Override
            protected byte[] doFinal(byte[] data, byte[] iv, boolean encrypt) throws CryptoException {
                throw new CryptoException("Dummy crypter");
            }

            @Override
            protected InputStream getInputStream0(InputStream in, byte[] iv, boolean encrypt) throws CryptoException {
                throw new CryptoException("Dummy crypter");
            }

            @Override
            protected OutputStream getOutputStream0(OutputStream out, byte[] iv, boolean encrypt) throws CryptoException {
                throw new CryptoException("Dummy crypter");
            }
        };
        dummyCrypter.generateRandomKey();

        new EncryptedRepr(dummyCrypter).castIn(new byte[10]);
    }

    @Test(expected = ClassCastException.class)
    public void invalidCrypter2() {
        castOut("0000");
    }


}