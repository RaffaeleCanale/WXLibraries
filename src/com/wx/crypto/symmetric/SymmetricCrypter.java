/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wx.crypto.symmetric;

import com.wx.crypto.Crypter;
import com.wx.crypto.CryptoException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 *
 * @author Raffaele Canale - raffaelecanale@gmail.com
 * @version 0.1
 *
 * Project: WXLibraries File: SymmetricCrypter.java (UTF-8) Date: 29 déc. 2012
 */
public class SymmetricCrypter extends Crypter {

    private static final String ALGORITHM_NAME = "Symmetric";
//    private static final int[] SUPPORTED_SIZES = {64, 128, 192, 256};

    private byte[] mKey;


    public SymmetricCrypter() {
        super(-1);
    }
//    @Override
//    public void initKey(byte[] key) throws CryptoException {
//        mKey = Arrays.copyOf(key, key.length);
//    }
    
    @Override
    protected void initKey(byte[] key, int size) throws CryptoException {
//        mKey = Arrays.copyOf(key, size / 8);
        mKey = key;
    }

    @Override
    protected void generateKey(char[] password, byte[] salt, int keySize, 
            int iterationsCount, String hashAlgorithm) throws CryptoException {
        try {
            SecretKeyFactory factory =
                    SecretKeyFactory.getInstance("PBKDF2With" + hashAlgorithm);
            // TODO Maybe use PBEAlgorithm instead of hash
            KeySpec spec = new PBEKeySpec(password, salt, iterationsCount, 
                    keySize);
            
            SecretKey tmp = factory.generateSecret(spec);
            mKey = tmp.getEncoded();
        } catch (NoSuchAlgorithmException ex) {
            throw new CryptoException(ex, this);
        } catch (InvalidKeySpecException ex) {
            throw new CryptoException("", ex, this);
        }
    }

    @Override
    protected final void generateRandomKey(int size) throws CryptoException {
        mKey = new byte[size / 8];
        SecureRandom random = new SecureRandom();
        random.nextBytes(mKey);
    }

    @Override
    public String getAlgorithmName() {
        return ALGORITHM_NAME;
    }

    @Override
    public byte[] getKey() {
        return Arrays.copyOf(mKey, mKey.length);
    }

    @Override
    public String toString() {
        return "Symmetric key (key size: " + getKeyLength() + ")";
    }

    @Override
    protected InputStream getInputStream0(InputStream in, byte[] iv,
            boolean encrypt)
            throws CryptoException {
        return new SymmetricInputStream(in, mKey, initOffset(iv, mKey.length));
    }

    @Override
    protected OutputStream getOutputStream0(OutputStream out, byte[] iv, 
            boolean encrypt)
            throws CryptoException {
        return new SymmetricOutputStream(out, mKey, initOffset(iv, mKey.length));
    }

    @Override
    protected byte[] doFinal(byte[] data, byte[] iv, boolean encrypt) {
        byte[] result = new byte[data.length];
        for (int i = 0, keyIndex = initOffset(iv, mKey.length); 
                i < data.length; 
                i++, keyIndex = (keyIndex + 1) % mKey.length) {
            result[i] = (byte) (data[i] ^ mKey[keyIndex]);
        }

        return result;
    }
    
    private int initOffset(byte[] iv, int keyByteLength) {
        if (iv == null) {
            return 0;
        } else {
            return new BigInteger(iv).remainder(
                    BigInteger.valueOf(keyByteLength)).intValue();
        }
    }
}
