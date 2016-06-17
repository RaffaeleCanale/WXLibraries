package com.wx.crypto;

import com.wx.crypto.cipher.AESCrypter;
import com.wx.crypto.cipher.BlowfishCrypter;
import com.wx.crypto.cipher.DESCrypter;
import com.wx.crypto.hash.PassKey;
import com.wx.crypto.rsa.KeyPair;
import com.wx.crypto.rsa.RSACrypter;
import com.wx.crypto.symmetric.SymmetricCrypter;
import com.wx.io.Accessor;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.security.SecureRandom;
import java.util.Arrays;

/**
 * This is an utility class containing useful methods and attributes for
 * encryption/decryption.
 *
 * @author Raffaele Canale, Triin Merivald
 * @version 2.0
 */
public class CryptoUtil {

    public static final int MIN_SALT_LENGTH = 4;
    /*
    public static final CipherInfo BLOWFISH =
            new CipherInfo("Blowfish", 128, "Blowfish");
    public static final CipherInfo DES =
            new CipherInfo("DES/ECB/PKCS5Padding", 56, "DES");
    public static final CipherInfo AES = new CipherInfo("AES", 128, "AES");
    //*/
    private static char[] sCipherSalt = "lGaCH2A0".toCharArray();
    private static char[] sHashSalt = "bLNBMnLo".toCharArray();

    public static byte[] generateRandomSalt(int size) {
        byte[] salt = new byte[size];
        new SecureRandom().nextBytes(salt);
        
        return salt;
    }
    
    /**
     * Sets the default salt used for generating Cipher keys.<br> <font
     * color='#FF4000'><b>Warning:</b></font> If the salt changes, generated key
     * will change.<br> Make sure to always use the same salt.
     *
     * @param salt Salt to use
     *
     * @throws IllegalArgumentException if the salt is less than
     * {@link #MIN_SALT_LENGTH} characters long
     * @deprecated 
     */
    @Deprecated
    public static void setCipherSalt(char[] salt) {
        if (salt == null) {
            throw new NullPointerException("Salt is null");
        }
        if (salt.length < MIN_SALT_LENGTH) {
            throw new IllegalArgumentException("Salt should have a length of at"
                    + " least " + MIN_SALT_LENGTH);
        }
        sCipherSalt = Arrays.copyOf(salt, salt.length);
    }

    /**
     * Sets the default salt used for generating Hashes.<br> <font
     * color='#FF4000'><b>Warning:</b></font> If the salt changes, generated key
     * will change.<br> Make sure to always use the same salt.
     *
     * @param salt Salt to use
     *
     * @throws IllegalArgumentException if the salt is less than
     * {@link #MIN_SALT_LENGTH} characters long
     * * @deprecated 
     */
    @Deprecated
    public static void setHashSalt(char[] salt) {
        if (salt == null) {
            throw new NullPointerException("Salt is null");
        }
        if (salt.length < MIN_SALT_LENGTH) {
            throw new IllegalArgumentException("Salt should have a length of at"
                    + " least " + MIN_SALT_LENGTH);
        }

        sHashSalt = Arrays.copyOf(salt, salt.length);
    }

    /**
     * Gets a copy the actual cipher salt.
     *
     * @return The actual cipher salt
     *
     * @see #setCipherSalt(char[])
     * 
     */
    @Deprecated
    public static char[] getCipherSalt() {
        return Arrays.copyOf(sCipherSalt, sCipherSalt.length);
    }

    /**
     * Gets a copy the actual hash salt.
     *
     * @return The actual cipher salt
     *
     * @see #setHashSalt(char[])
     */
    @Deprecated
    public static char[] getHashSalt() {
        return Arrays.copyOf(sHashSalt, sHashSalt.length);
    }
    
    /**
     * Gets the {@code Crypter} corresponding to the given algorithm name.
     * @param name Name of the {@code Crypter} algorithm
     * @return The corresponding {@code Crypter} or {@code null} if not found.
     */
    public static Crypter getCrypterFromName(String name) {        
        Crypter[] crypters = getSupportedCryterAlgorithms();
        
        for (Crypter crypter : crypters) {
            if (crypter.getAlgorithmName().equals(name)) {
                return crypter;
            }
        }
        return null;
    }
    
