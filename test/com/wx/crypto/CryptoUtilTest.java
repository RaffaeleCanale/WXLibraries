/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wx.crypto;

import static org.hamcrest.CoreMatchers.*;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
/**
 *
 * @author Raffaele Canale - raffaelecanale@gmail.com
 */
public class CryptoUtilTest extends Assert {
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
        
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    //<editor-fold defaultstate="collapsed" desc="CipherSalt Set">
    @Test(expected=NullPointerException.class)
    public void cipherSaltTestNull() {
        CryptoUtil.setCipherSalt(null);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void cipherSaltTestSmall() {
        char[] salt = createSalt(CryptoUtil.MIN_SALT_LENGTH - 1);
        CryptoUtil.setCipherSalt(salt);
    }
    
    @Test
    public void cipherSaltSet() {
        char[] salt = createSalt(CryptoUtil.MIN_SALT_LENGTH);
        CryptoUtil.setCipherSalt(salt);
        assertArrayEquals(salt, CryptoUtil.getCipherSalt());
    }
    
    @Test
    public void cipherSaltRefTest() {
        assertTrue(CryptoUtil.MIN_SALT_LENGTH > 0);
        
        char[] salt = createSalt(CryptoUtil.MIN_SALT_LENGTH);
        CryptoUtil.setCipherSalt(salt);
        assertNotSame(salt, CryptoUtil.getCipherSalt());
        
        salt = CryptoUtil.getCipherSalt();
        salt[0] = ' ';
        assertThat(salt[0], not(CryptoUtil.getCipherSalt()[0]));
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="HashSalt Set">
    @Test(expected=NullPointerException.class)
    public void hashSaltTestNull() {
        CryptoUtil.setHashSalt(null);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void hashSaltTestSmall() {
        char[] salt = createSalt(CryptoUtil.MIN_SALT_LENGTH - 1);
        CryptoUtil.setHashSalt(salt);
    }
    
    @Test
    public void hashSaltSet() {
        char[] salt = createSalt(CryptoUtil.MIN_SALT_LENGTH);
        CryptoUtil.setHashSalt(salt);
        assertArrayEquals(salt, CryptoUtil.getHashSalt());
    }
    
    @Test
    public void hashSaltRefTest() {
        assertTrue(CryptoUtil.MIN_SALT_LENGTH > 0);
        
        char[] salt = createSalt(CryptoUtil.MIN_SALT_LENGTH);
        CryptoUtil.setHashSalt(salt);
        assertNotSame(salt, CryptoUtil.getHashSalt());
        
        salt = CryptoUtil.getHashSalt();
        salt[0] = ' ';
        assertThat(salt[0], not(CryptoUtil.getHashSalt()[0]));
    }
    //</editor-fold>
        
    //<editor-fold defaultstate="collapsed" desc="concat">
    @Test(expected=NullPointerException.class)
    public void testNullConcat() {
        CryptoUtil.concat((char[][]) null);
    }
    
    @Test(expected=NullPointerException.class)
    public void testNullConcat2() {
        CryptoUtil.concat(createSalt(2), null, createSalt(1));
    }
    
    @Test
    public void testSingleConcat() {
        char[] test = createSalt(10);
        assertArrayEquals(test, CryptoUtil.concat(test));
    }
    
    @Test
    public void testMultipleConcat() {
        char[] expectedResult = createSalt(30);
        
        char[] array1 = new char[5];
        char[] array2 = new char[15];
        char[] array3 = new char[10];
        
        System.arraycopy(expectedResult, 0, array1, 0, array1.length);
        System.arraycopy(expectedResult, 5, array2, 0, array2.length);
        System.arraycopy(expectedResult, 20, array3, 0, array3.length);
        
        assertArrayEquals(expectedResult, CryptoUtil.concat(array1, array2,
                array3));
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="toBytes">    
    @Test
    public void toBytesTest() {
        char[] chars = createSalt(12); // A..L
        // As found in http://www.string-functions.com/string-hex.aspx
        String expectedHex = "4142434445464748494a4b4c";
        
        // Assumes decodeHex is correct
        byte[] expectedBytes = CryptoUtil.decodeHex(expectedHex.toCharArray());
        
        assertArrayEquals(expectedBytes, CryptoUtil.toByteArray(chars));
        assertArrayEquals(expectedBytes, CryptoUtil.toByteArray(
                new String(chars)));
        assertArrayEquals(expectedBytes, CryptoUtil.toByteArray(chars, "UTF8"));
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Supported algorithms">
    @Test
    public void supportedHashAlgorithmsTest() {
        String[] hashAlgorithms = CryptoUtil.getSupportedHashAlgorithms();
        assertNotNull(hashAlgorithms);
        assertTrue(hashAlgorithms.length > 0);
        
        assertNotSame(hashAlgorithms, CryptoUtil.getSupportedHashAlgorithms());
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="encodeHex">
    @Test(expected=NullPointerException.class)
    public void encodeNullHexTest() {
        CryptoUtil.encodeHex(null);
    }
    
    @Test
    public void encodeHexTest() {
        char[] chars = createSalt(12); // A..L
        // As found in http://www.string-functions.com/string-hex.aspx
        String expectedHex = "4142434445464748494a4b4c";
        
        byte[] data = CryptoUtil.toByteArray(chars);
        
        assertEquals(expectedHex, CryptoUtil.encodeHex(data));
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="decodeHex">
    @Test(expected=NullPointerException.class)
    public void decodeNullHexTest() {
        CryptoUtil.decodeHex(null);
    }
    
    @Test
    public void decodeHexTest() {
        char[] chars = createSalt(12); // A..L
        // As found in http://www.string-functions.com/string-hex.aspx
        String expectedHex = "4142434445464748494a4b4c";
        
        byte[] data = CryptoUtil.toByteArray(chars);
        
        assertArrayEquals(data,
                CryptoUtil.decodeHex(expectedHex.toCharArray()));
    }
    
    @Test(expected=RuntimeException.class)
    public void decodeOddHexTest() {
        char[] chars = new char[3];
        CryptoUtil.decodeHex(chars);
    }
    
    @Test(expected=RuntimeException.class)
    public void decodeInvalidHex() {
        char[] chars = "invalid  hex".toCharArray();
        CryptoUtil.decodeHex(chars);
    }
    //</editor-fold>
    
    private char[] createSalt(int length) {
        char[] salt = new char[length];
        for (int i = 0; i < length; i++) {
            salt[i] = (char)('A' + i);
        }
        return salt;
    }
}
