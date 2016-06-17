package com.wx.crypto.rsa;

import com.wx.crypto.Crypter;
import com.wx.crypto.CryptoException;
import com.wx.io.Accessor;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.security.SecureRandom;

/**
 *
 * @author Raffaele Canale - raffaelecanale@gmail.com
 * @version 0.1
 *
 * Project: WXLibraries
 * File: RSACrypter.java (UTF-8)
 * Date: 29 déc. 2012 
 */
public class RSACrypter extends Crypter {
    
    private static final int DEFAULT_KEY_LENGTH = 128;
    private static final String ALGORITHM_NAME = "RSA";

    private KeyPair mKeyPair;
        
    public RSACrypter() {
        super(DEFAULT_KEY_LENGTH);
    }
    
    @Override
    public void initKey(byte[] hash, int size) throws CryptoException {   
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected void generateKey(char[] password, byte[] salt, int keySize, int iterationsCount, String hashAlgorithm) throws CryptoException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void generateRandomKey(int size) throws CryptoException {
        //Generate random primes p and q
        BigInteger p = BigInteger.probablePrime(size / 2, new SecureRandom());
        BigInteger q = BigInteger.probablePrime(size / 2, new SecureRandom());

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
            generateRandomKey();
        } else {

            //Find d such that ed = 1 mod phi (modular inverse of e mod phi)
            BigInteger d = e.modInverse(phi);

            mKeyPair = new KeyPair(n, e, d, size);
        }
    }

    @Override
    public String getAlgorithmName() {
        return ALGORITHM_NAME;
    }

    @Override
    public byte[] getKey() {
        throw new UnsupportedOperationException("Not supported yet.");
    }    
    
    @Override
    protected InputStream getInputStream0(InputStream in, byte[] iv, 
            boolean encrypt) throws CryptoException {
        return new RSAInputStream(mKeyPair, in, encrypt);
    }

    @Override
    protected OutputStream getOutputStream0(OutputStream out, byte[] iv,
            boolean encrypt) throws CryptoException {
        return new RSAOutputStream(mKeyPair, out, encrypt);
    }

    @Override
    protected byte[] doFinal(byte[] data, byte[] iv, boolean encrypt) throws CryptoException {
        if (encrypt) {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            try (OutputStream out = getOutputStream(buffer, true)) {
                out.write(data);
                return buffer.toByteArray();
            } catch (IOException ex) {
                throw new CryptoException(null, ex, this);
            }
        
        } else {
            ByteArrayInputStream source = new ByteArrayInputStream(data);
            InputStream in = getInputStream(source, false);
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            try (Accessor accessor = new Accessor().setIn(in).setOut(buffer)) {
                accessor.pourInOut();
                return buffer.toByteArray();
            } catch (IOException ex) {
                throw new CryptoException(null, ex, this);
            }
        }
    }
}
