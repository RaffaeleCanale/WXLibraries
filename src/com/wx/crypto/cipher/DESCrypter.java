/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.wx.crypto.cipher;

import com.wx.crypto.CryptoException;

/**
 *
 * @author Raffaele Canale - raffaelecanale@gmail.com
 * @version 0.1
 *
 * Project: WXLibraries
 * File: DESCrypter.java (UTF-8)
 * Date: 29 déc. 2012 
 */
public class DESCrypter extends CipherCrypter {
    
    //private static final String ALGORITHM_NAME = "DES";
    private static final String ALGORITHM_NAME = "DES/ECB/PKCS5Padding";
    private static final String GENERATOR_NAME = "DES";
    private static final int DEFAULT_KEY_LENGTH = 56;
    
    public DESCrypter() {
        super(ALGORITHM_NAME, GENERATOR_NAME, DEFAULT_KEY_LENGTH);
    }

    @Override
    public void initKey(byte[] hash) throws CryptoException {
        initKey(hash, 64);
    }

    @Override
    protected void generateKey(char[] password, byte[] salt, int keySize,
            int iterationsCount, String hashAlgorithm) throws CryptoException {
        super.generateKey(password, salt, 64, iterationsCount, hashAlgorithm);
    }
}
