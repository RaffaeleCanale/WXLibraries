/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wx.io.datafile.impl;

import com.wx.io.datafile.DataFile;
import com.wx.io.datafile.DataFileTest;
import com.wx.io.datafile.page.DataPageFactory;
import java.io.File;
import java.io.IOException;
import org.junit.Test;

/**
 *
 * @author Raffaele Canale - raffaelecanale@gmail.com
 */
public class SinglePageFileTest extends DataFileTest {

    @Override
    protected DataFile getDataFile(File dir, DataPageFactory pageFactory) throws IOException {
        return new SinglePageFile("testFile", dir, pageFactory);
    }
    
    @Test (expected = IOException.class)
    public void testFullPage() throws IOException {
        File dir = createTemporaryDir();
        DataFile dataFile = getDataFile(dir, pageFactory);
        
        int count = (int) (Math.floor(pageSize / registerLength) + 1);
        dataFile.addAllRegisters(initRegisters(count));
    }
}