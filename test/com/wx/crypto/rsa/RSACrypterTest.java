/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.wx.crypto.rsa;

import com.wx.crypto.Crypter;
import com.wx.crypto.CrypterTest;
import com.wx.crypto.CryptoException;
import java.io.IOException;

/**
 *
 * @author Raffaele Canale - raffaelecanale@gmail.com
 * @version 0.1
 *
 * Project: WXLibraries
 * File: RSACrypterTest.java (UTF-8)
 * Date: 30 déc. 2012 
 */
public class RSACrypterTest extends CrypterTest {

    @Override
    protected Crypter getNewCrypter() {
        return new RSACrypter();
    }

    @Override
    public void testPBE() throws CryptoException, IOException {
        // Do nothing
    }

    @Override
    public void testSameKey() throws CryptoException {
        // Do nothing
    }

}
