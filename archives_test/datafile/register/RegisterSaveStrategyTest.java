package com.wx.io.datafile.register;

import com.wx.io.Accessor;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author Raffaele Canale - raffaelecanale@gmail.com
 */
@Ignore
public abstract class RegisterSaveStrategyTest extends Assert {
    
    private final RegisterColumn[] columns = createTestColumns();
    private final RegisterSaveStrategy strategy = createStrategy(columns);
    
    
    @Test(expected = AssertionError.class)
    public void testFirstColIsID() {
        createStrategy(new RegisterColumn[]{
            new RegisterColumn("some", RegisterColType.string)});
    }
    
    @Test(expected = AssertionError.class)
    public void testWriteBadValues1() throws IOException {
        Object[] badValues = {32, "test", 23};
        strategy.write(badValues, new Accessor().setOut(new ByteArrayOutputStream()));
    }
    
    @Test(expected = Exception.class)
    public void testWriteBadValues2() throws IOException {
        Object[] badValues = {32, "test", 23, "hello"};
        strategy.write(badValues, new Accessor().setOut(new ByteArrayOutputStream()));
    }
    
    @Test(expected = IOException.class)
    public void testReadBadValues1() throws IOException {
        Object[] badValues = {32, "test", 23};
        RegisterColumn[] badColumns = {
            new RegisterColumn("col1", RegisterColType.id),
            new RegisterColumn("col2", RegisterColType.string, 32),
            new RegisterColumn("col3", RegisterColType.integer)};
        RegisterSaveStrategy badStrategy = createStrategy(badColumns);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Accessor accessor = new Accessor().setOut(baos);
        
        badStrategy.write(badValues, accessor);
        accessor.close();
        
        accessor.setIn(new ByteArrayInputStream(baos.toByteArray()));        
        strategy.read(accessor);
    }
    
    //@Test(expected = IOException.class)
    /*
     * Finally not tested since such corruption cannot be easily tested
     */
    public void testReadBadValues2() throws IOException {
        Object[] badValues = {32, "test", 23, "hello"};
        RegisterColumn[] badColumns = {
            new RegisterColumn("col1", RegisterColType.id),
            new RegisterColumn("col2", RegisterColType.string, 32),
            new RegisterColumn("col3", RegisterColType.integer),
            new RegisterColumn("col4", RegisterColType.string, 32)};
        RegisterSaveStrategy badStrategy = createStrategy(badColumns);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();        
        Accessor accessor = new Accessor().setOut(baos);
        
        badStrategy.write(badValues, accessor);
        accessor.close();
        
        accessor.setIn(new ByteArrayInputStream(baos.toByteArray()));        
        Object[] read = strategy.read(accessor);
    }
    
    @Test
    public void encodeDecodeTest() throws IOException {
        Object[] values = {32, "test", 21, 2.4f};
        testReadWrite(values);
    }    
    
    private void testReadWrite(Object[] values) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (Accessor accessor = new Accessor().setOut(baos)) {
            strategy.write(values, accessor);
        }
        Object[] result = strategy.read(new Accessor().setIn(new ByteArrayInputStream(baos.toByteArray())));
        assertArrayEquals(values, result);
    }
    
    @Test
    public void nullTest() throws IOException {
        Object[] values = {32, null, 21, 2.5f};
        testReadWrite(values);
    }
    
    @Test
    public void eofTest() throws IOException {
        ByteArrayInputStream bis = new ByteArrayInputStream(new byte[0]);
        assertNull(strategy.read(new Accessor().setIn(bis)));
    }

    private RegisterColumn[] createTestColumns() {
        return new RegisterColumn[] {
            new RegisterColumn("col1", RegisterColType.id),
            new RegisterColumn("col2", RegisterColType.string, 32),
            new RegisterColumn("col3", RegisterColType.integer),
            new RegisterColumn("col4", RegisterColType.real)
        };
    }

    protected abstract RegisterSaveStrategy createStrategy(RegisterColumn[] columns);
    
    
    
}