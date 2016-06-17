package com.wx.crypto;

import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * This exception regroups all possible exceptions when using encryption.
 * @author Raffaele
 */
public class CryptoException extends GeneralSecurityException {
    private static final long serialVersionUID = 1L;
    
    public CryptoException() {
        super();
    }
    
    public CryptoException(String message) {
        super(message);
    }
    
    public CryptoException(String message, Exception ex, Crypter cipher) {
        super(buildMessage(message, ex, cipher));
    }

    public CryptoException(String message, Exception ex) {
        super(buildMessage(message, ex, null));
    }

    public CryptoException(IllegalBlockSizeException ex, Crypter cipher) {
        this("The length of blocks is invalid. This could occur from the use of"
                + " a wrong algorithm."
                , ex, cipher);
    }

    public CryptoException(BadPaddingException ex, Crypter cipher) {
        this("Data is not correctly padded. This could occur from the use of a"
                + " wrong algorithm."
                , ex, cipher);
    }

    public CryptoException(InvalidKeyException ex, Crypter cipher) {
        this("The key is not valid. This could occur if the key has not been"
                + " correctly initialized."
                , ex, cipher);
    }

    public CryptoException(NoSuchAlgorithmException ex, Crypter cipher) {
        this("This VM doesn't support this algorithm.", ex,  cipher);
    }

    public CryptoException(NoSuchPaddingException ex, Crypter cipher) {
        this("This VM doesn't support this padding algorithm.", ex, cipher);
    }

    private static String buildMessage(String message, Exception ex, Crypter cipher) {
        String tmp = cipher == null ? "" : cipher + "\n";
        if (message == null) {
            message = "Unknown error";
        }

        return message + "\n" + tmp + "\nDetails: " + ex.getMessage();
    }

    /*public CryptoException(Exception ex, Crypter cipher) {
        this("Unknown exception.", ex, cipher);
    }//*/
}
