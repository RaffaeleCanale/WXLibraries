/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.wx.crypto.symmetric;

import com.wx.crypto.Crypter;
import com.wx.crypto.CrypterTest;

/**
 *
 * @author Raffaele Canale - raffaelecanale@gmail.com
 * @version 0.1
 *
 * Project: WXLibraries
 * File: SymmetricCrypterTest.java (UTF-8)
 * Date: 30 déc. 2012 
 */
public class SymmetricCrypterTest extends CrypterTest {

    @Override
    protected Crypter getNewCrypter() {
        return new SymmetricCrypter();
    }
    
}
