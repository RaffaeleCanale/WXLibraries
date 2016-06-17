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
 * File: AESCrypter.java (UTF-8)
 * Date: 29 déc. 2012 
 */
public class AESCrypter extends CipherCrypter {

    private static final String ALGORITHM_NAME = "AES";
    private static final int DEFAULT_KEY_LENGTH = 128;

    public AESCrypter() {
        super(ALGORITHM_NAME, ALGORITHM_NAME, DEFAULT_KEY_LENGTH);
    }

    public AESCrypter(byte[] key) throws CryptoException {
        this();
        initKey(key);
    }

    public AESCrypter(char[] password, byte[] salt) 
            throws CryptoException {
        this();
        generateKey(password, salt);
    }

}
