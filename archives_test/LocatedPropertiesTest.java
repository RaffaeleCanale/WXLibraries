package com.wx.resource;

import com.wx.crypto.Crypter;
import com.wx.crypto.CryptoException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Raffaele Canale - raffaelecanale@gmail.com
 */
public class LocatedPropertiesTest extends Assert {

//    private static final String EXAMPLE_DIR = "dir1/dir2/";
//    private static final String EXAMPLE_NAME = "file.property";
    
    private final Map<Object, Object> testProperties = createTestProperties();
    private final ByteArrayOutputStream baos = new ByteArrayOutputStream();

    public LocatedPropertiesTest() {
    }

    private AbstractProperties createNewLocatedProperties() {
        return new AbstractProperties(false, null) {
            private static final long serialVersionUID = 1231L;

            @Override
            protected InputStream getInputStream(Crypter crypter,
                    Class<?> source) throws FileNotFoundException, CryptoException {                
                return new ByteArrayInputStream(baos.toByteArray());
            }

            @Override
            protected OutputStream getOutputStream(Crypter crypter) throws FileNotFoundException, CryptoException {
                return baos;
            }

        };
    }

    @Test
    public void testAll() throws FileNotFoundException, IOException, CryptoException {
        AbstractProperties props = createNewLocatedProperties();
        props.putAll(testProperties);
        props.saveProperties();

//        byte[] writtenBytes = baos.toByteArray();
        props = createNewLocatedProperties();
        props.load((Class<?>) null);

        assertMapEquals(testProperties, props);
        
        
       
    }

    private void assertMapEquals(Map<Object, Object> a, Map<Object, Object> b) {
        for (Map.Entry<Object, Object> entry : a.entrySet()) {
            assertTrue(b.containsKey(entry.getKey()));
            assertEquals(entry.getValue(), b.get(entry.getKey()));
        }
    }

    private Map<Object, Object> createTestProperties() {
        Map<Object, Object> result = new HashMap<>();
        result.put("key1", "value1");
        result.put("key2", "value2");
        result.put("key3", "");

        return result;
    }

}