    public static boolean isHashAlgorithmSupported(String hashAlgorithm) {
        for (String algorithm : getSupportedHashAlgorithms()) {
            if (algorithm.equals(hashAlgorithm)) {
                return true;
            }
        }
        return false;
    }

    //<editor-fold defaultstate="collapsed" desc="Deprecated">
    /**
     * Generates a valid RSA KeyPair with random p and q.
     *
     * @param N Key length.
     *
     * @return Returns a valid instance of KeyPair
     */
    @Deprecated
    public static KeyPair generateRSAKeyPair(int N) {
        //Generate random primes p and q
        BigInteger p = BigInteger.probablePrime(N / 2, new SecureRandom());
        BigInteger q = BigInteger.probablePrime(N / 2, new SecureRandom());
        
        //Compute n = pq
        BigInteger n = p.multiply(q);
        
        //Compute phi = (p-1)(q-1)
        BigInteger phi = p.subtract(BigInteger.ONE).multiply(
                q.subtract(BigInteger.ONE));
        
        //Choose any integer 1 < e < phi so that gcd(e, phi) = 1,
        //here we pick 65537
        BigInteger e = new BigInteger("65537");
        //As said, gcd(e, phi) = 1, if not the case, we should find others
        //p and q.
        if (!e.gcd(phi).equals(BigInteger.ONE)) {
            return generateRSAKeyPair(N);
        }
        
        //Find d such that ed = 1 mod phi (modular inverse of e mod phi)
        BigInteger d = e.modInverse(phi);
        
        return new KeyPair(n, e, d, N);
    }
    
    /**
     * Generates a random Symmetric key respecting the given length.
     *
     * @param keyLength Key length
     * @return Returns a Symmetric key
     */
    @Deprecated
    public static byte[] generateSymmetricKey(int keyLength) {
        byte symmetricKey[] = new byte[keyLength];
        
        SecureRandom random = new SecureRandom();
        random.nextBytes(symmetricKey);
        
        return symmetricKey;
    }
    
    
    //</editor-fold>

    /**
     * Concatenates multiple {@code char} arrays.
     *
     * @param arrays The arrays to concatenate
     * @return The concatenated arrays
     */
    public static char[] concat(char[]... arrays) {
        if (arrays == null) {
            throw new NullPointerException();
        }

        // Calculate the total length
        int totalLength = 0;
        for (char[] array : arrays) {
            if (array == null) {
                throw new NullPointerException();
            }
            totalLength += array.length;
        }

        char[] result = new char[totalLength];

        // Fill the result
        int lastIndex = 0;
        for (char[] array : arrays) {
            System.arraycopy(array, 0, result, lastIndex, array.length);
            lastIndex += array.length;
        }

        return result;
    }

    /**
     * Transform the given {@code char} sequence into a sequence of bytes. Use
     * {@link String#String(byte[])} constructor to transform back the bytes
     * into a char sequence.
     *
     * @param sequence String to cast into bytes
     *
     * @return Encode bytes for the given sequence
     */
    public static byte[] toByteArray(String sequence) {
        return toByteArray(sequence.toCharArray(), Charset.defaultCharset());
    }

    /**
     * Transform the given {@code char} sequence into a sequence of bytes. Use
     * {@link String#String(byte[])} constructor to transform back the bytes
     * into a char sequence.
     *
     * @param array Chars to cast into bytes
     *
     * @return Encode bytes for the given sequence
     */
    public static byte[] toByteArray(char[] array) {
        return toByteArray(array, Charset.defaultCharset());
    }

