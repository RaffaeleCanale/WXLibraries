package com.wx.crypto;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

/**
 * This is an abstract representation of a {@code Crypter} that allows to easily encrypt/decrypt any data.<br><br>
 * <p>
 * Note that the key has to be created before using the encryption/decryption methods.
 *
 * @author Raffaele Canale - raffaelecanale@gmail.com
 * @version 1.0
 *          <p>
 *          Project: WXLibraries File: Crypter.java (UTF-8) Date: 29 déc. 2012
 */
public abstract class Crypter {

    private static final String DEFAULT_ENCODING = "UTF-8";
    private static final int DEFAULT_ITERATIONS_COUNT = 256;
    private static final String DEFAULT_HASH_ALGORITHM = "HmacSHA1";

    private final int[] supportedKeyLengths;
    private int keyLength;
    private boolean keyReady;

    /**
     * {@code Crypter} constructor. <br> Note that the first given key will be used as default (if the user did not
     * provide one).
     *
     * @param supportedKeyLengths All the supported key lengths for this {@code Crypter}
     */
    public Crypter(int... supportedKeyLengths) {
        assert supportedKeyLengths != null && supportedKeyLengths.length > 0 : "No supported key length provided";
        this.supportedKeyLengths = supportedKeyLengths;
        this.keyLength = supportedKeyLengths[0];
        this.keyReady = false;
    }

    /**
     * Encrypts the given text interpreted in the {@code UTF-8} encoding. <br><br> Note: To use a different encoding,
     * extract bytes yourself and use {@link #encrypt(byte[]) }
     *
     * @param plainText Text to encrypt
     *
     * @return Encrypted bytes
     *
     * @throws CryptoException
     */
    public byte[] encrypt(String plainText) throws CryptoException {
        return encrypt(plainText, null);
    }

    /**
     * Encrypts the given text interpreted in the {@code UTF-8} encoding. <br><br> Note: To use a different encoding,
     * extract bytes yourself and use {@link #encrypt(byte[]) }
     *
     * @param plainText Text to encrypt
     * @param iv        Initialization vector (might be ignored by some implementations, read documentation)F
     *
     * @return Encrypted bytes
     *
     * @throws CryptoException
     */
    public byte[] encrypt(String plainText, byte[] iv) throws CryptoException {
        try {
            return encrypt(plainText.getBytes(DEFAULT_ENCODING), iv);
        } catch (UnsupportedEncodingException ex) {
            throw new CryptoException(DEFAULT_ENCODING
                    + " encoding not supported.", ex, this);
        }
    }

    /**
     * Encrypts the given data.
     *
     * @param data Data to encrypt
     *
     * @return Encrypted bytes
     *
     * @throws CryptoException
     */
    public byte[] encrypt(byte[] data) throws CryptoException {
        return encrypt(data, null);
    }

    /**
     * Encrypts the given data.
     *
     * @param data Data to encrypt
     * @param iv   Initialization vector (might be ignored by some implementations, read documentation)
     *
     * @return Encrypted bytes
     *
     * @throws CryptoException
     */
    public byte[] encrypt(byte[] data, byte[] iv) throws CryptoException {
        checkKey();
        return doFinal(data, iv, true);
    }

    /**
     * Decrypts the given cipher.
     *
     * @param data Data to decrypt
     *
     * @return Decrypted data
     *
     * @throws CryptoException
     */
    public byte[] decrypt(byte[] data) throws CryptoException {
        return decrypt(data, null);
    }

    /**
     * Decrypts the given cipher.
     *
     * @param data Data to decrypt
     * @param iv   Initialization vector (might be ignored by some implementations, read documentation)
     *
     * @return Decrypted data
     *
     * @throws CryptoException
     */
    public byte[] decrypt(byte[] data, byte[] iv) throws CryptoException {
        checkKey();
        return doFinal(data, iv, false);
    }

    /**
     * Generates a valid key for this {@code Crypter} from the given bytes.<br> <br> Note that on some implementations,
     * this might throw a {@link UnsupportedOperationException}.
     *
     * @param hash Bytes from which will the key be generated
     *
     * @throws CryptoException
     */
    public void initKey(byte[] hash) throws CryptoException {
        if (keyReady) {
            throw new IllegalStateException("Key already set");
        }

        initKey(hash, keyLength);
        keyReady = true;
    }

    //<editor-fold defaultstate="collapsed" desc="initKey">

    protected abstract void initKey(byte[] hash, int keySize)
            throws CryptoException;

