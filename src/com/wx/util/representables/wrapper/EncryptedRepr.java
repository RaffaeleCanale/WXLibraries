package com.wx.util.representables.wrapper;

import com.wx.crypto.Crypter;
import com.wx.crypto.CryptoException;
import com.wx.util.representables.string.EncodedBytesRepr;

import java.util.Objects;

/**
 * Easily cast from bytes to a string representation of their encrypted value.
 *
 * @author Raffaele Canale
 * @version 2.0
 */
public class EncryptedRepr extends EncodedBytesRepr {

    private final Crypter crypter;


    /**
     * Creates a new CipherRepr. Output will be encrypted hexadecimal code.
     *
     * @param crypter {@code Crypter} used for encryption/decryption (must be initialized)
     */
    public EncryptedRepr(Crypter crypter) {
        this.crypter = Objects.requireNonNull(crypter);
    }

    /**
     * Encrypts the given bytes and encodes them into a {@code String}.
     *
     * @param e Bytes to encode
     *
     * @return A string representation of the encrypted bytes
     */
    @Override
    public String castIn(byte[] e) {
        try {
            return super.castIn(crypter.encrypt(Objects.requireNonNull(e)));
        } catch (CryptoException ex) {
            throw new ClassCastException(ex.toString());
        }
    }

    /**
     * Decodes the given string into bytes and decrypts them.
     *
     * @param value String representation of the bytes to decrypt
     *
     * @return The decrypted bytes
     */
    @Override
    public byte[] castOut(String value) {
        try {
            return crypter.decrypt(super.castOut(Objects.requireNonNull(value)));
        } catch (CryptoException e) {
            throw new ClassCastException(e.toString());
        }
    }
}
