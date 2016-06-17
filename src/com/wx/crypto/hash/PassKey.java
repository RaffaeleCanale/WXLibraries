package com.wx.crypto.hash;

import com.wx.crypto.CryptoException;
import com.wx.crypto.CryptoUtil;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * This class permits to generate Hashes using for example MD5 or SHA algorithm.
 *
 * @author Raffaele
 * @version 1.1
 */
public class PassKey {

    public static final String MD5_ALGORITHM = "MD5";
    public static final String SHA_ALGORITHM = "SHA";
    private byte[] hash;
    private String algorithm;

    public PassKey(char[] key, String algorithm) throws CryptoException {
        this(CryptoUtil.toByteArray(key), algorithm);
    }
    
    public PassKey(char[] key, String algorithm, boolean includeSalt) throws CryptoException {
        this(CryptoUtil.toByteArray(includeSalt
                ? CryptoUtil.concat(CryptoUtil.getHashSalt(), key, CryptoUtil.getHashSalt())
                : key),
                algorithm);
    }

    public PassKey(byte[] key, String algorithm) throws CryptoException {
        testAlgorithm(algorithm);
        this.algorithm = algorithm;

        this.hash = generateHash(key);
    }

    public PassKey(String algorithm, byte[] hash) throws CryptoException {
        testAlgorithm(algorithm);
        this.hash = hash;
        this.algorithm = algorithm;
    }

    public PassKey(String algorithm, String hash) throws CryptoException {
        this(algorithm, CryptoUtil.decodeHex(hash.toCharArray()));
    }

    public String getAlgorithm() {
        return algorithm;
    }

    private void testAlgorithm(String name) throws CryptoException {
        if (!CryptoUtil.isHashAlgorithmSupported(name)) {
            throw new CryptoException("Unknown algorithm: " + name);
        }
    }

    /**
     * Generates hash data using the given crypt algorithm digest from the given
     * key.
     *
     * @return Hash data represented in byte[].
     */
    private byte[] generateHash(byte[] key) {
        try {
            return MessageDigest.getInstance(algorithm).digest(key);
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException(algorithm + " does not run in this Virtual Machine. Amazing!...");
        }
    }

    /**
     *
     * Creates a String representation of hash code.
     *
     * @return Returns hash encoded in a String
     *
     */
    public String getEncodedHash() {
        return CryptoUtil.encodeHex(hash);
    }

    /**
     * @return Returns the hash code.
     */
    public byte[] getHash() {
        return Arrays.copyOf(hash, hash.length);
    }

    /**
     * Compares hash of two PassKeys
     *
     * @param obj The PassKey to compare
     *
     * @return Returns true if hash are equals.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PassKey other = (PassKey) obj;
        if ((this.algorithm == null) ? (other.algorithm != null) : !this.algorithm.equals(other.algorithm)) {
            return false;
        }

        return Arrays.equals(this.hash, other.hash);
    }

    @Override
    public int hashCode() {
        int hashCode = 7;
        hashCode = 67 * hashCode + Arrays.hashCode(this.hash);
        return hashCode;
    }
}
