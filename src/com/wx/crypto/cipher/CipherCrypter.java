/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wx.crypto.cipher;

import com.wx.crypto.Crypter;
import com.wx.crypto.CryptoException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author Raffaele Canale - raffaelecanale@gmail.com
 * @version 0.1
 *
 * Project: WXLibraries File: CipherCrypter.java (UTF-8) Date: 29 déc. 2012
 */
public abstract class CipherCrypter extends Crypter {

    private final String mAlgorithmName;
    private final String mKeyGeneratorName;
    private Key mSecretKey;
//    private int mKeyLength;

    public CipherCrypter(String algorithmName, String keyGeneratorName,
            int... defaultKeyLength) {
        super(defaultKeyLength);
        mAlgorithmName = algorithmName;
        mKeyGeneratorName = keyGeneratorName;
    }

    @Override
    protected void generateKey(char[] password, byte[] salt, int keySize,
            int iterationsCount, String hashAlgorithm) throws CryptoException {
        try {
            SecretKeyFactory factory
                    = SecretKeyFactory.getInstance("PBKDF2With" + hashAlgorithm);
            // TODO Maybe use PBEAlgorithm instead of hash
            KeySpec spec = new PBEKeySpec(password, salt, iterationsCount,
                    keySize);

            SecretKey tmp = factory.generateSecret(spec);
            mSecretKey = new SecretKeySpec(tmp.getEncoded(), mKeyGeneratorName);
//            mKeyLength = size;

        } catch (NoSuchAlgorithmException ex) {
            throw new CryptoException(ex, this);
        } catch (InvalidKeySpecException ex) {
            throw new CryptoException("", ex, this);
        }

    }

    @Override
    protected void initKey(byte[] hash, int size) throws CryptoException {
        byte[] keyData = Arrays.copyOf(hash, size / 8);
        mSecretKey = new SecretKeySpec(keyData, mKeyGeneratorName);
    }

    @Override
    protected void generateRandomKey(int size) throws CryptoException {
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance(mKeyGeneratorName);
            keyGen.init(size);
            mSecretKey = keyGen.generateKey();
        } catch (NoSuchAlgorithmException ex) {
            throw new CryptoException(ex, this);
        }
    }

    @Override
    public String getAlgorithmName() {
        return mAlgorithmName;
    }

    @Override
    public byte[] getKey() {
        return mSecretKey.getEncoded();
    }

    @Override
    protected byte[] doFinal(byte[] content, byte[] iv, boolean encryptMode)
            throws CryptoException {
        int cryptionMode = encryptMode ? Cipher.ENCRYPT_MODE
                : Cipher.DECRYPT_MODE;
        try {
            return initCipher(cryptionMode, iv).doFinal(content);
        } catch (IllegalBlockSizeException ex) {
            throw new CryptoException(ex, this);
        } catch (BadPaddingException ex) {
            throw new CryptoException(ex, this);
        }
    }

    @Override
    public InputStream getInputStream0(InputStream in, byte[] iv,
            boolean encrypt) throws CryptoException {
        int encryptonMode = encrypt ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE;
        return new CipherInputStream(in, initCipher(encryptonMode, iv));
    }

    @Override
    public OutputStream getOutputStream0(OutputStream out, byte[] iv, 
            boolean encrypt) throws CryptoException {
        int cryptonMode = encrypt ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE;
        return new CipherOutputStream(out, initCipher(cryptonMode, iv));
    }

    @Override
    public String toString() {
        return mAlgorithmName + " (key size: " + getKeyLength() + ")";
    }

    private Cipher initCipher(int cryptionMode, byte[] iv) throws CryptoException {
        assert mSecretKey != null;

        try {
            Cipher cipher = Cipher.getInstance(mAlgorithmName);
            if (iv == null) {
                cipher.init(cryptionMode, mSecretKey);
            } else {
                cipher.init(cryptionMode, mSecretKey);
                // TODO Iv not supported yet
//                cipher.init(cryptionMode, mSecretKey, new IvParameterSpec(iv));
                
            }
            return cipher;
        } catch (InvalidKeyException ex) {
            throw new CryptoException(ex, this);
        } catch (NoSuchAlgorithmException ex) {
            throw new CryptoException(ex, this);
        } catch (NoSuchPaddingException ex) {
            throw new CryptoException(ex, this);
//        } catch (InvalidAlgorithmParameterException ex) {
//            throw new CryptoException("", ex, this);
        }
    }

}