    /**
     * Transform the given {@code char} sequence into a sequence of bytes. Use
     * {@link String#String(byte[])} constructor to transform back the bytes
     * into a char sequence.
     *
     * @param array       Chars to cast into bytes
     * @param charsetName Charset to use for encoding
     *
     * @return Encode bytes for the given sequence
     */
    public static byte[] toByteArray(char[] array, String charsetName) {
        return toByteArray(array, Charset.forName(charsetName));
    }

    /**
     * Transform the given {@code char} sequence into a sequence of bytes. Use
     * {@link String#String(byte[])} constructor to transform back the bytes
     * into a char sequence.
     *
     * @param array Chars to cast into bytes
     * @param cs    Charset to use for encoding
     *
     * @return Encode bytes for the given sequence
     */
    public static byte[] toByteArray(char[] array, Charset cs) {
        CharBuffer cb = CharBuffer.wrap(array);
        ByteBuffer bb = cs.encode(cb);
        byte[] bytes = bb.array();
        byte[] result = Arrays.copyOf(bytes, bb.limit());
        Arrays.fill(bytes, (byte) 0);

        return result;
    }
    
    /**
     * @return An array containing all the supported hash algorithm names
     */
    public static String[] getSupportedHashAlgorithms() {
        return new String[]{PassKey.MD5_ALGORITHM, PassKey.SHA_ALGORITHM};
    }
    
    /**
     * @return An array containing an instance of each supported crypter. Those
     * crypter do not contain any key and that must be generated before use.
     */
    public static Crypter[] getSupportedCryterAlgorithms() {
        return new Crypter[]{new AESCrypter(), new BlowfishCrypter(),
            new DESCrypter(), new RSACrypter(), new SymmetricCrypter()};        
    }
    
    /**
     * Encodes the given bytes into an hexadecimal representation.
     *
     * @param data Bytes to encode
     * @return A String representation of the hexadecimal encoded bytes
     */
    public static String encodeHex(byte[] data) {
        String encodedHash = "";
        for (byte b : data) {
            encodedHash += Integer.toHexString(
                    (0x000000ff & b) | 0xffffff00).substring(6);
        }
        return encodedHash;
    }

    /**
     * Converts an array of characters representing hexadecimal values into an
     * array of bytes of those same values. The returned array will be half the
     * length of the passed array, as it takes two characters to represent any
     * given byte. An exception is thrown if the passed char array has an odd
     * number of elements.
     *
     * @param data An array of characters containing hexadecimal digits
     * @return A byte array containing binary data decoded from the supplied
     * char array.
     * @throws RuntimeException Thrown if an odd number or illegal of characters
     * is supplied
     */
    public static byte[] decodeHex(char[] data) throws RuntimeException {
        int len = data.length;

        if ((len & 0x01) != 0) {
            throw new RuntimeException("Odd number of characters.");
        }

        byte[] out = new byte[len >> 1];

        // two characters form the hex value.
        for (int i = 0, j = 0; j < len; i++) {
            int f = toDigit(data[j], j) << 4;
            j++;
            f = f | toDigit(data[j], j);
            j++;
            out[i] = (byte) (f & 0xFF);
        }

        return out;
    }

    /**
     * Converts a hexadecimal character to an integer.
     *
     * @param ch A character to convert to an integer digit
     * @param index The index of the character in the source
     * @return An integer
     * @throws RuntimeException Thrown if ch is an illegal hex character
     */
    protected static int toDigit(char ch, int index) throws RuntimeException {
        int digit = Character.digit(ch, 16);
        if (digit == -1) {
            throw new RuntimeException("Illegal hexadecimal character " + ch
                    + " at index " + index);
        }
        return digit;
    }

    /**
     * @param file
     * @param algorithm
     * @return
     * @throws IOException 
     * 
     * @deprecated This method creates a byte array containing the whole file
     */
    public static byte[] fileHash(File file, String algorithm) throws
                                                                   IOException, CryptoException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try (Accessor accessor = new Accessor().setIn(file).setOut(bos)) {
            accessor.pourInOut();
        }
        

        PassKey pk = new PassKey(bos.toByteArray(), algorithm);
        return pk.getHash();
    }
    
    private CryptoUtil() {
    }
}
