/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wx.io.datafile.impl;

import com.wx.io.datafile.BufferedRegisterIterator;
import com.wx.io.datafile.DataFile;
import com.wx.io.datafile.DataFileTest;
import com.wx.io.datafile.page.DataPageFactory;
import com.wx.resource.property.Property;
import com.wx.resource.property.SimpleProperty;
import java.io.File;
import java.io.IOException;
import org.junit.Test;

/**
 *
 * @author Raffaele Canale - raffaelecanale@gmail.com
 */
public class MultiplePageStrategyTest extends DataFileTest {
    
    
    @Override
    protected DataFile getDataFile(File dir, DataPageFactory pageFactory) throws IOException {
        SimpleProperty<Integer> pageCount = new SimpleProperty<>();
        return getDataFile(dir, pageFactory, pageCount);
    }
    
    private DataFile getDataFile(File dir, DataPageFactory pageFactory, 
            Property<Integer> pageCount) throws IOException {
        String filesNamePrefix = "test_file";
        String filesExtension = ".dll";
        return new MultiplePageFile(pageCount, filesNamePrefix, 
                filesExtension, dir, pageFactory);
    }
    
    @Test
    public void multiPageTest() throws IOException {
        SimpleProperty<Integer> pageCount = new SimpleProperty<>();
        DataFile dataFile = getDataFile(createTemporaryDir(), pageFactory, pageCount);
        final int registersPerPage = (int) Math.floor(pageSize / registerLength);
        final int wantedPageCount = 5;
        final int totalRegisters = wantedPageCount * registersPerPage;
        
        Object[][] registers = initRegisters(totalRegisters);
        dataFile.addAllRegisters(registers);
        dataFile.flush();
        
        assertEquals(wantedPageCount, (int) pageCount.get());
        
        BufferedRegisterIterator bri = dataFile.readRegisters(-1);
        readAndMatch(registers, bri);
    }
    
}