    /**
     * Generate a key by hashing several times the given password. This is the best way to generate key for PBE
     * (Password Based Encryption).
     *
     * @param password Password for the key
     * @param salt     Salt for the hashing (recommended to be always different)
     *
     * @throws CryptoException
     */
    public void generateKey(char[] password, byte[] salt) throws CryptoException {
        generateKey(password, salt, DEFAULT_ITERATIONS_COUNT,
                DEFAULT_HASH_ALGORITHM);
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="generateKey">

    /**
     * Generate a key by hashing several times the given password. This is the best way to generate key for PBE
     * (Password Based Encryption).
     *
     * @param password        Password for the key
     * @param salt            Salt for the hashing (recommended to be always different)
     * @param iterationsCount Number of times the key will be hashed
     *
     * @throws CryptoException
     */
    public void generateKey(char[] password, byte[] salt,
                            int iterationsCount) throws CryptoException {
        generateKey(password, salt, iterationsCount, DEFAULT_HASH_ALGORITHM);
    }

    /**
     * Generate a key by hashing several times the given password. This is the best way to generate key for PBE
     * (Password Based Encryption).
     *
     * @param password        Password for the key
     * @param salt            Salt for the hashing (recommended to be always different)
     * @param iterationsCount Number of times the key will be hashed
     * @param hashAlgorithm   Hash algorithm
     *
     * @throws CryptoException
     */
    public void generateKey(char[] password, byte[] salt,
                            int iterationsCount, String hashAlgorithm) throws CryptoException {
        if (keyReady) {
            throw new IllegalStateException("Key already set");
        }

        generateKey(password, salt, keyLength, iterationsCount, hashAlgorithm);
        keyReady = true;
    }

    protected abstract void generateKey(char[] password, byte[] salt, int keySize,
                                        int iterationsCount, String hashAlgorithm) throws CryptoException;

    /**
     * Generates a random and valid key.
     * <p>
     * Note that on some implementations, this might throw a {@link UnsupportedOperationException}.
     *
     * @throws CryptoException
     */
    public void generateRandomKey() throws CryptoException {
        if (keyReady) {
            throw new IllegalStateException("Key already set");
        }

        generateRandomKey(keyLength);
        keyReady = true;
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="generateRandomKey">

    protected abstract void generateRandomKey(int size) throws CryptoException;

    /**
     * Gets an input stream implementing this {@code Crypter} algorithm.
     *
     * @param in      Input stream that will be wrapped
     * @param iv      Initialization vector (might be ignored by some implementations, read documentation)
     * @param encrypt {@code true} to set this {@code Crypter} to encrypt mode
     *
     * @return An {@code Crypter} input stream
     *
     * @throws CryptoException
     */
    public InputStream getInputStream(InputStream in, byte[] iv,
                                      boolean encrypt) throws CryptoException {
        checkKey();
        return getInputStream0(in, iv, encrypt);
    }
//</editor-fold>

    /**
     * Gets an input stream implementing this {@code Crypter} algorithm.
     *
     * @param in      Input stream that will be wrapped
     * @param encrypt {@code true} to set this {@code Crypter} to encrypt mode
     *
     * @return An {@code Crypter} input stream
     *
     * @throws CryptoException
     */
    public InputStream getInputStream(InputStream in, boolean encrypt)
            throws CryptoException {
        return getInputStream(in, null, encrypt);
    }

    /**
     * Gets an output stream implementing this {@code Crypter} algorithm.
     *
     * @param out     Output stream that will be wrapped
     * @param iv      Initialization vector (might be ignored by some implementations, read documentation)
     * @param encrypt {@code true} to set this {@code Crypter} to encrypt mode
     *
     * @return An {@code Crypter} output stream
     *
     * @throws CryptoException
     */
    public OutputStream getOutputStream(OutputStream out, byte[] iv,
                                        boolean encrypt)
            throws CryptoException {
        checkKey();
        return getOutputStream0(out, iv, encrypt);
    }

    /**
     * Gets an output stream implementing this {@code Crypter} algorithm.
     *
     * @param out     Output stream that will be wrapped
     * @param encrypt {@code true} to set this {@code Crypter} to encrypt mode
     *
     * @return An {@code Crypter} output stream
     *
     * @throws CryptoException
     */
    public OutputStream getOutputStream(OutputStream out, boolean encrypt)
            throws CryptoException {
        return getOutputStream(out, null, encrypt);
    }

    /**
     * @return The key length
     */
    public int getKeyLength() {
        if (keyReady) {
            return keyLength;
        } else {
            return -1;
        }
    }

    /**
     * Set the key length (in bits). <br><br> Note that a default key length is always automatically set.
     *
     * @param keyLength New key length
     *
     * @throws IllegalArgumentException If the key length is not supported
     */
    public void setKeyLength(int keyLength) throws IllegalArgumentException {
        for (int supported : supportedKeyLengths) {
            if (keyLength == supported) {
                this.keyLength = keyLength;
                return;
            }
        }

        throw new IllegalArgumentException(keyLength + " is not a supported key"
                + " size.\nSupported key lengths: "
                + Arrays.toString(supportedKeyLengths));
    }

    public int[] getSupportedKeyLengths() {
        return supportedKeyLengths;
    }

    public abstract String getAlgorithmName();

    public abstract byte[] getKey();

    /**
     * Perform a simple encryption or decryption on the given data.
     *
     * @param data    Data to process
     * @param iv      Initialization vector
     * @param encrypt {@code true} for encryption
     *
     * @return The encrypted or decrypted data
     *
     * @throws CryptoException
     */
    protected abstract byte[] doFinal(byte[] data, byte[] iv, boolean encrypt)
            throws CryptoException;

    protected abstract InputStream getInputStream0(InputStream in, byte[] iv,
                                                   boolean encrypt) throws CryptoException;

    protected abstract OutputStream getOutputStream0(OutputStream out,
                                                     byte[] iv, boolean encrypt) throws CryptoException;

    /**
     * Throws an {@link IllegalStateException} if the secret key is not created.
     */
    private void checkKey() {
        if (!keyReady) {
            throw new IllegalStateException("Key not created");
        }
    }
}
