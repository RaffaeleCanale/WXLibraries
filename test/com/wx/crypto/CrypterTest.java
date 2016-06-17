/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wx.crypto;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Random;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author Raffaele Canale - raffaelecanale@gmail.com
 * @version 0.1
 *
 * Project: WXLibraries File: CrypterTest.java (UTF-8) Date: 30 déc. 2012
 */
@Ignore
public abstract class CrypterTest extends Assert {

    private static final int MAX_STRING_LENTGH = 1600;
    private static final int MIN_STRING_LENTGH = 5;

    private static final char[] TEST_PASSWORD = "password".toCharArray();
    private static final byte[] TEST_SALT = "testsalt".getBytes();
    private static final byte[] TEST_IV = "0123456789abcdff".getBytes();

    private static String createTestString() {
        int length = new Random().nextInt(MAX_STRING_LENTGH - MIN_STRING_LENTGH)
                + MIN_STRING_LENTGH;
        StringBuilder result = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int code = new Random().nextInt(3);
            char c;
            switch (code) {
                case 0:
                    c = (char) ('0' + new Random().nextInt(10));
                    break;
                case 1:
                    c = (char) ('a' + new Random().nextInt(26));
                    break;
                case 2:
                    c = (char) ('A' + new Random().nextInt(26));
                    break;
                default:
                    throw new AssertionError();
            }
            result.append(c);
        }
        return result.toString();
    }

    private final String TEST_STRING = createTestString();
    private Crypter crypter;

    @Before
    public void setUp() {
        crypter = getNewCrypter();
    }

    @Test(expected = IllegalStateException.class)
    public void doubleKeyTest() throws CryptoException {
        crypter.generateRandomKey();
        crypter.generateRandomKey();
    }

    @Test(expected = IllegalStateException.class)
    public void noKeyTest() throws CryptoException {
        cryptionTest(null);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void wrongKeySizeTest() {
        crypter.setKeyLength(-2);
    }

    @Test
    public void testRandomKey() throws CryptoException, IOException {        
        crypter.generateRandomKey();

        streamTest(null);
//        streamTest(TEST_IV);
        cryptionTest(null);
//        cryptionTest(TEST_IV);
    }
  
    @Test
    public void testPBE() throws CryptoException, IOException {
        crypter.generateKey(TEST_PASSWORD, TEST_SALT);
        
        streamTest(null);
//        streamTest(TEST_IV);
        cryptionTest(null);
//        cryptionTest(TEST_IV);
    }
    
//    @Test  IV is not really working yet
    public void testWrongIV() throws CryptoException {
        crypter.generateRandomKey();
        
        byte[] data = crypter.encrypt(TEST_STRING, TEST_IV);
        
        byte[] otherIv = Arrays.copyOf(TEST_IV, TEST_IV.length);
        otherIv[0] = (byte) (TEST_IV[0] + 1);
        
        String result = new String(crypter.decrypt(data, otherIv));
        assertFalse(TEST_STRING.equals(result));
    }

    @Test
    public void testSameKey() throws CryptoException {
        crypter.generateRandomKey();

        byte[] key = crypter.getKey();

        Crypter otherCrypter = getNewCrypter();
        otherCrypter.initKey(key);

        assertArrayEquals(key, otherCrypter.getKey());
    }

    private void streamTest(byte[] iv) throws CryptoException, IOException {
        byte[] testBytes = TEST_STRING.getBytes();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (OutputStream out = crypter.getOutputStream(baos, iv, true)) {
            out.write(testBytes);
            out.flush();
        }

        byte[] encryptedBytes = baos.toByteArray();

        ByteArrayInputStream bais = new ByteArrayInputStream(encryptedBytes);
        InputStream in = crypter.getInputStream(bais, iv, false);

        baos.reset();
        int n;
        while ((n = in.read()) > -1) {
            baos.write(n);
        }
        byte[] result = baos.toByteArray();

        assertArrayEquals(testBytes, result);
    }


    private void cryptionTest(byte[] iv) throws CryptoException {

        byte[] data = crypter.encrypt(TEST_STRING, iv);

        String result = new String(crypter.decrypt(data, iv));
        assertEquals(TEST_STRING, result);
    }

    protected abstract Crypter getNewCrypter();
}
