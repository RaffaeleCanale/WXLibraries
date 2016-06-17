package com.wx.crypto.rsa;

import java.math.BigInteger;

/**
 * This class is a representation of a RSA key
 *
 * @author Raffaele Canale, Triin Merivald
 */
public class KeyPair {

    private BigInteger n;	//Modulo
    private BigInteger e;	//Public key, Encryption key
    private BigInteger d;	//Private key, Decryption key
    private int N;			//Multiple of 8, key length

    /**
     * Initiates a key with the given parameters.
     *
     * @param n Modulo.
     * @param e Encryption key.
     * @param d Decryption key.
     * @param N Key length, should be a multiple of 8.
     */
    public KeyPair(BigInteger n, BigInteger e, BigInteger d, int N) {
        if (N % 8 != 0) {
            throw new IllegalArgumentException("N must be a multiple of 8");
        }
        this.n = n;
        this.e = e;
        this.d = d;
        this.N = N;
    }

    /**
     * Encrypts a BigInteger
     *
     * @param m Value to encrypt
     * @return Returns m <sup>e</sup> (mod n)
     */
    public BigInteger encrypt(BigInteger m) {
        return m.modPow(e, n);
    }

    /**
     * Decrypts a BigInteger
     *
     * @param c Value to decrypt
     * @return Returns c <sup>d</sup> (mod n)
     */
    public BigInteger decrypt(BigInteger c) {
        return c.modPow(d, n);
    }

    public int getKeyLength() {
        return N;
    }

    public BigInteger getPublicKey() {
        return e;
    }

    public BigInteger getModulo() {
        return n;
    }

    @Override
    public String toString() {
        return "Modulo: " + n + "\n"
                + "Encryption key: " + e + "\n"
                + "Decryption key: " + d + "\n"
                + "Key length: " + N;
    }
}
