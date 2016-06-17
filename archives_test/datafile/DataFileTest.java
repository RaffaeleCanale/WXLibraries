/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wx.io.datafile;

import com.wx.io.datafile.page.DataPageFactory;
import com.wx.io.datafile.page.impl.CachedOnlyPage;
import com.wx.io.datafile.register.RegisterColType;
import com.wx.io.datafile.register.RegisterColumn;
import com.wx.io.datafile.register.fixlen.FixedLengthRegister;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

/**
 *
 * @author Raffaele Canale - raffaelecanale@gmail.com
 */
@Ignore
public abstract class DataFileTest extends Assert {
    
    @Rule
    public TemporaryFolder tmpFolder = new TemporaryFolder();
    private final Object[][] registers = initRegisters(20);
    
    private final RegisterColumn[] columns = {
        new RegisterColumn("col1", RegisterColType.id),
        new RegisterColumn("col2", RegisterColType.integer),
        new RegisterColumn("col3", RegisterColType.real),
        new RegisterColumn("col4", RegisterColType.string, 64)
    };
    
    protected final DataPageFactory pageFactory = initPageFactory();
    protected final int pageSize = 4096;
    protected int registerLength;

    @Test
    public void testAdd() throws IOException {
        File dir = createTemporaryDir();
        DataFile dataFile = getDataFile(dir, pageFactory);
        dataFile.addAllRegisters(registers);
        dataFile.flush();
        
        BufferedRegisterIterator bri = dataFile.readRegisters(-1);
        readAndMatch(registers, bri);
    }
    
    
    private boolean contains(Object[] seek, Object[][] registers) {
        for (Object[] register : registers) {
            if (Arrays.equals(seek, register)) {
                return true;
            }
        }
        
        return false;
    }
    
    protected void readAndMatch(Object[][] registers, BufferedRegisterIterator bri) throws IOException {
        int count = 0;
        
        while (bri.hasNext()) {
            assertTrue(contains(bri.readRegister(), registers));
            count++;
        }
        
        assertEquals(registers.length, count);
        bri.close();
    }
    
    protected  Object[][] initRegisters(int count) {
        Object[][] result = new Object[count][];
        
        for (int i = 0; i < count; i++) {
            result[i] = new Object[] {i, i + 23, (float) (32.3 + i), "col" + i};
        }
        
        return result;
    }
    
    private DataPageFactory initPageFactory() {
        FixedLengthRegister flr = new FixedLengthRegister(columns);
        registerLength = flr.getRegisterSize();
        return CachedOnlyPage.getFactory(flr, pageSize);
    }
    
    
    protected abstract DataFile getDataFile(File dir, 
            DataPageFactory pageFactory) throws IOException;
    
    protected File createTemporaryDir() throws IOException {
        return tmpFolder.newFolder();
    }
}